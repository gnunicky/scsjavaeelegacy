package com.bean.stateless;

import com.bean.singleton.StateLocal;
import com.*;
import com.message.*;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.jms.ConnectionFactory;
import javax.jms.JMSProducer;
import javax.jms.Topic;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Stateless(mappedName = "COOBean")
public class COO implements COOLocal{

    @EJB
    private StateLocal state;

    
//Resorucers -------------------------------------------------------------------    
    @Resource(mappedName = "jms/__defaultConnectionFactory")
    private ConnectionFactory cf;
    
    @Resource(mappedName = "jms/Topic") 
    private Topic topic;    
    
    @Resource
    private TimerService timerService;
    
//Queste risorse vengono iniettate attraverso il deployment description in modo
//in  modo da  permettere  di deployare più componeneti al variariare di alcuni 
//parametri. I valori messi nel codice sono quelli utilizzati di default.    
    @Resource(name ="ID")
    private int ID=0;               //Id del coordinator
    
    @Resource(name="queue")
    private String queue="QUEUE1";  //Coda di ascolto
    
    @Resource(name="N")
    private int N=3;                //Numero dei replication manager
    
    @Resource(name="abortTimeout")
    private int abortTimeout=5000;  //Timeout in cui viene verificato il quorum
    
    @Resource(name="timeToLive_ms") 
    private int timeToLive_ms;      //Tempo di vita di ogni messaggio JMS
//------------------------------------------------------------------------------    
  
    private int Qw;
    private int Qr;
    
    private JMSProducer producer;
        
    @PostConstruct
    public void init(){
/*        String msg="*** Coordinator "+ID+" is created ***\n"+
                   "RESOURCES:\n"+
                    "{\n- ID: "+ID+"\n"+
                    "- Numeber Replication manager: "+N+"\n"+
                    "- Return Queue: "+queue+"\n"+
                    "- Abort timeout: "+abortTimeout+"\n"+
                    "- Time to Live JMS Message "+timeToLive_ms+
                    "\n}\n";
        System.out.println(msg);
*/
        Qw=calculateWriteQuorum(N);
        Qr=calculateReadQuorum(N,Qw); 
        
        producer=cf.createContext().createProducer();
    }
    
    
//Metodi chiamati dalla FE_Servlet_Logged --------------------------------------  
    
    @Override
    public int createSession(String sessionId){
        return state.createSession(sessionId);
    }
   
    
    @Override
    public Tag addOffer(int clientID,String carId){ 
        //Per dare un identificativo in manaiera veloce
        String uniqueID = UUID.randomUUID().toString();
        return writeRequest(clientID,new Offer(uniqueID,carId));
    }
 
    
    @Override
    public Tag getDB(int clientID){
        return readRequest(clientID);
    }
    
    
    @Override
    public Tag addReservation(int clientID,String offerId,String passenger) {
        return writeRequest(clientID,new Reservation(offerId,passenger));
    }
    

    @Override
    public String checkStatusData(Tag tag){
        return state.getTagStatus(tag);
    }
   
    
    @Override
    public Object getData(Tag tag) {
        //System.out.println("*********************************************************************\n"+state.print()+
        //        "\n*******************************************************************");
        return (!state.getTagStatus(tag).equals("PENDING")) ? state.getData(tag): null;
    }
//------------------------------------------------------------------------------        


    
    private Tag writeRequest(int clientID,Object o){
        //------------------ FASE 1 --------------------------------------------
        Tag tag=new Tag(ID,clientID,'W',state.messageCounter());   //TAG
        
        int sender=clientID;                               //SENDER
        int localTimeStamp=state.updateTs(clientID);       //LOCAL TIME STAMP
        
        state.setTagStatus(tag,"PENDING");
        state.addData(tag,null);
        
        RequestMessage message=new RequestMessage(o,tag,sender,localTimeStamp);
        message.setQueue(queue);
        
        if(o instanceof Offer)
            message.setType(TypeMessage.OFFER_REVISE_TS);
         else 
            message.setType(TypeMessage.RESERVATION_REVISE_TS);
        
        publishJMSMessage(message,timeToLive_ms);
        //----------------------------------------------------------------------
        
        
        //Avvio il timer entro il quale se non viene raggiunto il quorum  farà abortire il commit        
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(tag);
        timerConfig.setPersistent(false);
        timerService.createSingleActionTimer(abortTimeout,timerConfig);
        return tag;
    }
    
    private Tag readRequest(int clientID){
        
        Tag tag=new Tag(ID,clientID,'R',state.messageCounter());   //TAG
        
        state.addMaxTimeStamp(tag); //Associa un timeStamp al tag
        state.addData(tag,null);
        state.setTagStatus(tag,"PENDING");
        
        RequestMessage message=new RequestMessage(tag,clientID);
        message.setQueue(queue);
        message.setType(TypeMessage.READ_DB);
        
        publishJMSMessage(message,timeToLive_ms);
                
        //Avvio il timer entro il quale se non viene raggiunto il quorum  farà abortire il commit 
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(tag);
        timerConfig.setPersistent(false);
        timerService.createSingleActionTimer(abortTimeout,timerConfig);
        return tag;
    }
    

//Metodi chiamati dal COO_MDB per gestire i messaggi che arrivano dagli RM -----    
   
    @Override
    public void processWriteReply(ReplyMessage p){
        //Arrivo dei Proposed_TS
        
        int j   = p.getReceiver();
        int ts  = p.getTS();
        Tag tag = p.getTag();
        
        
        state.addMaxTimeStamp(tag,Math.max(state.getMaxTimeStamp(tag),ts));
        
        String tagStatus=state.getTagStatus(tag);
        
        int countProposed=state.addReplyMessage(tag);    

        if(countProposed==3 && tagStatus.equals("PENDING")){
            System.out.println("COO: Sono arrivati tutti i messaggi!");
            commitRequest(tag);         
        }
    }
    
    
    @Override
    public void processReadReply(ReplyMessage r) {

        int j   = r.getReceiver();
        int ts  = r.getTS();
        Tag tag = r.getTag();
            
        
        
        //E' il la prima risposta che arriva per quel tag
        if(state.getMaxTimeStamp(tag)==0){
            state.addData(tag,r.getMessage());
            state.addMaxTimeStamp(tag,ts);
        }
        else{
            int oldTS=state.getMaxTimeStamp(tag);
            int _new=r.getTS();
            if(oldTS<_new){
                state.addData(tag,r.getMessage());
                state.addMaxTimeStamp(tag,_new);
            }
        }
        
        int countReply=state.addReplyMessage(tag);
        
        if(countReply==3 && state.getTagStatus(tag).equals("PENDING")){
            
            //E' arrivato il tutto e metto il dato con il ts maggiore a disposizione del client
            System.out.println("COO: Sono arrivati 3 risposte alla lettura");
            
            state.setTagStatus(tag,"AVAILABLE");
        }
    }

//------------------------------------------------------------------------------    
    
    
    @Timeout
    public void checkingQuorum(Timer t){
       
        boolean quorum=false;        
        Tag tag=(Tag)t.getInfo();
        System.out.println("COO: Timeout for "+tag);
        
        String status=state.getTagStatus(tag);
        
        switch(tag.getTypeRequest()){
            case 'W':
                quorum=(state.countReplyMessage(tag) >= Qw);
                if(quorum && status.equals("PENDING")){
                    System.out.println("Q scrittura raggiunto:"+state.countReplyMessage(tag));
                    commitRequest(tag);
                }
                else if(!quorum && status.equals("PENDING")){
                    System.out.println("Q scrittura NON raggiunto:"+state.countReplyMessage(tag));
                    state.addData(tag,"Quorum scrittura non raggiunto!");
                    state.setTagStatus(tag,"ABORT");
                }
                break;
            case 'R':
                quorum=(state.countReplyMessage(tag) >= Qr);
                if(quorum && status.equals("PENDING")){
                    System.out.println("Q lettura raggiunto:"+state.countReplyMessage(tag));
                    state.setTagStatus(tag,"AVAILABLE");
                }
                else if(!quorum && status.equals("PENDING")){
                    System.out.println("Q lettura NON raggiunto:"+state.countReplyMessage(tag));
                    state.addData(tag,"Quorum lettura non raggiunto!");
                    state.setTagStatus(tag,"ABORT"); 
                }
                break;
        }
        t.cancel();
    }
    

    private void commitRequest(Tag tag){
                
        int clientID=tag.getClientID();
        int maxTimeStamp=state.getMaxTimeStamp(tag);
        int ts=state.getTs(clientID);
        
        RequestMessage message=new RequestMessage(clientID,tag,maxTimeStamp);
        message.setQueue(queue);
        message.setType(TypeMessage.FINAL_TS);
        
        state.updateTs(clientID,Math.max(ts,maxTimeStamp)); 
        
        publishJMSMessage(message,timeToLive_ms);

        state.addData(tag,"WRITE SUCCESSFULLY");
        state.setTagStatus(tag,"AVAILABLE");
    }
    

//AUX    
    private int calculateWriteQuorum(int N){        
        int Qw=0;
        while(!(Qw>=((N/2)+1)))Qw++;
        return Qw;
    }
    
    private int calculateReadQuorum(int N,int Qw){        
        int Qr=0;
        while(!(Qr>(N-Qw)))Qr++;
        return Qr;
    }
    
    private void publishJMSMessage(RequestMessage message,long timeToLive){
        producer.setTimeToLive(timeToLive);
        producer.send(topic,message);
    }
}

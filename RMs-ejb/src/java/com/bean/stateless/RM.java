package com.bean.stateless;

import com.*;
import com.bean.singleton.StateLocal;
import com.message.*;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.jms.ConnectionFactory;
import javax.jms.JMSProducer;
import javax.jms.Queue;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public abstract class RM implements RMLocal{

//Resorucers -------------------------------------------------------------------     
    @Resource(mappedName = "jms/__defaultConnectionFactory")
    private ConnectionFactory cf;
    
    //Si è deciso di instanziare 3 Code per i messaggi di ritorno ai coordinator
    //Lo scenario quindi prevede che sia possibile deployare fino a 3 Coordinator
    
    @Resource(mappedName = "jms/Queue1") 
    private Queue queue1;
    
    @Resource(mappedName = "jms/Queue2") 
    private Queue queue2;
        
    @Resource(mappedName = "jms/Queue3") 
    private Queue queue3;
    
    @Resource
    private TimerService timerService;
    
    @Resource(name="timeToLive_ms") 
    private int timeToLive_ms;      //Tempo di vita di ogni messaggio JMS
    
    
//Queste risorse vengono iniettate attraverso il deployment description in modo
//da  permettere  di deployare più componeneti al variariare di alcuni parametri.
//I valori messi nel codice sono quelli utilizzati di default. 
    @Resource(name = "timeoutLock")
    private int timeoutLock=10000;
    
    @Resource(name = "readRate")
    private int readRate=500;
    
    private JMSProducer producer;
//-----------------------------------------------------------------------------
    @PostConstruct
    public void init(){
        System.out.println("RM"+getID()+": Time to live: "+timeToLive_ms);        
        producer=cf.createContext().createProducer();
    }
    
//PUBBLICI ---------------------------------------------------------------------
    @Override
    public void processWriteRequest(RequestMessage m){
        System.out.println("RM"+getID()+":\n"+m);
        
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(m.getTag());
        timerConfig.setPersistent(false);
        
        System.out.println("RM"+getID()+" START Timer lock"+new Date());
        
        switch(m.getType()){
            
            case OFFER_REVISE_TS:
                getState().lock();
                processRevise(m); 
                timerService.createSingleActionTimer(timeoutLock,timerConfig);
                break;
                
            case RESERVATION_REVISE_TS:
                getState().lock();
                processRevise(m); 
                timerService.createSingleActionTimer(timeoutLock,timerConfig);
                break;
                
            case FINAL_TS:
                processCommit(m);
                getState().unlock();
        }
    }
    
    @Override
    public void processReadRequest(RequestMessage r){
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(r);
        timerConfig.setPersistent(false);
        timerService.createIntervalTimer(0,readRate,timerConfig);
    }
    
    /**
     * Prova a leggere sul DB con una certa periodicità nell'attesa che eventuali
     * lock di scrittura siano liberati. Nel caso in cui i lock non vengono 
     * rilasciti dopo un tempo prestabilito vengono rilasciti in automatico.
     * @param t timer che viene associato alla richiesta. 
     */
    @Timeout
    public void tryReadDB(Timer t){
        if(t.getInfo() instanceof RequestMessage){
            System.out.println("RM"+getID()+" TRY READ DB"+getID()+" Date"+new Date());

            RequestMessage r=(RequestMessage)t.getInfo();

            int j=r.getSender();

            if(!getState().isLock()){
                Tag tag=r.getTag();        
                String queue=r.getQueue();

                Object o=readDB();

                ReplyMessage message=new ReplyMessage(o,tag,getState().getPriority());
                message.setReceiver(j);
                message.setType(TypeMessage.READ_DB);

                sendResponse(message,queue);
                t.cancel();
            }
        }
        else if(t.getInfo() instanceof Tag){
            getState().unlock();
            System.out.println("RM"+getID()+" STOP Timer lock"+new Date());
            t.cancel();
        }
    }
//------------------------------------------------------------------------------   
    
    
//PRIVATI ----------------------------------------------------------------------    
    
    //Gestisce le REVISE_TS
    private void processRevise(RequestMessage r){
        
        int j = r.getSender();
        int ts = r.getTS();
        Tag tag = r.getTag();
          
        int priority=getState().getPriority();
        priority=Math.max(priority+1,ts);
        getState().setPriority(priority);
        
        Q_entry Q_e=new Q_entry(r.getMessage(),tag,j,priority,false);
        
        getState().getTemp_Q().add(Q_e);
        
        ReplyMessage message=new ReplyMessage(getID(),j,tag,priority);
        
        message.setType(TypeMessage.PROPOSED_TS);
        
        sendResponse(message,r.getQueue());      
    }
    
    //Gestisce le FINAL_TS
    public void processCommit(RequestMessage f){
                
        String j=f.getQueue();
        Tag tag = f.getTag();
        int ts = f.getTS();
        
        LinkedList<Q_entry> temp_Q=getState().getTemp_Q();
        LinkedList<Q_entry> delivery_Q=getState().getCommitQueue();
        
        Q_entry Q_e=getState().getEntry(tag);

        try{
            Q_e.setDeliverable(true);
            Q_e.setTemp_ts(ts);
            Collections.sort(temp_Q);
                
                 
            Q_entry head = temp_Q.element();
            if(head.getTag().equals(Q_e.getTag())){
                try{                                               
                    while(head.isDeliverable()){
                        head=temp_Q.removeFirst();
                        delivery_Q.add(head);
                        head = temp_Q.getFirst();
                    }
                }
                catch(NoSuchElementException e){
                }
            }
        }
        catch(NullPointerException e){e.printStackTrace();}
           
        Iterator it=delivery_Q.iterator();
        while(it.hasNext()){
            Q_entry e=(Q_entry)it.next();
            getState().setLocalTimestamp(Math.max(getState().getLocalTimestamp(),e.getTemp_ts()) + 1);

                
            //Write into DB
            Object o=e.getData();
            if(o instanceof Offer) writeDB((Offer)o);
            if(o instanceof Reservation) writeDB((Reservation)o);
            it.remove();                
        }
    }
    
    //Invia le ReplyMessage nelle code di ritorno opportune. Necessario nel caso
    //Siano stati deployati più Coordinator.
    private void sendResponse(ReplyMessage p,String queue){
        producer.setTimeToLive(timeToLive_ms);
        switch(queue){
            case "QUEUE1":producer.send(queue1,p);  break;
            case "QUEUE2":producer.send(queue2,p);  break;
            case "QUEUE3":producer.send(queue3,p);  break;
        }
    }
//------------------------------------------------------------------------------
    
    
//ASTRATTI ---------------------------------------------------------------------    
    /**
     * Scrive nel Database la proposta di carsharing fatta da un certo client
     * @param o proposta di carsharing
     */
    public abstract void writeDB(Offer o) ;
    
    /**
     * Prenota un certo posto in una proposta di carsharing esistente nel database
     * @param r rischiesta di prenotazione posto
     */
    public abstract void writeDB(Reservation r);
    
    /**
     * Richiesta di lettura dell'intero Database
     * @return 
     */
    public abstract Object readDB();
    
    /**
     * Restituisce l'identificativo del replication manager che è stato instanziato
     * @return identificativo del Replication manager
     */
    public abstract int getID();
    
    /**
     * Restituisce lo stato (memorizzato tramite singleton bean) del replication
     * manager che è stato istanziato.
     * @return stato del RM
     */
    public abstract StateLocal getState();
//------------------------------------------------------------------------------
}


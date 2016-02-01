package com.bean.singleton;

import com.Tag;
import java.util.TreeMap;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

/**
 * Questo bean tiene rappresenta lo stato del Coordinator , necessario per correlare
 * i messaggi che provengono dai cavari replication manager.
 * @author Russo , Didomenico e Delpopolo
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class State implements StateLocal {
    
    private long messageCounter;
    
    private TreeMap<String,Integer> sessionMap; //Mappa delle session e gli ID dei client
    
    //Utilie per associare un timestamp locale ad ogni client
    private TreeMap<Integer,Integer>localTimestampMap;  //Key = clientID && Value Localtimestamp
    
    //Tiene conto di massimo timestamp che è stato ricevuto per un determinata richiesta
    private TreeMap<Tag,Integer> maxTimestampMap; //Key = tag && Value = max TS
    
    //Tiene conto del numero di messaggi di risposta sono arrivato per un certo tag
    private TreeMap<Tag,Integer> numReplyMap; //Key = tag && Value = num ReplyMessage
    
    //Ogni richiesta associata ad un tag viene riservata un area di memoria dove verra messo 
    //il dato contenuto nella risposta.
    private TreeMap<Tag,Object>  dataMap;   //Key = tag && Value = data received
    
    //Non èssendo prondo subito il dato (causa messaggi asincroni del JMS) si è deciso di 
    //associare uno stato al dato risposta (utile per il pooling della servlet)
    private TreeMap<Tag,String>  statusMap; //Key = tag && Value = ABORT | PENDING | AVAIBLABLE

    
    public State() {
        maxTimestampMap = new TreeMap<>();
        numReplyMap     = new TreeMap<>();        
        statusMap       = new TreeMap<>();  
        dataMap         = new TreeMap<>();
        sessionMap      = new TreeMap<>();
        localTimestampMap=new TreeMap<>();
    }
        
    @Override
    @Lock(LockType.WRITE)
    public int createSession(String sessionId){
        if(!sessionMap.containsKey(sessionId)){
            int seq=sessionMap.size();
            sessionMap.put(sessionId,seq);
            localTimestampMap.put(seq,0);
            return seq;
        }
        else
            return sessionMap.get(sessionId);
    }
    
    
    @Override
    @Lock(LockType.WRITE)
    public long messageCounter(){
        return ++messageCounter;
    }
    
    
//LocalTimestamp method --------------------------------------------------------
    @Override
    @Lock(LockType.READ)
    public int getTs(int clientId) {
        return localTimestampMap.get(clientId);
    }
    @Override
    @Lock(LockType.WRITE)
    public int updateTs(int clientId){
        int ts=localTimestampMap.get(clientId);
        ts++;
        localTimestampMap.put(clientId,ts);
        return ts;
    }
    
    @Override
    @Lock(LockType.WRITE)
    public void updateTs(int clientId,int localTimestamp){
        localTimestampMap.put(clientId,localTimestamp);
    }   
//------------------------------------------------------------------------------
    
    
//maxTimestampMap method -------------------------------------------------------
    @Override
    @Lock(LockType.WRITE)
    public void addMaxTimeStamp(Tag t){
        maxTimestampMap.put(t,0);
    }
    @Override
    @Lock(LockType.WRITE)
    public void addMaxTimeStamp(Tag t,int ts){
        maxTimestampMap.put(t,ts);
    }
    @Override
    @Lock(LockType.READ)
    public int getMaxTimeStamp(Tag t){
        try{
            return maxTimestampMap.get(t);
        }
        catch(NullPointerException e){return 0;}
    }
//------------------------------------------------------------------------------    


//statusMap method -------------------------------------------------------------    
    @Override
    @Lock(LockType.WRITE)
    public void setTagStatus(Tag t,String status){
        statusMap.put(t,status);
    }
    @Override
    @Lock(LockType.READ)
    public String getTagStatus(Tag t){
        return statusMap.get(t);
    }
    @Override
    @Lock(LockType.WRITE)
    public void addData(Tag t,Object o){
        dataMap.put(t, o);
    }
    @Override
    @Lock(LockType.READ)
    public Object getData(Tag t){
        return dataMap.get(t);
    }
//------------------------------------------------------------------------------
    
    
//numReplyMap method -----------------------------------------------------------
    @Override
    @Lock(LockType.WRITE)
    public int addReplyMessage(Tag t){
         try{
            int count = (numReplyMap.containsKey(t)) ? numReplyMap.get(t) : 0;
            count ++;
            numReplyMap.put(t,count);
            return count;
        }
        catch(NullPointerException e){return 0;}
    }    
    @Override
    @Lock(LockType.READ)
    public int countReplyMessage(Tag t){
        try{
            return numReplyMap.get(t);
        }
        catch(NullPointerException e){ return 0;}
    }
    
    
//------------------------------------------------------------------------------

    @Override
    public String print() {
        return "\n\n\n\nState\n{\n" + "\nmessageCounter=" + messageCounter  + ",\nsessionMap=" + sessionMap + ",\nlocalTimestampMap=" +localTimestampMap + ",\nmaxTimestampMap=" + maxTimestampMap + ",\nnumReplyMap=" + numReplyMap + ",\nstatusMap=" + statusMap + ",\ndataMap=" + dataMap + '}';
    }
}

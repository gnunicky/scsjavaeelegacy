package com;

import com.message.HeartBeat;
import java.util.HashMap;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class FaultDetector {
        
    final private HashMap<String,String> processStatusMap;
    
    final private HashMap<HeartBeat,Integer> heartBeatMap;

    final private  HashMap<String,Integer> timeoutMap;
    
    public FaultDetector() {
        processStatusMap=new HashMap<>();
        heartBeatMap=new HashMap<>();
        timeoutMap=new HashMap<>();
    }
    
    synchronized public void putProcessStatus(String processName,String status){
        processStatusMap.put(processName,status);
    }
    
    synchronized public int putHeartBeat(HeartBeat hb){
         try{
            int count = (heartBeatMap.containsKey(hb)) ? heartBeatMap.get(hb) : 0;
            count ++;
            heartBeatMap.put(hb,count);
            return count;
        }
        catch(NullPointerException e){return 0;}
    }

    synchronized public void resetHeartBeartCount(HeartBeat hb){
        heartBeatMap.put(hb,0);
    }
    
    synchronized public boolean isPresent(String processName){
        return processStatusMap.containsKey(processName);
    }
    
    synchronized public void setTimeout(String hb,int timeout){
        timeoutMap.put(hb,timeout);
    }

    synchronized public int getHeartbeatCounter(HeartBeat hb) {
        return heartBeatMap.get(hb);
    }
    
    synchronized public boolean wasSuspected(String processName) {
        return getProcessStatusMap().get(processName).equalsIgnoreCase("SUSPECTED");
    }

    public HashMap<String, String> getProcessStatusMap() {
        return processStatusMap;
    }

    public HashMap<HeartBeat, Integer> getHeartBeatMap() {
        return heartBeatMap;
    }

    public HashMap<String, Integer> getTimeoutMap() {
        return timeoutMap;
    }
    
    
}

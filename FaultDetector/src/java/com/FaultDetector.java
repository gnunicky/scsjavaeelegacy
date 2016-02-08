package com;

import com.message.HeartBeat;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class FaultDetector {
        
    final private HashMap<String,String> processStatusMap;
    
    final private HashMap<HeartBeat,Integer> heartBeatMap;

    final private  HashMap<String,Integer> timeoutMap;
    
    private DatagramSocket socket;
    
    private Thread listenerThread;
    
    private int port;
    
    private String log;
    
    private boolean start;
    
    public FaultDetector() {
        log="";
        start=false;
        processStatusMap=new HashMap<>();
        heartBeatMap=new HashMap<>();
        timeoutMap=new HashMap<>();
        log("Fault Detector is created!");
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
    
    public void start(int port){
        this.port=port;
        start=true;
        listenerThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Fault Detector running...");
                    log("Fault Detector running...");
                    startup();
                }
            });
        listenerThread.start();     
    }
    
    public void stop(){
        if(!listenerThread.isInterrupted()){
            start=false;
            socket.close();            
            listenerThread.interrupt();
            System.out.println("...Fault Detector stopped!");
            log=log+new Date()+"...Fault Detector stopped!";
        }
    }
     
    public String getLog(){return log;}
    
    public void log(String msg){
        log=log.concat("\n"+new Date()+" : "+msg);
    }
    
    private void startup(){
        try {
            
            socket = new DatagramSocket(port);
            
            byte[] incomingData = new byte[1024];
            
            while(start){
                DatagramPacket incomingPacket = new DatagramPacket(incomingData,incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    HeartBeat heartbeat=(HeartBeat) is.readObject();
                    log("Received: "+heartbeat);
                    (new Thread(new HeartBeatHandler(heartbeat,this))).start();
                } catch (ClassNotFoundException ex) {
                    System.out.println("Non ho ricevuto un oggetto di tipo Heartbeat");
                }
            }
            
        } catch (SocketException ex) {
            Logger.getLogger(FaultDetector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FaultDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void finalize(){
        System.out.println("Fault Detector Object removed!");
        try {
            super.finalize();
            socket.close();
        } catch (Throwable ex) {
            Logger.getLogger(FaultDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

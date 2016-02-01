package com;

import com.message.HeartBeat;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class HeartBeatHandler implements Runnable{

    private FaultDetector fd;
    private ObjectInputStream in;
    
    public HeartBeatHandler(Socket client, FaultDetector fd) {
        try {
            this.fd=fd;
            in=new ObjectInputStream(client.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(HeartBeatHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            
            Object data=in.readObject();
            
            if(data instanceof HeartBeat){

                HeartBeat heartbeat=(HeartBeat)data;
                //System.out.println("Arrivato Heartbeat: "+heartbeat);
                
                fd.putHeartBeat(heartbeat);
                
                String processName=heartbeat.getProcessName();
                
                if(!fd.isPresent(processName) || fd.wasSuspected(processName)){
                    
                    int checkFaultRate_ms = 3 * heartbeat.getNextTimeout();
                    
                    fd.putProcessStatus(processName,"ALIVE");
                    
                    fd.setTimeout(processName,checkFaultRate_ms);
                    
                    //Start timer
                    FaultHandler timerTask = new FaultHandler(fd,heartbeat,checkFaultRate_ms);
                    Timer timer = new Timer(true);
                    timer.schedule(timerTask,checkFaultRate_ms);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(HeartBeatHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HeartBeatHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}

package com;

import com.message.HeartBeat;
import java.util.Timer;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class HeartBeatHandler implements Runnable{
    
    private FaultDetector fd;
    
    private HeartBeat heartbeat;
    
    public HeartBeatHandler(HeartBeat heartbeat, FaultDetector fd) {
        this.fd=fd;
        this.heartbeat=heartbeat;
    }

    @Override
    public void run() {
        
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
    
    
}

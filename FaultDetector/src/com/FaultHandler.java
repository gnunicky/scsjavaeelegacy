package com;

import com.message.HeartBeat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class FaultHandler extends TimerTask{

    private FaultDetector fd;
    private HeartBeat hb;
    private int timeout;
    
    public FaultHandler(FaultDetector fd,HeartBeat hb,int timeout) {
        this.fd = fd;
        this.hb=hb;
        this.timeout=timeout;
    }

    @Override
    public void run() {
        System.out.println("Time out for the heartbeat: "+hb);
        
        int numHeartBeat=fd.getHeartbeatCounter(hb);
        
        String processName=hb.getProcessName();
        
        if(numHeartBeat==0){
            fd.putProcessStatus(processName,"SUSPECTED");
            System.out.println("Process "+processName+" is suspected!");
        }
        else{      
            System.out.println("Num Heart beat arrived into round: "+numHeartBeat);
            fd.resetHeartBeartCount(hb);  
            restartTimeout();
        }
        this.cancel();
    }
    
    private void restartTimeout(){
        FaultHandler timerTask = new FaultHandler(fd,hb,timeout);
        Timer timer = new Timer(true);
        timer.schedule(timerTask,timeout);
    }
    
}

package com.fault;

import com.message.HeartBeat;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Startup
@Singleton(mappedName = "FaultDetectorClient")
public class FaultDetectorClient{
    
    //Risorse iniettate da ejb-jar.xml. Se il file manca verranno presi i valori
    //di default assegnati qui nel codice
    
    @Resource(name = "heartbeatRate")
    private int heartbeatRate=5000;
    
    @Resource(name = "address")
    private String address="localhost";
    
    @Resource(name="port")
    private int port=9999;
    
    @Resource(name="processName")
    private String processName="GENERIC PROCESS";
    
    @Resource
    private TimerService timerService;
    
    private long seq;
    
    @PostConstruct
    public void init(){
        String msg=
                "**** FAULT DETECTION CLIENT ***\n"+
                "Heart beat rate: "+heartbeatRate+"\n"+
                "Fault detector address: "+address+"\n"+
                "Fault detector port: "+port+"\n"+
                "Process name monitored: "+processName+"\n\n";
        
        System.out.println(msg);
        seq=0;
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo("Heartbeat timer");
        timerConfig.setPersistent(false);
        timerService.createIntervalTimer(5000,heartbeatRate,timerConfig);
    }
    
        
    @Timeout
    public void sendHeartBeat(Timer t){
        HeartBeat hb=new HeartBeat(processName,heartbeatRate,++seq);
        //System.out.println("COO SEND: "+hb);
        ConnectionUDP connection=new ConnectionUDP(address,port);
        try{
            connection.send(hb);
        }
        catch(Exception e){
            System.out.println("Fault detectior offline!");
        } 
    }
}

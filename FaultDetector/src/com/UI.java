package com;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class UI {
    
    public static void main(String s[]){
        try {
            //Questi dati andrebbero caricati da file di configurazione...
            int port=9999;
            //------------------------------------------------------------
            
            System.out.println("Fault Detector running...");
            FaultDetector fd=new FaultDetector();
            
            ServerSocket server=new ServerSocket(port);
            
            while(true){                
                Socket client=server.accept();
                (new Thread(new HeartBeatHandler(client,fd))).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }
}

package com.fault;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class ConnectionTCP{
    
    private Socket client;
    private ObjectOutputStream out;
    
    public ConnectionTCP(String ipAddress,int port){
        try {
            client=new Socket(ipAddress,port);
            out=new ObjectOutputStream(client.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ConnectionTCP.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    public void sendHeartBeat(Object o) throws Exception{
        out.writeObject(o);
        out.flush();
    }
}

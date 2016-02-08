package com.fault;

import com.message.HeartBeat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class ConnectionUDP {
    
    private String address;
    private int port;

    public ConnectionUDP(String address, int port) {
        this.address = address;
        this.port = port;
    }
    
    
    
    public boolean send(HeartBeat hb){       
        try {	
            DatagramSocket Socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(address);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(hb);
            byte[] data = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
            Socket.send(sendPacket);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConnectionUDP.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (SocketException ex) {
            Logger.getLogger(ConnectionUDP.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUDP.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    	
}

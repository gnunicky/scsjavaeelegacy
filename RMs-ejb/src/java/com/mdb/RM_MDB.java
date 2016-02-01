package com.mdb;

import com.message.RequestMessage;
import java.io.Serializable;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public abstract class RM_MDB {
    
    public void processMessage(Message message) throws JMSException{
        System.out.println("Thread: "+Thread.currentThread().getName());
        if(message instanceof ObjectMessage){

            ObjectMessage om=(ObjectMessage)message;
            
            Serializable o=om.getObject();
            
            if(o instanceof RequestMessage){
                RequestMessage m=(RequestMessage)o;
                switch(m.getTypeAccess()){
                    case "WRITE": processWriteRequest(m); break;
                    case "READ":  processReadRequest(m);
                }
            }
        }
    }
    
    public abstract void processReadRequest(RequestMessage r);
    
    public abstract void processWriteRequest(RequestMessage r);
    
 
    
}

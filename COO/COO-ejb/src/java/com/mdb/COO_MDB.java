package com.mdb;

import com.bean.stateless.COOLocal;
import com.message.ReplyMessage;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/Queue1"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class COO_MDB implements MessageListener {
    
    @EJB(mappedName ="COOBean")
    private COOLocal COO;
    
    @Override
    public void onMessage(Message message) {
            
        if(message instanceof ObjectMessage){
            ObjectMessage om=(ObjectMessage)message;
            try {
                Serializable o=om.getObject();
                if(o instanceof ReplyMessage){
                    ReplyMessage r=(ReplyMessage)o;
                    System.out.println("COO_MDB: \n"+r);
                    switch(r.getTypeAccess()){
                        case "WRITE":                            
                            COO.processWriteReply(r);
                            break;
                        case "READ":
                            COO.processReadReply(r);
                            break;
                    }
                }
            } 
            catch (JMSException ex) {
                Logger.getLogger(COO_MDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

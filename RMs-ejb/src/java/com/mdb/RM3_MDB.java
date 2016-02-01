package com.mdb;

import com.bean.stateless.RMLocal;
import com.message.RequestMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@MessageDriven(mappedName="jms/Topic",activationConfig={
    @ActivationConfigProperty(propertyName  = "acknowledgeMode",
                              propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName  = "destinationType",
                              propertyValue = "javax.jms.Topic")})
public class RM3_MDB extends RM_MDB implements MessageListener{
    
    @EJB(beanName = "RM3")
    RMLocal RM3;
    
    @Override
    public void onMessage(Message message) {
     
        try {
            processMessage(message);
        } catch (JMSException ex) {
            Logger.getLogger(RM1_MDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void processWriteRequest(RequestMessage r) {
        RM3.processWriteRequest(r);
    }


    
    @Override
    public void processReadRequest(RequestMessage r) {
        RM3.processReadRequest(r);
    }


}


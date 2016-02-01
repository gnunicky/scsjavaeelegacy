package com.bean.stateless;

import com.message.RequestMessage;
import javax.ejb.Local;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Local
public interface RMLocal{
    
    /**
     * Effettua il processing dei messaggi in arrivo (RequestMessage) dal 
     * Coordinator relativi alle rischieste di scrittura sul DB
     * @param r messaggio in arrivo dai Replication manager
     */
    public void processWriteRequest(RequestMessage r);
    
    /**
     * Effettua il processing dei messaggi in arrivo (RequestMessage) dal 
     * Coordinator relativi alle rischieste di lettura sul DB
     * @param r messaggio in arrivo dai Replication manager
     */
    public void processReadRequest(RequestMessage r);

}

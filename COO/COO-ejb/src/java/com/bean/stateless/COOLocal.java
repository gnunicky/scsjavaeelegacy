package com.bean.stateless;

import com.Tag;
import com.message.ReplyMessage;
import javax.ejb.Local;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Local
public interface COOLocal{

    
    //-- Main method --
    /**
     * Mette a disposiszione una certa automobile per il servizio di carsharing
     * @param clientID id del client che sta facendo la richista
     * @param carID ID dell'auto (es. targa) che si sta mettendo a dispozione
     * @return ritorna un oggetto che identifica univocamente la richiesta
     */
    public Tag addOffer(int clientID,String carID);
    
    /**
     * Invia una richiesta di lettura dell'intero database.
     * @param clientID id del client che sta facendo la richista 
     * @return ritorna un oggetto che identifica univocamente la richiesta
     */
    public Tag getDB(int clientID);
    
    
    /**
     * Avvia la richiesta di prenotazione di un posto di una auto che viene messa
     * a disposizione in una certa proposta.
     * @param clientID id del client che sta facendo la richista
     * @param offerID id della proposta di carsharing
     * @param passenger identificativo del passeggero
     * @return ritorna un oggetto che identifica univocamente la richiesta
     */
    public Tag addReservation(int clientID,String offerID,String passenger);
    //-----------------
    
    
    /**
     * Effettua il processing dei messaggi in arrivo (ReplyMessage) dai 
     * replication manager relativi alle rischiesta di scrittura sul DB
     * @param r messaggio in arrivo dai Replication manager
     */
    public void processWriteReply(ReplyMessage r);
    
    
     /**
     * Effettua il processing dei messaggi in arrivo (ReplyMessage) dai 
     * replication manager relativi alle rischiesta di lettura sul DB
     * @param r messaggio in arrivo dai Replication manager
     */
    public void processReadReply(ReplyMessage r);
    
    /**
     * Assengna un identificativo all'al client in base alla sessione creata con
     * l'utente nella servlet.
     * @param sessionId identificativo di sessione
     * @return identificativo del client all'interno del coordinator.
     */
    public int createSession(String sessionId);
    
    /**
     * Restituisce il dato associato ad una certa assiato ad una certa richiesta
     * @param tag identificativo della richiesta
     * @return dato generico
     */
    public Object getData(Tag tag);

    /**
     * Controlla se il dato associato a una certa richiesta è pronto
     * @param tag identificativo della richiesta
     * @return stato: AVAILAVLE| ABORT | PENDING
     */
    public String checkStatusData(Tag tag);
}

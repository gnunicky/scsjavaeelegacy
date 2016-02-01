package com.message;

import com.Tag;
import java.io.Serializable;

/**
 * L'oggetto rappresenta un generica richiesta di scrittura o di lettura che 
 * viene mandato dal Coordinator ai replication manager.
 * @author Russo , Didomenico e Delpopolo
 */
public class RequestMessage implements Serializable{
    
    private TypeMessage type;
    
    private Tag tag;
    
    private int sender;
    
    private String queue;
    
    private Object message;
    
    private int TS;

    //REVISE_TS
    public RequestMessage(Object o, Tag tag, int i, int ts) {
        this.message=o;
        this.tag=tag;
        this.sender=i;
        this.TS=ts;
    }

    //FINAL_TS
    public RequestMessage(int i, Tag tag, int ts) {
        this.sender = i;
        this.tag = tag;
        this.TS = ts;
    }

    //READ_DB
    public RequestMessage(Tag tag,int sender){
        this.tag=tag;
        this.sender=sender;
    }
    
    public TypeMessage getType() {
        return type;
    }

    public void setType(TypeMessage type) {
        this.type = type;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getTS() {
        return TS;
    }

    public void setTS(int TS) {
        this.TS = TS;
    }

    public String getTypeAccess() {
        return (tag.getTypeRequest()=='W') ? "WRITE":"READ";
    }

    public void setTypeAccess(String typeAccess) {
        
    }

    @Override
    public String toString() {
        String temp="REQUEST MESSAGE: "+type+"\n"+
                    "{\n"+
                    "\t- Tag: "+tag+"\n"+
                    "\t- Timestamp: "+TS+"\n"+
                    "\t- Obejct:\n"+message+"\n}\n";                
        switch(type){
            case OFFER_REVISE_TS: 
                return temp+
                        "\n}";
            case RESERVATION_REVISE_TS:
                return temp+
                        "\n}";
            case FINAL_TS:
                return temp+
                        "\n}";
            case READ_DB:
                return temp+
                        "\n}";
            default:
                return "-- Not recognized --";
        }
    }
    
    
    
}

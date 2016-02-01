package com.message;

import com.Tag;
import java.io.Serializable;

/**
 *
 * @author leandro
 */
public class ReplyMessage implements Serializable,Comparable<ReplyMessage>{
    
    private TypeMessage type;
    
    private Tag     tag;
    
    private int     receiver;          //process_id receviver
    
    private int     sender;            //process_id sender
    
    private int     TS;
    
    private Object message;

    //PROPOSED_TS
    public ReplyMessage(int i, int j,Tag tag, int ts) {
        this.sender = i;
        this.receiver = j;
        this.tag = tag;
        this.TS = ts;
    }

    //READ_DB
    public ReplyMessage(Object o,Tag tag,int ts) {
        this.message=o;
        this.tag=tag;
        this.TS=ts;
    }
    
    public TypeMessage getType() {
        return type;
    }

    public void setType(TypeMessage type) {
        this.type = type;
    }

    public String getTypeAccess() {
        return (tag.getTypeRequest()=='W') ? "WRITE":"READ";
    }


    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getTS() {
        return TS;
    }

    public void setTS(int TS) {
        this.TS = TS;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
    
    @Override
    public int compareTo(ReplyMessage o) {
        if(tag.equals(o.getTag())) return 0;
        else return 1;
    }
    
    @Override
    public String toString() {
        String temp="REPLY MESSAGE: "+type+"\n"+
                    "{\n"+
                    "\t- Tag: "+tag+"\n"+
                    "\t- Timestamp: "+TS+"\n"+
                    "\t- Object:\n"+message+"\n}\n";
        switch(type){
            case PROPOSED_TS: 
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

package com;

import java.io.Serializable;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class Tag implements Serializable,Comparable<Tag>{
    
    private int   ID;               //ID del coodinator   
    private int   clientID;         //ID del client
    private char  typeRequest;      // Carattere: 'W' | 'R'
    private long  messageCounter;   //Conta quanti messaggi vengono inviati dal coordinator ID

    public Tag(int beanID, int clientID, char typeRequest, long messageCounter) {
        this.ID = beanID;
        this.clientID = clientID;
        this.typeRequest = typeRequest;
        this.messageCounter = messageCounter;
    }

    public int getID() {
        return ID;
    }

    public int getClientID() {
        return clientID;
    }

    public char getTypeRequest() {
        return typeRequest;
    }

    public long getMessageCounter() {
        return messageCounter;
    }
    
    @Override
    public int compareTo(Tag o) {
        if(this.toString().equalsIgnoreCase(o.toString())) return 0;
        else return 1;
    }
    
    @Override
    public String toString() {
        return "{"+ID+"|"+clientID+"|"+(typeRequest+"")+"|"+messageCounter+"}";
    }
    
    
}

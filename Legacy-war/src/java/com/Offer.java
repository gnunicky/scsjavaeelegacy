package com;

import java.io.Serializable;
/**
 * 
 * @author Russo , Didomenico e Delpopolo
 */
public class Offer implements Serializable{

    private String offerID;
    private String carID;
    private String passenger1;
    private String passenger2;
    private String passenger3;
    private String passenger4;
    private int    busySeat;
    private int    timestamp;

    
    public Offer(String car_id) {
        this.carID = car_id;
        passenger1="";
        passenger2="";
        passenger3="";
        passenger4="";
        busySeat=0;
    }

    
    public Offer(String offer_id, String carID) {
        this(carID);
        this.offerID = offer_id;
    }
    
    public Offer(String offer_id, String carID,String p1,String p2,String p3,String p4){
        this(offer_id,carID);
        this.passenger1=p1;
        this.passenger2=p2;
        this.passenger3=p3;
        this.passenger4=p4;
        if(!p1.isEmpty()) busySeat++;
        if(!p2.isEmpty()) busySeat++;
        if(!p3.isEmpty()) busySeat++;
        if(!p4.isEmpty()) busySeat++;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getPassenger1() {
        return passenger1;
    }

    public void setPassenger1(String passenger1) {
        this.passenger1 = passenger1;
    }

    public String getPassenger2() {
        return passenger2;
    }

    public void setPassenger2(String passenger2) {
        this.passenger2 = passenger2;
    }

    public String getPassenger3() {
        return passenger3;
    }

    public void setPassenger3(String passenger3) {
        this.passenger3 = passenger3;
    }

    public String getPassenger4() {
        return passenger4;
    }

    public void setPassenger4(String passenger4) {
        this.passenger4 = passenger4;
    }

    public int getBusySeat() {
        return busySeat;
    }

    public void setBusySeat(int busySeat) {
        this.busySeat = busySeat;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
    
    
    public boolean addPassenger(String id){
        if(busySeat==0){
            return false;
        }
        else{
            if(passenger1.isEmpty()) passenger1=id;
            else if(passenger2.isEmpty()) passenger2=id;
            else if(passenger3.isEmpty()) passenger3=id;
            else if(passenger4.isEmpty()) passenger4=id;
            busySeat++;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Offer{" + "offerID=" + offerID + ", carID=" + carID + ", passenger1=" + passenger1 + ", passenger2=" + passenger2 + ", passenger3=" + passenger3 + ", passenger4=" + passenger4 + ", busySeat=" + busySeat + ", timestamp=" + timestamp + '}';
    }

    
}

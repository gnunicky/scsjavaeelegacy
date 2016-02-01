package com;

import java.io.Serializable;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class Reservation implements Serializable{
    
    private String offerID;
    private String passengerID;

    public Reservation(String offerID, String passengerID) {
        this.offerID = offerID;
        this.passengerID = passengerID;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    @Override
    public String toString() {
        return "Reservetion{" + "offerID=" + offerID + ", passengerID=" + passengerID + '}';
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author leandro
 */
@Entity
@Table(name = "Offers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Offers.findAll", query = "SELECT o FROM Offers o"),
    @NamedQuery(name = "Offers.findByOfferID", query = "SELECT o FROM Offers o WHERE o.offerID = :offerID"),
    @NamedQuery(name = "Offers.findByCarID", query = "SELECT o FROM Offers o WHERE o.carID = :carID"),
    @NamedQuery(name = "Offers.findByPassenger1", query = "SELECT o FROM Offers o WHERE o.passenger1 = :passenger1"),
    @NamedQuery(name = "Offers.findByPassenger2", query = "SELECT o FROM Offers o WHERE o.passenger2 = :passenger2"),
    @NamedQuery(name = "Offers.findByPassenger3", query = "SELECT o FROM Offers o WHERE o.passenger3 = :passenger3"),
    @NamedQuery(name = "Offers.findByPassenger4", query = "SELECT o FROM Offers o WHERE o.passenger4 = :passenger4"),
    @NamedQuery(name = "Offers.findByBusySeat", query = "SELECT o FROM Offers o WHERE o.busySeat = :busySeat"),
    @NamedQuery(name = "Offers.findByTimestamp", query = "SELECT o FROM Offers o WHERE o.timestamp = :timestamp")})
public class Offers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "offerID")
    private String offerID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "carID")
    private String carID;
    @Size(max = 50)
    @Column(name = "passenger1")
    private String passenger1;
    @Size(max = 50)
    @Column(name = "passenger2")
    private String passenger2;
    @Size(max = 50)
    @Column(name = "passenger3")
    private String passenger3;
    @Size(max = 50)
    @Column(name = "passenger4")
    private String passenger4;
    @Basic(optional = false)
    @NotNull
    @Column(name = "busySeat")
    private int busySeat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timestamp")
    private int timestamp;

    public Offers() {
    }

    public Offers(String offerID) {
        this.offerID = offerID;
    }

    public Offers(String offerID, String carID, int busySeat, int timestamp) {
        this.offerID = offerID;
        this.carID = carID;
        this.busySeat = busySeat;
        this.timestamp = timestamp;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (offerID != null ? offerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Offers)) {
            return false;
        }
        Offers other = (Offers) object;
        if ((this.offerID == null && other.offerID != null) || (this.offerID != null && !this.offerID.equals(other.offerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entity.Offers[ offerID=" + offerID + " ]";
    }
    
}

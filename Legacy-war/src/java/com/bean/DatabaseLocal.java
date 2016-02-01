package com.bean;

import com.Offer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Local
public interface DatabaseLocal {

    public List<Offer> getDB();

    public boolean addReservation(String offerID, String passengerID);

    public boolean addOffer(String offerID, String carID);
    
}

package com.webservice;

import com.Offer;
import com.bean.DatabaseLocal;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@WebService(serviceName = "FE_Database")
public class FE_Database {

    @EJB(name = "Database")
    private DatabaseLocal database;

    
    
    /**
     * Web service operation
     * @return 
     */
    @WebMethod(operationName = "getDB")
    public Offer[] getDB() {
        List<Offer> list=database.getDB();
        Offer[] vector=new Offer[list.size()];       
        return list.toArray(vector);
    }

    /**
     * Web service operation
     * @param offerID
     * @param passengerID
     * @return 
     */
    @WebMethod(operationName = "addReservation")
    public boolean addReservation(@WebParam(name = "offerID") String offerID, @WebParam(name = "passengerID") String passengerID) {
        database.addReservation(offerID, passengerID);
        return true;
    }

    /**
     * Web service operation
     * @param offerID
     * @param carID
     * @return 
     */
    @WebMethod(operationName = "addOffer")
    public boolean addOffer(@WebParam(name = "offerID") String offerID, @WebParam(name = "carID") String carID) {
       database.addOffer(offerID, carID);
       return true;
    }
}

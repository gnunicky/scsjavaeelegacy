package com.bean.stateless;

import com.Offer;
import com.Reservation;
import com.bean.singleton.StateLocal;
import com.ws.FEDatabase_Service;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Stateless(mappedName = "RM1Bean")
public class RM1 extends RM implements RMLocal{

    @WebServiceRef(wsdlLocation = "META-INF/wsdl/leandro1.homeunix.org_8080/Legacy-war/FE_Database.wsdl")
    private FEDatabase_Service service;

    @EJB(beanName = "State1")
    private StateLocal state;

    @Resource(name = "ID")
    private int ID;

    
    @Override
    public void writeDB(Offer o){        
        System.out.println("RM"+getID()+" I've write DB"+getID());        
        
        //Chiamata al WS
        addOffer(o.getOfferID(),o.getCarID());
    }
    
    @Override
    public void writeDB(Reservation r){
        System.out.println("RM"+getID()+" I've write DB"+getID());
        
        //Chiamata al WS
        addReservation(r.getOfferID(),r.getPassengerID());
    }

    @Override
    public Object readDB() {
        System.out.println("RM"+getID()+" I've read DB"+getID());
        List<Offer> list=new LinkedList<>();
        
        //Chiamata al WS
        List<com.ws.Offer> list_ws=getDB();
        
        for(com.ws.Offer ows:list_ws){
            String offerID=ows.getOfferID();
            String carID=ows.getCarID();
            String p1=ows.getPassenger1();
            String p2=ows.getPassenger2();
            String p3=ows.getPassenger3();
            String p4=ows.getPassenger4();
            list.add(new Offer(offerID,carID,p1, p2, p3, p4));
        }
        return list;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public StateLocal getState() {
        return state;
    }



    
//Web Service client call ------------------------------------------------------
    private java.util.List<com.ws.Offer> getDB() {
        com.ws.FEDatabase port = service.getFEDatabasePort();
        return port.getDB();
    }

    private boolean addOffer(java.lang.String offerID, java.lang.String carID) {
        com.ws.FEDatabase port = service.getFEDatabasePort();
        return port.addOffer(offerID, carID);
    }

    private boolean addReservation(java.lang.String offerID, java.lang.String passengerID) {
        com.ws.FEDatabase port = service.getFEDatabasePort();
        return port.addReservation(offerID, passengerID);
    }
//------------------------------------------------------------------------------    
}

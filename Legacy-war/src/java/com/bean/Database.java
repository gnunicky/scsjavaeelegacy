package com.bean;

import com.Offer;
import com.entity.Offers;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Stateless
public class Database implements DatabaseLocal {

    @PersistenceContext(unitName = "Legacy-warPU")
    private EntityManager em;


    @Override
    public List<Offer> getDB(){
        List<Offers> list=em.createQuery("SELECT e FROM Offers e").getResultList();
        List<Offer> offerList=new LinkedList<>();
        for(Offers offerDB:list){
            String offerID=offerDB.getOfferID();
            String carID=offerDB.getCarID();
            String p1=(offerDB.getPassenger1() == null) ? "" :offerDB.getPassenger1();
            String p2=(offerDB.getPassenger2() == null) ? "" :offerDB.getPassenger2();
            String p3=(offerDB.getPassenger3() == null) ? "" :offerDB.getPassenger3();
            String p4=(offerDB.getPassenger4() == null) ? "" :offerDB.getPassenger4();
            offerList.add(new Offer(offerID,carID, p1, p2, p3, p4));
        }
        return offerList;
    }


    @Override
    public boolean addReservation(String offerID,String passengerID) {
        Offers offer=em.find(Offers.class,offerID);
        int busy=offer.getBusySeat();
        switch(busy){
            case 0:offer.setPassenger1(passengerID);break;
            case 1:offer.setPassenger2(passengerID);break;
            case 2:offer.setPassenger3(passengerID);break;
            case 3:offer.setPassenger4(passengerID);break;
            case 4:return false; 
        }
        offer.setBusySeat(++busy);
        em.persist(offer);
        return true;
    }


    @Override
    public boolean addOffer(String offerID,String carID) {
        if(em.find(Offers.class,offerID)!=null) return false;
        Offers offer=new Offers();
        offer.setOfferID(offerID);
        offer.setCarID(carID); 
        em.persist(offer);
        return true;
    }
}

package com.bean.singleton;

import javax.ejb.Singleton;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Singleton(mappedName = "StateBean1")
public class State1 extends State implements StateLocal {

    public State1() {
        System.out.println("Stato 1 creato");
    }
    
    
}

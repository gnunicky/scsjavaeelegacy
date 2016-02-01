package com.bean.singleton;

import javax.ejb.Singleton;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Singleton(mappedName = "StateBean2")
public class State2 extends State implements StateLocal {
    
    public State2() {
        System.out.println("Stato 2 creato");
    }

}

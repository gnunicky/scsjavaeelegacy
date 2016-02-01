package com.bean.singleton;

import javax.ejb.Singleton;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Singleton(mappedName = "StateBean3")
public class State3 extends State implements StateLocal {
    
    public State3() {
        System.out.println("Stato 3 creato");
    }

}

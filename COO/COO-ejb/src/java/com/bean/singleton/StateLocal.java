/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean.singleton;

import com.Tag;
import javax.ejb.Local;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Local
public interface StateLocal {
    public int createSession(String sessionId);
    
    public long messageCounter();
    public int getTs(int clientId);
    public int updateTs(int clientId);
    public void updateTs(int clientId,int localTimestamp);
    public void addMaxTimeStamp(Tag t);
    public void addMaxTimeStamp(Tag t,int ts);    
    public int getMaxTimeStamp(Tag t);
    public void setTagStatus(Tag t,String status);
    public String getTagStatus(Tag t);
    public void addData(Tag t,Object o);   
    public Object getData(Tag t);
    public int addReplyMessage(Tag t);
    public int countReplyMessage(Tag t);
    
    public String print();
}

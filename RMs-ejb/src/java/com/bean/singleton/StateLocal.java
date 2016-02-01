package com.bean.singleton;

import com.Q_entry;
import com.Tag;
import java.util.LinkedList;
import javax.ejb.Local;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
@Local
public interface StateLocal {
    
    public int getPriority();
    
    public void setPriority(int priority);
    
    public int lock();
    
    public int unlock();
    
    public boolean isLock();
    
    public int getLocalTimestamp();
    
    public void setLocalTimestamp(int ts);
    
    
    public void addTemp_Q(Q_entry e);
   
    public LinkedList<Q_entry> getTemp_Q();
    
    public Q_entry getEntry(Tag tag);
     
    public LinkedList<Q_entry>  getCommitQueue(); 
}

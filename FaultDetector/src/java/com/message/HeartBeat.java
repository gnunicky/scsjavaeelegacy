package com.message;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class HeartBeat implements Serializable,Comparable<HeartBeat>{
    
    private String processName;
    
    private int nextTimeout;

    public HeartBeat(String processName, int nextTimeout) {
        this.processName = processName;
        this.nextTimeout = nextTimeout;
    }
    
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getNextTimeout() {
        return nextTimeout;
    }

    public void setNextTimeout(int nextTimeout) {
        this.nextTimeout = nextTimeout;
    }
    
    @Override
    public int compareTo(HeartBeat o) {
        return processName.compareTo(o.getProcessName());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HeartBeat other = (HeartBeat) obj;
        if (!Objects.equals(this.processName, other.processName)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "HeartBeat{" + "processName=" + processName + ", nextTimeout=" + nextTimeout + '}';
    }

    
}

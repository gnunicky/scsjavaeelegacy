package com;


/**
 *
 * @author Russo , Didomenico e Delpopolo
 */
public class Q_entry implements Comparable<Q_entry>{
    
    //Messaggio ----
    private Object data;            //Generico elemento da appendere
    //--------------
    
    private Tag     tag;             // identificatore unico del messaggio
    private int     sender_id;       // sender del messaggio
    private int     temp_ts;         // tentative timestamp assegnato al messaggio
    private boolean deliverable;     // se il messaggio è ready for delivery

    public Q_entry(Object o, Tag tag, int sender_id, int temp_ts, boolean deliverable) {
        this.data=o;
        this.tag = tag;
        this.sender_id = sender_id;
        this.temp_ts = temp_ts;
        this.deliverable = deliverable;
    }
    

    public Object getData(){return data;}

    public Tag getTag() {
        return tag;
    }

    public int getSender_id() {
        return sender_id;
    }

    public int getTemp_ts() {
        return temp_ts;
    }

    public boolean isDeliverable() {
        return deliverable;
    }

    public void setDeliverable(boolean deliverable) {
        this.deliverable = deliverable;
    }

    public void setTemp_ts(int temp_ts) {
        this.temp_ts = temp_ts;
    }

    @Override
    public int compareTo(Q_entry o) {
        if(temp_ts > o.getTemp_ts()) return 1;
        else if(temp_ts < o.getTemp_ts()) return -1;
        else{
            //Se il time stamp è uguale l'ordine viene fatto basandosi sull'id del processo
            int i = tag.getClientID();
            int j = o.getTag().getClientID();
            if(i>j) return 1;
            if(i<j) return -1;
            else return 0;
        }
    }

    @Override
    public String toString() {
        return "\nQ_entry:"+
               "\n{\n"+
               "   Tag: "+tag + "\n"+ 
               "   Sender: "+sender_id+"\n"+
               "   temp_ts: "+temp_ts + "\n"+ 
               "   deliverable: "+deliverable + "\n"+ 
               "}\n";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduino.object.counter.gui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mathew
 */
public class Cue <t> {
    private int LENGTH;
    
    List<t> CUE;
    
    public Cue(int length){
        LENGTH = length;
        CUE = new ArrayList<t>();
    }
    public t takeFront(){
        return CUE.remove(0);
    }
    public void addToRear(t obj){
        if(CUE.size()>= LENGTH) takeFront();
        CUE.add(obj);
    }
    @Override
    public String toString(){
        String app= "";
        for(t obj: CUE) app+=obj;
        return app;
    }
    
    public int getSize(){
        return CUE.size();
    }
    
    public static void main(String args[]){
       
    }
}

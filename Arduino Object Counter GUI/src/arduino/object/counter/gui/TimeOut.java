/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduino.object.counter.gui;

/**
 *
 * @author mathew
 */
public abstract class TimeOut {
    private int TIMEOUT =0;
    private long startMillis =0;
    private boolean timerRunning = false;
    private boolean disposed = false;
        public TimeOut(int limitMillis){
        TIMEOUT = limitMillis;
        
        ArduinoObjectCounterGUI.inst.addShutdownHook(()-> dispose());
        
        new Thread(()->{
        while(!disposed) {
            if(timerRunning && System.currentTimeMillis()>=(startMillis+TIMEOUT)){
                timeOut();
                timerRunning = false;
                dispose();
            }
            try{Thread.sleep(250);}catch(Exception e){}
            
        }}).start();
        
    }
    public void dispose(){
        System.out.println("Disposing thread");
        disposed = true;
    }
    public abstract void timeOut();
    
    public void start(){
        
        startMillis = System.currentTimeMillis();
        timerRunning = true;
        
    }
    
    public void reset(boolean stopTimer){
        timerRunning = !stopTimer;
        startMillis = System.currentTimeMillis();
    }
    
}

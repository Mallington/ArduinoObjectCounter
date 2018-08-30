/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduino.object.counter.gui;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathew
 */
public abstract class Serial {
      private int TIME_OUT = 1000;
      private int TOTAL_OVERRUN =0;
      private int EXTRA =0;
      private boolean RUN = true;
      private InputStream IN =null;
      SerialPort PORT = null;
      private String DEVICE_ID;
      
      long lastSeen =0;
      long responsePeriod = 1000; //in millis
      
      public Serial(SerialPort port, String devID){
          IN = (PORT = port).getInputStream();
          DEVICE_ID = devID;
         ArduinoObjectCounterGUI.inst.addShutdownHook(()-> {
              try {
                  stop();
              } catch (IOException ex) {
                  Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
      }
      
      public void setTimeout(int timeout){
          TIME_OUT = timeout;
      }
      
      public void start() throws IOException, InterruptedException{
       connected("Connecting...");
         new Thread(()->{
             
                
                
                
                  connected( DEVICE_ID);
            
             TimeOut time = new TimeOut (TIME_OUT) {
                 @Override
                 public void timeOut() {
                     try {
                         System.out.println("Connection timed out");
                         stop();
                     } catch (IOException ex) {
                         Logger.getLogger(Serial.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
             };
             time.start();
          while(RUN){
            
              try {
                 
                  if(IN.available()>0) {
                      int in = IN.read();
                      time.reset(false);
                      if((in - EXTRA)<0 && in ==0 &&EXTRA ==255) TOTAL_OVERRUN ++;
                      EXTRA = in;
                      updateCount(getCount());
                  }
                  
                
              } catch (Exception ex) {
                  System.out.println("Failed to read from port");
              }
          }
          
          }).start();
      }
      public void stop() throws IOException{
          System.out.println("Closing Port");
          RUN = false;
          IN.close();
          endStream();
      }
      public int getCount(){
          return EXTRA+(TOTAL_OVERRUN*256);
      }
      public abstract void updateCount(int value);
      public abstract void endStream();
      public abstract void connected(String devID);
   
        
    
}

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
      private int TOTAL_OVERRUN =0;
      private int EXTRA =0;
      private boolean RUN = true;
      private InputStream IN =null;
      SerialPort PORT = null;
      
      long lastSeen =0;
      long responsePeriod = 1000; //in millis
      
      public Serial(SerialPort port){
          IN = (PORT = port).getInputStream();
         
      }
      
      public void start() throws IOException, InterruptedException{
       connected("Connecting...");
         new Thread(()->{
             
             try {
                String devID=  retrieveDeviceID(IN);
                 System.out.println("Header Found");
                  connected(devID);
             } catch (IOException ex) {
                 System.out.println("Failed to find header");
             }
          while(RUN){
            
              try {
                 
                  if(IN.available()>0) {
                      int in = IN.read();
                      System.out.println("dif"+(in-EXTRA));
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
    
      public static int getPortValue(String descriptor) {
        List<Port> bob = listPorts();
        for (Port alice : bob) {
            if (alice.descriptor.contains(descriptor)) {
                return alice.port;
            }
        }

        return -1;
    }
    public static String retrieveDeviceID(InputStream in) throws IOException{
        boolean headerFufilled = false;
        Cue<Character> cue = new Cue<Character>("HEADER".length());
        while(!headerFufilled) {
           if(in.available()>0) {
               cue.addToRear((char)in.read());
               if(cue.toString().equalsIgnoreCase("HEADER")) headerFufilled = true;
           }
           
        }
        
        
        return new Scanner(in).nextLine();
    }
    public static List<Port> listPorts() {
        List<Port> portNames = new ArrayList<Port>();
        int i = 0;
        for (SerialPort port : SerialPort.getCommPorts()) {
            
            Port p = new Port(i++, port.getSystemPortName());
            if(port.getSystemPortName().toLowerCase().contains("tty.usb")){
            portNames.add(p);}
        }
        return portNames;
    }
}

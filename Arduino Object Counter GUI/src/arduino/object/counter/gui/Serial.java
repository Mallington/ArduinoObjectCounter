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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathew
 */
public abstract class Serial {
      private boolean RUN = true;
      private InputStream IN =null;
      public Serial(SerialPort port){
          IN = port.getInputStream();
      }
      
      public void start() throws IOException{
          new Thread(()->{
          
          while(RUN){
              
              try {
                  Thread.sleep(100);
                  if(IN.available()>0) updateCount(IN.read());
              } catch (Exception ex) {
                  System.out.println("Failed to read from port");
              }
          }
          }).start();
      }
      public void stop() throws IOException{
          RUN = false;
          IN.close();
      }
    
    
      public abstract void updateCount(int value);
    
      public static int getPortValue(String descriptor) {
        List<Port> bob = listPorts();
        for (Port alice : bob) {
            if (alice.descriptor.contains(descriptor)) {
                return alice.port;
            }
        }

        return -1;
    }

    public static List<Port> listPorts() {
        List<Port> portNames = new ArrayList<Port>();
        int i = 0;
        for (SerialPort port : SerialPort.getCommPorts()) {
            Port p = new Port(i++, port.getSystemPortName());
            portNames.add(p);
        }
        return portNames;
    }
}

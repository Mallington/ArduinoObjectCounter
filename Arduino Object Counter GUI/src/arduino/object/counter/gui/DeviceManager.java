/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduino.object.counter.gui;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mathew
 */
public class DeviceManager {
    private int TIME_OUT;
    public boolean timedout = false;
    public DeviceManager(int timeout){
        TIME_OUT = timeout;
    }
    
    public  List<Port> listPorts()  {
        List<Port> portNames = new ArrayList<Port>();
        int i = 0;
        for (SerialPort port : SerialPort.getCommPorts()) {
            try{
            // port.openPort();
           // String id = retrieveDeviceID(port.getInputStream(), 2000); 
           // if(id!=null){
            Port p = new Port(i, port.getSystemPortName(), null);
            portNames.add(p);//}
             } catch(Exception e){ System.out.println("Could not retrieve device ID");}
            i++;
        }
        return portNames;
    }
        
        public String retrieveDeviceID(InputStream in, int timeout) throws IOException{
        boolean headerFufilled = false;
        timedout = false;
        Cue<Character> cue = new Cue<Character>("HEADER".length());
        
         TimeOut time = new TimeOut (timeout) {
                 @Override
                 public void timeOut() {
                    timedout = true; 
                     System.out.println("Device retrieval timed out");}
             };
        time.start();
        while(!headerFufilled && !timedout) {
          
           if(in.available()>0) {
               cue.addToRear((char)in.read());
               time.reset(false);
               if(cue.toString().equalsIgnoreCase("HEADER")) headerFufilled = true;
           }
           
        }
        time.dispose();
        if(headerFufilled){
            System.out.println("Retrieving header");
            return new Scanner(in).nextLine();
        }
        else {
            System.out.println("No Header");
            return null;
        }
     
    }
      
      
}

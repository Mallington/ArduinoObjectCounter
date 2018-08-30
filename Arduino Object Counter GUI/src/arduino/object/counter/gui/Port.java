/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduino.object.counter.gui;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author mathew
 */
public class Port {
    final public String DESCRIPTOR;
    final public int PORT;
    public String ID;
    private SerialPort serial = null;
    public Port (int p, String descriptor, String id){
        PORT = p;
        DESCRIPTOR = descriptor;
        ID = id;
    } 
    
    public SerialPort initSerialPort() throws IOException{
        if(serial == null) {
           serial = SerialPort.getCommPorts()[PORT];
           serial.openPort();
         
        }
        return serial;
    }
    
}

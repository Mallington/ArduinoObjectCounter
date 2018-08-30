/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduino.object.counter.gui;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author mathew
 */
public class MainUIController implements Initializable {
    
    @FXML Text OBJECTS_COUNTED = new Text();
    @FXML ComboBox PORT_SELECTOR = new ComboBox();
    @FXML Text CONNECTED_DEVICE  = new Text();
    @FXML Button CONNECT_BUTTON = new Button();
    
    Serial SERIAL = null;
    private List<Port> PORTS = null;
    
    boolean CONNECTED = false;
    
    //Port refresh variables
    int last = 0;
    boolean first = true;
    private DeviceManager DEVICES = new DeviceManager(1000);
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addRefreshTimer();
    }    
    public void addRefreshTimer(){
        TimerTask task = new TimerTask() {
        public void run() {
            try {
                refreshPorts();
                if(first ) first = false;
                else if(last != PORTS.size() && !CONNECTED && last<PORTS.size()) displayConnected((PORTS.size()-last)+" new communication channel(s) detected","#33a3cc"); 
                last = PORTS.size(); 
            } catch (IOException ex) {
                System.out.println("Failed to refresh ports");
            }
        }
    };
    Timer timer = new Timer("Timer");
     
    long delay = 1000L;
   
   ArduinoObjectCounterGUI.inst.addShutdownHook(()-> timer.cancel());
    timer.scheduleAtFixedRate(task, delay, delay);
    
        System.out.println("Added timers");
    }
    public void refreshPorts() throws IOException{
   
        
        PORTS = DEVICES.listPorts();
          ObservableList<String> list = FXCollections.observableArrayList();
        for (Port p : PORTS) {
            list.add(p.PORT + ": " + p.DESCRIPTOR);
        }
       Platform.runLater(()-> PORT_SELECTOR.setItems(list));
    }
    public void connect(){
         CONNECT_BUTTON.setDisable(true);
        if(CONNECTED) {
            System.out.println("Already connected");
            return;
        }
        
       
        
          try {
              displayConnected("Connecting...","#d2e00d");
              int selected = PORT_SELECTOR.getSelectionModel().getSelectedIndex();
              Port p = this.PORTS.get(selected);
               new Thread ( ()->{
                  try {
                      if(SERIAL!=null) SERIAL.stop();
                      SerialPort s =p.initSerialPort();
                      p.ID = DEVICES.retrieveDeviceID(s.getInputStream(), 5000);
                      
                    
                      
                      
                      
                      if(p.ID !=null){
                          
                          SERIAL = new Serial(s, p.ID) {
                              @Override
                              public void updateCount(int value) {
                                  displayCounted(value);
                              }
                              
                              @Override
                              public void endStream() {
                                  displayConnected("Disconnected", "#bb1a1a");
                                  CONNECTED = false;
                                   CONNECT_BUTTON.setDisable(false);
                                  
                              }
                              
                              @Override
                              public void connected(String devID) {
                                  displayConnected(devID,"#1aba45");
                              }
                              
                          };
                          System.out.println("Connecting to " + p.DESCRIPTOR + " (" + p.PORT + ")");
                          CONNECTED = true;
                          SERIAL.start();
                          
                      }
                      else {
                          new Thread( ()-> JOptionPane.showMessageDialog(null, "Hmm, It seems you have selected an incorrect device.\nPlease try another port")).start();
                          CONNECT_BUTTON.setDisable(false);
                          displayConnected("Device not recognised", "#bb1a1a");
                      }  } catch (IOException ex) {
                      Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                  } catch (InterruptedException ex) {
                      Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
                  } catch(Exception e){ CONNECT_BUTTON.setDisable(false);}
               }).start();
             } catch (Exception e) {
                  CONNECT_BUTTON.setDisable(false);
              System.out.println("Connection failed");
        }
          System.out.println("Done.");
    }
    
    public void displayConnected(String ID, String paint){
        Platform.runLater(() ->{
            System.out.println("Displaying: \""+ID+"\"");
            CONNECTED_DEVICE.setText(ID);
            CONNECTED_DEVICE.setFill(Paint.valueOf(paint));
                });
    }
     public void displayCounted(int num){
        Platform.runLater(() ->OBJECTS_COUNTED.setText(num+""));
    }
    
    
}

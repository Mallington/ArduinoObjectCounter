/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduino.object.counter.gui;

import com.fazecast.jSerialComm.SerialPort;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 *
 * @author mathew
 */
public class MainUIController implements Initializable {
    
    @FXML Text OBJECTS_COUNTED = new Text();
    @FXML ComboBox PORT_SELECTOR = new ComboBox();
    @FXML Text CONNECTED_DEVICE  = new Text();
    Serial SERIAL = null;
    private List<Port> PORTS = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshPorts();
    }    
    
    public void refreshPorts(){
        System.out.println("Refreshing ports");
        PORTS = Serial.listPorts();
          ObservableList<String> list = FXCollections.observableArrayList();
        for (Port p : PORTS) {
            list.add(p.port + ": " + p.descriptor);
        }
        PORT_SELECTOR.setItems(list);
    }
    public void connect(){
          try {
              int selected = PORT_SELECTOR.getSelectionModel().getSelectedIndex();
              Port p = this.PORTS.get(selected);
              SerialPort s =p.initSerialPort();
               if(SERIAL!=null) SERIAL.stop();
              SERIAL = new Serial(s) {
                @Override
                public void updateCount(int value) {
                    displayCounted(value);
                }

                @Override
                public void endStream() {
                    displayConnected("Disconnected");
                    
                }

                  @Override
                  public void connected(String devID) {
                     displayConnected(devID);
                  }
               
            };
             System.out.println("Connecting to " + p.descriptor + " (" + p.port + ")");
            SERIAL.start();
             } catch (Exception e) {
              System.out.println("Connection failed");
        }
    }
    
    public void displayConnected(String ID){
        Platform.runLater(() ->CONNECTED_DEVICE.setText(ID));
    }
     public void displayCounted(int num){
        Platform.runLater(() ->OBJECTS_COUNTED.setText(num+""));
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduino.object.counter.gui;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mathew
 */
public class ArduinoObjectCounterGUI extends Application {
    public static ArduinoObjectCounterGUI inst;
    private List<Runnable> hooks = new ArrayList<Runnable>();
    @Override
    public void start(Stage stage) throws Exception {
        inst = this;
        Parent root = FXMLLoader.load(getClass().getResource("MainUI.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    
    public void addShutdownHook(Runnable r){
        hooks.add(r);
    }
    
    @Override
    public void stop(){
        System.out.println("Exiting");
        for(Runnable r :hooks) new Thread(r).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

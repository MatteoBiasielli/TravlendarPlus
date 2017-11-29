/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author matteo
 */
public class Travlendardesktopclient extends Application {
    
    /** attribute used to handle operations on the login screen*/
    private FXMLLoader loginLoader;
    /** attribute used to handle operations on the login screen*/
    private Stage loginStage;
    
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.loginStage=primaryStage;
        initializeLoginStage();
        this.loginStage.show();
    }
    
    
    /**initializes the loginStage and the loginLoader
    * 
    */
    private void initializeLoginStage() throws IOException{
        this.loginLoader= new FXMLLoader();
        this.loginLoader.setLocation(getClass().getResource("login.fxml"));
        this.loginStage.setScene(new Scene(loginLoader.load()));
        this.loginStage.setResizable(false);
        ((LoginController)loginLoader.getController()).setStage(loginStage);
    }
	
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

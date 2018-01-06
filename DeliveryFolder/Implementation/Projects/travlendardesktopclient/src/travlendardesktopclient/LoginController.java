/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import travlendardesktopclient.data.Data;
import travlendardesktopclient.network.NetworkLayer;
import travlendardesktopclient.network.loginresponse.ResponseLogin;
import travlendardesktopclient.network.loginresponse.ResponseLoginType;
import travlendardesktopclient.network.registerresponse.ResponseRegister;
import travlendardesktopclient.network.registerresponse.ResponseRegisterType;


/**
 * FXML Controller class
 *
 * @author matteo
 */
public class LoginController implements Initializable{
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField ip;
    @FXML
    private TextField registerusername;
    @FXML
    private PasswordField registerpassword;
    @FXML
    private Button registerButton;
    @FXML
    private Label registererrorLabel;
    @FXML
    private TextField registerip;
    
    
    /** attribute used to handle operations on the main screen*/
    private FXMLLoader mainLoader;
    /** attribute used to handle operations on the main screen*/
    private Stage mainStage;
    /** attribute containing the stage controlled by this controller*/
    private Stage thisStage;
    public LoginController(){
        
    }
    @FXML
    public void initialize(){
            
    }
    
    /**tells the controller the stage it is controlling*/
    public void setStage(Stage s){
        this.thisStage=s;
    }
    
    /**initializes the mainStage and the mainLoader
    * 
    */
    private void initializeMainStage() throws IOException{
        mainStage=new Stage();
        this.mainLoader= new FXMLLoader();
        this.mainLoader.setLocation(getClass().getResource("mainwindow.fxml"));
        this.mainStage.setScene(new Scene(mainLoader.load()));
        this.mainStage.setResizable(false);
        ((MainWindowController)this.mainLoader.getController()).setStage(mainStage);
        ((MainWindowController)this.mainLoader.getController()).initMap();
    }
    
    /**called automatically on application start, exploits all initialization
     * procedures required
     * @param location NOT USED
     * @param resources NOT USED
     */ 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initializeMainStage();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**activated when the login button is pressed. Handles all the required 
     * procedures to peform the login.
     */
    public void onLogin(){
        loginButton.setDisable(true);
        String user=username.getText();
        String pass=password.getText();
        username.setText("");
        password.setText("");
        String ipadd=ip.getText();
        NetworkLayer.setIP(ipadd);
        try{
            ResponseLogin rl=NetworkLayer.loginRequest(user, pass);
            ResponseLoginType rlt=rl.getType();
            errorLabel.setText(rlt.getMessage());
            if(rlt==ResponseLoginType.OK){
                Data.setUser(rl.getUser());
                mainStage.show();
                ((MainWindowController)mainLoader.getController()).update();
                ((MainWindowController)mainLoader.getController()).startNotificationThread();
                this.thisStage.hide();
            }
        }catch(IOException e){
            errorLabel.setText("No Internet connection is available.");
        }finally{
            loginButton.setDisable(false);
        }
    }
    
    /**activated when the login button is pressed. Handles all the required 
     * procedures to peform the registration.
     */
    public void onRegister(){
        registerButton.setDisable(true);
        String user=registerusername.getText();
        String pass=registerpassword.getText();
        registerusername.setText("");
        registerpassword.setText("");
        String ipadd=registerip.getText();
        NetworkLayer.setIP(ipadd);
        try{
            ResponseRegister rl=NetworkLayer.registerRequest(user, pass);
            ResponseRegisterType rrt=rl.getType();
            registererrorLabel.setText(rrt.getMessage());
        }catch(IOException e){
            registererrorLabel.setText("No Internet connection is available.");
        }finally{
            registerButton.setDisable(false);
        }
    }

    void stopNotificationThread() {
        ((MainWindowController)mainLoader.getController()).stopNotificationThread();
    }
}

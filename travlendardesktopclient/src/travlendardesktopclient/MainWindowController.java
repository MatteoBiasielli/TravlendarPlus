package travlendardesktopclient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import travlendardesktopclient.data.Data;
import travlendardesktopclient.data.activities.Activity;
import travlendardesktopclient.data.activities.Break;
import travlendardesktopclient.data.activities.FixedActivity;
import travlendardesktopclient.data.user.FavouritePosition;
import travlendardesktopclient.data.user.preferences.BooleanPreferencesSet;

import travlendardesktopclient.data.user.preferences.RangedPreference;
import travlendardesktopclient.data.user.preferences.RangedPreferenceType;
import travlendardesktopclient.network.NetworkLayer;
import travlendardesktopclient.network.addactivityresponse.ResponseAddActivity;
import travlendardesktopclient.network.addactivityresponse.ResponseAddActivityNotification;
import travlendardesktopclient.network.addactivityresponse.ResponseAddActivityType;
import travlendardesktopclient.network.addtagresponse.ResponseAddTag;
import travlendardesktopclient.network.addtagresponse.ResponseAddTagType;
import travlendardesktopclient.network.deleteactivityresponse.ResponseDeleteActivity;
import travlendardesktopclient.network.deleteactivityresponse.ResponseDeleteActivityType;
import travlendardesktopclient.network.deleterangedpreferenceresponse.ResponseDeleteRangedPreferences;
import travlendardesktopclient.network.deleterangedpreferenceresponse.ResponseDeleteRangedPreferencesType;
import travlendardesktopclient.network.deletetagresponse.ResponseDeleteTag;
import travlendardesktopclient.network.deletetagresponse.ResponseDeleteTagType;
import travlendardesktopclient.network.retrievenotificationsresponse.ResponseRetrieveNotifications;
import travlendardesktopclient.network.retrievenotificationsresponse.ResponseRetrieveNotificationsType;
import travlendardesktopclient.network.updateactivityresponse.ResponseUpdateActivity;
import travlendardesktopclient.network.updateactivityresponse.ResponseUpdateActivityType;
import travlendardesktopclient.network.updatebooleanpreferencesresponse.ResponseUpdateBooleanPreferences;
import travlendardesktopclient.network.updatebooleanpreferencesresponse.ResponseUpdateBooleanPreferencesType;
import travlendardesktopclient.network.updaterangedpreferencesresponse.ResponseUpdateRangedPreferences;
import travlendardesktopclient.network.updaterangedpreferencesresponse.ResponseUpdateRangedPreferencesType;

/**
 * FXML Controller class
 *
 * @author matteo
 */
public class MainWindowController implements Initializable {
    private UpdateNotificationsThread notifThread;
    private Stage thisStage;
    private ArrayList<Activity> selectedDayActivities;
    private int totActivitiesForSelectedDay=0;
    private int actualActivity=0;
    private ArrayList<Activity> selectedDayActivitiesUpdate;
    private int totActivitiesForSelectedDayUpdate=0;
    private int actualActivityUpdate=0;
    @FXML
    private Label selectedTagAddress;
    @FXML
    private ComboBox chosenTag;

    
    
    @FXML
    private TextArea addTagAddress;
    @FXML
    private TextField tagToAdd;

    
    @FXML
    private RadioButton norm;
    @FXML
    private RadioButton carb;
    @FXML
    private RadioButton cost;
    @FXML
    private RadioButton time;

    @FXML
    private CheckBox car;
    @FXML
    private CheckBox carsharing;
    @FXML
    private CheckBox bike;
    @FXML
    private CheckBox bikesharing;
    @FXML
    private CheckBox transport;
    @FXML
    private CheckBox ubertaxi;
    
    @FXML
    private TextField maxWalk;
    @FXML
    private TextField maxCar;
    @FXML
    private TextField maxTransp;
    @FXML
    private TextField maxBike;
    @FXML
    private TextField maxCost;
    
    
    @FXML
    private Label actLabel;
    @FXML
    private Label actNotes;
    @FXML
    private Label actLocation;
    @FXML
    private Label actStartDate;
    @FXML
    private Label actEndDate;
    @FXML
    private Label actStart;
    @FXML
    private Label actFlexible;
    @FXML
    private Label actDuration;
    @FXML
    private DatePicker showActDatePicker;
    
    
    
    @FXML
    private TextArea actNewLabel;
    @FXML
    private TextArea actNewNotes;
    @FXML
    private TextArea actNewLocation;
    @FXML
    private ComboBox actNewLocationTag;
    @FXML
    private ComboBox actNewStartTag;
    @FXML
    private TextArea actNewStart;
    @FXML
    private RadioButton actNewFixed;
    @FXML
    private RadioButton actNewBreak;
    @FXML
    private ComboBox actNewDuration;
    @FXML
    private DatePicker actNewStartDatePicker;
    @FXML
    private DatePicker actNewEndDatePicker;
    @FXML
    private ComboBox actNewStartHour;
    @FXML
    private ComboBox actNewStartMinute;
    @FXML
    private ComboBox actNewEndHour;
    @FXML
    private ComboBox actNewEndMinute;
    
    
    
    @FXML
    private TextArea actUpdateLabel;
    @FXML
    private TextArea actUpdateNotes;
    @FXML
    private TextArea actUpdateLocation;
    @FXML
    private ComboBox actUpdateLocationTag;
    @FXML
    private ComboBox actUpdateStartTag;
    @FXML
    private TextArea actUpdateStart;
    @FXML
    private RadioButton actUpdateFixed;
    @FXML
    private RadioButton actUpdateBreak;
    @FXML
    private ComboBox actUpdateDuration;
    @FXML
    private DatePicker actUpdateStartDatePicker;
    @FXML
    private DatePicker actUpdateEndDatePicker;
    @FXML
    private ComboBox actUpdateStartHour;
    @FXML
    private ComboBox actUpdateStartMinute;
    @FXML
    private ComboBox actUpdateEndHour;
    @FXML
    private ComboBox actUpdateEndMinute;
    @FXML
    private DatePicker actUpdateDatePicker;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.actNewEndHour.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24");
        this.actNewStartHour.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24");
        this.actNewEndMinute.getItems().addAll("00","15","30","45");
        this.actNewStartMinute.getItems().addAll("00","15","30","45");
        this.actUpdateEndHour.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24");
        this.actUpdateStartHour.getItems().addAll("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24");
        this.actUpdateEndMinute.getItems().addAll("00","15","30","45");
        this.actUpdateStartMinute.getItems().addAll("00","15","30","45");
        this.actNewDuration.setDisable(true);
    }
    public void startNotificationThread(){
        this.notifThread=new UpdateNotificationsThread(this);
        this.notifThread.start();
    }    
    @FXML
    public void init(){
        
    }
    public void setStage(Stage s){
        this.thisStage=s;
    }
    public void onUpdateActivity(){
        try{
            String l=this.actUpdateLabel.getText();
            String n=this.actUpdateNotes.getText()==null?"":this.actUpdateNotes.getText();
            String startAddr=this.actUpdateStart.getText()==null?"":this.actUpdateStart.getText();
            String startAddrTag=this.actUpdateStartTag.getValue()==null?"":((FavouritePosition)this.actUpdateStartTag.getValue()).getTag();
            String locationAddr=this.actUpdateLocation.getText()==null?"":this.actUpdateLocation.getText();
            String locationAddrTag=this.actUpdateLocationTag.getValue()==null?"":((FavouritePosition)this.actUpdateLocationTag.getValue()).getTag();
            Boolean flexible=this.actUpdateBreak.isSelected();
            String duration="0";
            if(flexible)
                duration=this.actUpdateDuration.getValue()==null?"":(String)this.actUpdateDuration.getValue();
            Integer startMinute=Integer.parseInt((String)this.actUpdateStartMinute.getValue());
            Integer startHour=Integer.parseInt((String)this.actUpdateStartHour.getValue());
            Integer endMinute=Integer.parseInt((String)this.actUpdateEndMinute.getValue());
            Integer endHour=Integer.parseInt((String)this.actUpdateEndHour.getValue());
            Date startDate=this.localDateToDate(this.actUpdateStartDatePicker.getValue());
            Date endDate=this.localDateToDate(this.actUpdateEndDatePicker.getValue());
            int id=this.selectedDayActivitiesUpdate.get(this.actualActivityUpdate).getKey();
            startDate.setHours(startHour);
            startDate.setMinutes(startMinute);
            endDate.setHours(endHour);
            endDate.setMinutes(endMinute);
            if(l==null || "".equals(l) || "".equals(duration)){
                showAlert("Invalid Input Data");
                return;
            }
            if("".equals(startAddr) && "".equals(startAddrTag) || "".equals(locationAddrTag) && "".equals(locationAddr)){
                showAlert("Invalid Input Data");
                return;
            }    
            ResponseUpdateActivity rua=NetworkLayer.updateActivityRequest(Data.getUser().getUsername(), Data.getUser().getPassword(), l, n, locationAddr, locationAddrTag, startAddr, startAddrTag, flexible, duration, startDate, endDate,id);
            if(rua.getType()==ResponseUpdateActivityType.OK){
                String text="Actvity updated correctly.";
                if(rua.getNotification()!=ResponseAddActivityNotification.NO)
                    text=text+"\n"+rua.getNotification().getMessage();
                Data.getUser().setCalendar(rua.getU().getCalendar());
                this.onDateSelect();
                showAlert(text);
            }else if(rua.getType()==ResponseUpdateActivityType.OK_ESTIMATED_TIME){
                String text=rua.getType().getMessage();
                if(rua.getNotification()!=ResponseAddActivityNotification.NO)
                    text=text+"\n"+rua.getNotification().getMessage();
                Data.getUser().setCalendar(rua.getU().getCalendar());
                this.onDateSelect();
                showAlert(text);
            }else
               showAlert(rua.getType().getMessage()); 
        }catch(NumberFormatException|NullPointerException e){
            showAlert("Invalid Input Data");
        }catch(IOException e){
            showAlert("Check your internet connection.");
        }
    }
    public void onAddActivity(){
        try{
            String l=this.actNewLabel.getText();
            String n=this.actNewNotes.getText()==null?"":this.actNewNotes.getText();
            String startAddr=this.actNewStart.getText()==null?"":this.actNewStart.getText();
            String startAddrTag=this.actNewStartTag.getValue()==null?"":((FavouritePosition)this.actNewStartTag.getValue()).getTag();
            String locationAddr=this.actNewLocation.getText()==null?"":this.actNewLocation.getText();
            String locationAddrTag=this.actNewLocationTag.getValue()==null?"":((FavouritePosition)this.actNewLocationTag.getValue()).getTag();
            Boolean flexible=this.actNewBreak.isSelected();
            String duration="0";
            if(flexible)
                duration=this.actNewDuration.getValue()==null?"":(String)this.actNewDuration.getValue();
            Integer startMinute=Integer.parseInt((String)this.actNewStartMinute.getValue());
            Integer startHour=Integer.parseInt((String)this.actNewStartHour.getValue());
            Integer endMinute=Integer.parseInt((String)this.actNewEndMinute.getValue());
            Integer endHour=Integer.parseInt((String)this.actNewEndHour.getValue());
            Date startDate=this.localDateToDate(this.actNewStartDatePicker.getValue());
            Date endDate=this.localDateToDate(this.actNewEndDatePicker.getValue());
            startDate.setHours(startHour);
            startDate.setMinutes(startMinute);
            endDate.setHours(endHour);
            endDate.setMinutes(endMinute);
            if(l==null || "".equals(l) || "".equals(duration)){
                showAlert("Invalid Input Data");
                return;
            }
            if("".equals(startAddr) && "".equals(startAddrTag) || "".equals(locationAddrTag) && "".equals(locationAddr)){
                showAlert("Invalid Input Data");
                return;
            }    
            ResponseAddActivity raa=NetworkLayer.addActivityRequest(Data.getUser().getUsername(), Data.getUser().getPassword(), l, n, locationAddr, locationAddrTag, startAddr, startAddrTag, flexible, duration, startDate, endDate);
            if(raa.getType()==ResponseAddActivityType.OK){
                String text="Actvity added correctly.";
                if(raa.getNotification()!=ResponseAddActivityNotification.NO)
                    text=text+"\n"+raa.getNotification().getMessage();
                Data.getUser().setCalendar(raa.getUser().getCalendar());
                this.onDateSelect();
                showAlert(text);
            }else if(raa.getType()==ResponseAddActivityType.OK_ESTIMATED_TIME){
                String text=raa.getType().getMessage();
                if(raa.getNotification()!=ResponseAddActivityNotification.NO)
                    text=text+"\n"+raa.getNotification().getMessage();
                Data.getUser().setCalendar(raa.getUser().getCalendar());
                this.onDateSelect();
                showAlert(text);
            }else
               showAlert(raa.getType().getMessage()); 
        }catch(NumberFormatException|NullPointerException e){
            showAlert("Invalid Input Data");
        }catch(IOException e){
            showAlert("Check your internet connection.");
        }
    }
    public void setDuration(){
        try{
            Integer startMinute=Integer.parseInt((String)this.actNewStartMinute.getValue());
            Integer startHour=Integer.parseInt((String)this.actNewStartHour.getValue());
            Integer endMinute=Integer.parseInt((String)this.actNewEndMinute.getValue());
            Integer endHour=Integer.parseInt((String)this.actNewEndHour.getValue());
            Date startDate=this.localDateToDate(this.actNewStartDatePicker.getValue());
            Date endDate=this.localDateToDate(this.actNewEndDatePicker.getValue());
            startDate.setHours(startHour);
            startDate.setMinutes(startMinute);
            endDate.setHours(endHour);
            endDate.setMinutes(endMinute);
            this.actNewDuration.getItems().clear();
            if(startDate.before(endDate));
                for(long i=15*60*1000;i<=endDate.getTime()-startDate.getTime();i=i+15*60*1000)
                    this.actNewDuration.getItems().add(Integer.toString((int) (i/(1000*60))));
        }catch(NumberFormatException|NullPointerException e){
            //NOTHING, I JUST HAD TO SKIP OUT
        }
    }
    public void onSelectBreak(){
        this.actNewStart.setText("break");
        this.actNewLocation.setText("break");
        this.actNewStart.setDisable(true);
        this.actNewLocation.setDisable(true);
        this.actNewLocationTag.setDisable(true);
        this.actNewStartTag.setDisable(true);
        this.actNewDuration.setDisable(false);
    }
    public void onSelectFixed(){
        this.actNewStart.setText("");
        this.actNewLocation.setText("");
        this.actNewStart.setDisable(false);
        this.actNewLocation.setDisable(false);
        this.actNewDuration.setDisable(true);
        this.actNewLocationTag.setDisable(false);
        this.actNewStartTag.setDisable(false);
    }
    
    public void onUpdateSelectBreak(){
        this.actUpdateStart.setText("break");
        this.actUpdateLocation.setText("break");
        this.actUpdateStart.setDisable(true);
        this.actUpdateLocation.setDisable(true);
        this.actUpdateLocationTag.setDisable(true);
        this.actUpdateStartTag.setDisable(true);
        this.actUpdateDuration.setDisable(false);
    }
    public void onUpdateSelectFixed(){
        this.actUpdateStart.setText("");
        this.actUpdateLocation.setText("");
        this.actUpdateStart.setDisable(false);
        this.actUpdateLocation.setDisable(false);
        this.actUpdateDuration.setDisable(true);
        this.actUpdateLocationTag.setDisable(false);
        this.actUpdateStartTag.setDisable(false);
    }
    public void setDurationUpdate(){
        try{
            Integer startMinute=Integer.parseInt((String)this.actUpdateStartMinute.getValue());
            Integer startHour=Integer.parseInt((String)this.actUpdateStartHour.getValue());
            Integer endMinute=Integer.parseInt((String)this.actUpdateEndMinute.getValue());
            Integer endHour=Integer.parseInt((String)this.actUpdateEndHour.getValue());
            Date startDate=this.localDateToDate(this.actUpdateStartDatePicker.getValue());
            Date endDate=this.localDateToDate(this.actUpdateEndDatePicker.getValue());
            startDate.setHours(startHour);
            startDate.setMinutes(startMinute);
            endDate.setHours(endHour);
            endDate.setMinutes(endMinute);
            this.actUpdateDuration.getItems().clear();
            if(startDate.before(endDate));
                for(long i=15*60*1000;i<=endDate.getTime()-startDate.getTime();i=i+15*60*1000)
                    this.actUpdateDuration.getItems().add(Integer.toString((int) (i/(1000*60))));
        }catch(NumberFormatException|NullPointerException e){
            //NOTHING, I JUST HAD TO SKIP OUT
        }
    }
    public void onDateSelectUpdate(){
        this.setUpdateArea("","","","",true,"",null,null);
        LocalDate selDate=this.actUpdateDatePicker.getValue();
        if(selDate==null)
            return;
        Date date=localDateToDate(selDate);
        ArrayList<FixedActivity> fa=Data.getUser().getCalendar().getFixedActivities();
        this.selectedDayActivitiesUpdate= new ArrayList<>();
        for( FixedActivity act:fa){
            if(act.getStartDate().compareTo(date)>=0 && act.getStartDate().compareTo(new Date(date.getTime()+24*60*60*1000))<=0)
                this.selectedDayActivitiesUpdate.add(act);
        }
        ArrayList<Break> br=Data.getUser().getCalendar().getBreaks();
        for( Break act:br){
            if(act.getStartDate().compareTo(date)>=0 && act.getStartDate().compareTo(new Date(date.getTime()+24*60*60*1000))<=0)
                this.selectedDayActivitiesUpdate.add(act);
        }
        this.totActivitiesForSelectedDayUpdate=this.selectedDayActivitiesUpdate.size();
        this.actualActivityUpdate=0; 
        if(this.totActivitiesForSelectedDayUpdate>0){
            this.selectedDayActivitiesUpdate.sort(null);
            Activity a=this.selectedDayActivitiesUpdate.get(0);
            this.setUpdateArea(a.getLabel(), a.getNotes(), a.getLocationAddress(), a.getStartPlaceAddress(), a.isFlexible(), Long.toString(a.getDuration()), a.getStartDate(), a.getEndDate());
        }
    }
    public void onSelectTag(){
        FavouritePosition sel=(FavouritePosition) chosenTag.getValue();
        if(sel!=null){
            selectedTagAddress.setText(setNewLines(35,sel.getAddress()));
        }
    }
    public void onDeleteTag(){
        try{
            FavouritePosition sel=(FavouritePosition) chosenTag.getValue();
            if(sel!=null){
                String tag=sel.getTag();
                ResponseDeleteTag rdt=NetworkLayer.deleteTagRequest(Data.getUser().getUsername(), Data.getUser().getPassword(), tag);
                if(rdt.getType()==ResponseDeleteTagType.OK){
                    Data.setFavPlaces(rdt.getPositions());
                    this.updateTags();
                }
            }
        }catch(IOException e){
            showAlert("Check your internet connection");
        }
    }
    public void onAddTag(){
        try{
            String addr=this.addTagAddress.getText();
            String tag=this.tagToAdd.getText();
            if(tag!=null && addr!=null && !"".equals(addr) && !"".equals(tag)){
                ResponseAddTag rdt=NetworkLayer.addTagRequest(Data.getUser().getUsername(), Data.getUser().getPassword(), tag,addr);
                if(rdt.getType()==ResponseAddTagType.OK){
                    Data.setFavPlaces(rdt.getPositions());
                    this.updateTags();
                }else
                    showAlert(rdt.getType().getMessage());
            }
        }catch(IOException e){
            showAlert("Check your internet connection");
        }
    }
    public void onUpdateBoolPrefs(){
        try{
            boolean pC=car.isSelected();
            boolean cS=carsharing.isSelected();
            boolean pB=bike.isSelected();
            boolean bS=bikesharing.isSelected();
            boolean pT=transport.isSelected();
            boolean uT=ubertaxi.isSelected();
            int mode=1;
            if(norm.isSelected())
                mode=Integer.parseInt(norm.getId());
            else if(time.isSelected())
                mode=Integer.parseInt(time.getId());
            else if(cost.isSelected())
                mode=Integer.parseInt(cost.getId());
            else if(carb.isSelected())
                mode=Integer.parseInt(carb.getId());
            ResponseUpdateBooleanPreferences rubp=NetworkLayer.updateBooleanPreferencesRequest(Data.getUser().getUsername(), Data.getUser().getPassword(), pC,cS,pB,bS,pT,uT,mode);
            if(rubp.getType()==ResponseUpdateBooleanPreferencesType.OK){
                Data.getUser().setPreferences(Data.getUser().getRangedPreferences(), rubp.getPrefs());
                this.updatePreferences();
                showAlert("Preferences updated succesfully");
            }else
                showAlert(rubp.getType().getMessage());
        }catch(IOException e){
            showAlert("Check your internet connection");
        }
    }
    public void onDeleteRangedPrefs(){
        ArrayList<String> ids= new ArrayList<>();
        try{
            String val=maxCar.getText();
            if("".equals(val))
                ids.add(maxCar.getId());
            val=maxWalk.getText();
            if("".equals(val))
                ids.add(maxWalk.getId());
            val=maxTransp.getText();
            if("".equals(val))
                ids.add(maxTransp.getId());
            val=maxBike.getText();
            if("".equals(val))
                ids.add(maxBike.getId());
            val=maxCost.getText();
            if("".equals(val))
                ids.add(maxCost.getId());
            if(!ids.isEmpty()){
                ResponseDeleteRangedPreferences rurp=NetworkLayer.deleteRangedPreferencesRequest(Data.getUser().getUsername(), Data.getUser().getPassword(), ids);
                if(rurp.getType()==ResponseDeleteRangedPreferencesType.OK){
                    Data.getUser().setPreferences(rurp.getPrefs(), Data.getUser().getBoolPreferences());
                    showAlert("Preferences deleted succesfully.");
                }else
                    showAlert(rurp.getType().getMessage());
            }
            this.updatePreferences();
        }catch(IOException e){
            showAlert("Check your internet connection.");
        }
    }
    public void onUpdateRangedPrefs(){
        ArrayList<Integer> vals= new ArrayList<>();
        ArrayList<String> ids= new ArrayList<>();
        try{
            String val=maxCar.getText();
            if(!"".equals(val)){
                vals.add(Integer.parseInt(val));
                ids.add(maxCar.getId());
            }
            val=maxWalk.getText();
            if(!"".equals(val)){
                vals.add(Integer.parseInt(val));
                ids.add(maxWalk.getId());
            }
            val=maxTransp.getText();
            if(!"".equals(val)){
                vals.add(Integer.parseInt(val));
                ids.add(maxTransp.getId());
            }
            val=maxBike.getText();
            if(!"".equals(val)){
                vals.add(Integer.parseInt(val));
                ids.add(maxBike.getId());
            }
            val=maxCost.getText();
            if(!"".equals(val)){
                vals.add(Integer.parseInt(val));
                ids.add(maxCost.getId());
            }
            if(!ids.isEmpty()){
                ResponseUpdateRangedPreferences rurp=NetworkLayer.updateRangedPreferencesRequest(Data.getUser().getUsername(), Data.getUser().getPassword(), vals, ids);
                if(rurp.getType()==ResponseUpdateRangedPreferencesType.OK){
                    Data.getUser().setPreferences(rurp.getPrefs(), Data.getUser().getBoolPreferences());
                    showAlert("Preferences updated succesfully.");
                }else
                    showAlert(rurp.getType().getMessage());
                this.updatePreferences();
            }
        }catch(NumberFormatException e){
            showAlert("You must enter integer values or leave the fields blank.");
        }catch(IOException e){
            showAlert("Check your internet connection.");
        }
        
    }
    public void onDateSelect(){
        this.setActivityLabels("", "", "", "", null,null,null,null);
        LocalDate selDate=this.showActDatePicker.getValue();
        if(selDate==null)
            return;
        Date date=localDateToDate(selDate);
        ArrayList<FixedActivity> fa=Data.getUser().getCalendar().getFixedActivities();
        this.selectedDayActivities= new ArrayList<>();
        for( FixedActivity act:fa){
            if(act.getStartDate().compareTo(date)>=0 && act.getStartDate().compareTo(new Date(date.getTime()+24*60*60*1000))<=0)
                this.selectedDayActivities.add(act);
        }
        ArrayList<Break> br=Data.getUser().getCalendar().getBreaks();
        for( Break act:br){
            if(act.getStartDate().compareTo(date)>=0 && act.getStartDate().compareTo(new Date(date.getTime()+24*60*60*1000))<=0)
                this.selectedDayActivities.add(act);
        }
        this.totActivitiesForSelectedDay=this.selectedDayActivities.size();
        this.actualActivity=0; 
        if(this.totActivitiesForSelectedDay>0){
            this.selectedDayActivities.sort(null);
            Activity a=this.selectedDayActivities.get(0);
            this.setActivityLabels(a.getLabel(), a.getNotes(), a.getLocationAddress(), a.getStartPlaceAddress(), a.isFlexible(), a.getDuration(), a.getStartDate(), a.getEndDate());
        }
    }
    public void onNextAct(){
        if(this.selectedDayActivities!=null && !this.selectedDayActivities.isEmpty()){
            if(this.actualActivity+1<this.totActivitiesForSelectedDay){
                Activity a=this.selectedDayActivities.get(this.actualActivity+1);
                this.setActivityLabels(a.getLabel(), a.getNotes(), a.getLocationAddress(), a.getStartPlaceAddress(), a.isFlexible(), a.getDuration(), a.getStartDate(), a.getEndDate());
                this.actualActivity++;
            }   
        }
    }
    public void onDeleteActivity(){
        try{
            if(this.selectedDayActivities!=null && !this.selectedDayActivities.isEmpty()){
                Activity a=this.selectedDayActivities.get(this.actualActivity);
                ResponseDeleteActivity rda=NetworkLayer.deleteActivityRequest(Data.getUser().getUsername(), Data.getUser().getPassword(), Integer.toString(a.getKey()));
                if(rda.getRdat()==ResponseDeleteActivityType.OK){
                    Data.getUser().setCalendar(rda.getCal());
                    this.onDateSelect();
                    showAlert("Activity deleted succesfully.");
                }
                else
                    showAlert(rda.getRdat().getMessage());
            }
        }catch(IOException e){
            showAlert("Check your internet connection");
        }
    }
    public void onPrevAct(){
        if(this.selectedDayActivities!=null && !this.selectedDayActivities.isEmpty()){
            if(this.actualActivity-1>=0){
                Activity a=this.selectedDayActivities.get(this.actualActivity-1);
                this.setActivityLabels(a.getLabel(), a.getNotes(), a.getLocationAddress(), a.getStartPlaceAddress(), a.isFlexible(), a.getDuration(), a.getStartDate(), a.getEndDate());
                this.actualActivity--;
            }  
        }
    }
    
    public void setActivityLabels(String l, String n, String loc, String startPlace, Boolean flex, Long duration, Date start,Date end){
        this.actLabel.setText(this.setNewLines(25,l));
        if(n==null)
            this.actNotes.setText("");
        else
            this.actNotes.setText(this.setNewLines(25,n));
        this.actLocation.setText(this.setNewLines(25,loc));
        this.actStart.setText(this.setNewLines(25,startPlace));
        if(flex!=null)
            this.actFlexible.setText(flex.toString());
        else
            this.actFlexible.setText("");
        if(duration==null)
            this.actDuration.setText("");
        else
            this.actDuration.setText(duration.toString()+" minutes");
        if(start==null)
            this.actStartDate.setText("");
        else
            this.actStartDate.setText(start.toString());
        if(end==null)
            this.actEndDate.setText("");
        else
            this.actEndDate.setText(end.toString());
    }
    public void update(){
        updateTags();
        updatePreferences();
        //updateNotifications();
    }
    protected void updateNotifications(){
        TilePane notPane=(TilePane) this.thisStage.getScene().lookup("#101");
        Label title= (Label) this.thisStage.getScene().lookup("#102");
        notPane.getChildren().clear();
        notPane.getChildren().add(title);
        for(int i=0;i<Data.getUser().getNotifications().size();i++)
            notPane.getChildren().add(new Label(this.setNewLines(50,Data.getUser().getNotifications().get(i).toString())));
    }
    private void updateTags(){
        ArrayList<FavouritePosition> favPos=Data.getUser().getFavPositions();
        ObservableList<FavouritePosition> list=FXCollections.observableArrayList(favPos);
        chosenTag.setItems(list);
        this.actNewLocationTag.setItems(list);
        this.actNewStartTag.setItems(list);
        this.actUpdateLocationTag.setItems(list);
        this.actUpdateStartTag.setItems(list);
        selectedTagAddress.setText("");
    }
    private void updatePreferences(){
        BooleanPreferencesSet boolPrefs= Data.getUser().getBoolPreferences();
        int mValue=boolPrefs.mode().getValue();
        if(mValue==Integer.parseInt(norm.getId()))
            norm.setSelected(true);
        else if(mValue==Integer.parseInt(carb.getId()))
            carb.setSelected(true);
        else if(mValue==Integer.parseInt(time.getId()))
            time.setSelected(true);
        else if(mValue==Integer.parseInt(cost.getId()))
            cost.setSelected(true);
        car.setSelected(boolPrefs.personalCar());
        carsharing.setSelected(boolPrefs.carSharing());
        bike.setSelected(boolPrefs.personalBike());
        bikesharing.setSelected(boolPrefs.bikeSharing());
        transport.setSelected(boolPrefs.publicTrasport());
        ubertaxi.setSelected(boolPrefs.uberTaxi());
        ArrayList<RangedPreference> rngPref=Data.getUser().getRangedPreferences();
        maxCar.setText("");
        maxCost.setText("");
        maxTransp.setText("");
        maxWalk.setText("");
        maxBike.setText("");
        for(RangedPreference p:rngPref){
            if(p.getType()==RangedPreferenceType.CAR_TIME_LIMIT)
                maxCar.setText(String.valueOf(p.getValue()));
            else if(p.getType()==RangedPreferenceType.WALKING_TIME_LIMIT)
                maxWalk.setText(String.valueOf(p.getValue()));
            else if(p.getType()==RangedPreferenceType.COST_LIMIT)
                maxCost.setText(String.valueOf(p.getValue()));
            else if(p.getType()==RangedPreferenceType.PUBLIC_TRANSPORT_TIME_LIMIT)
                maxTransp.setText(String.valueOf(p.getValue()));
            else if(p.getType()==RangedPreferenceType.BIKING_TIME_LIMIT)
                maxBike.setText(String.valueOf(p.getValue()));
            
        }
    }
    private void showAlert(String mess){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(mess);
        alert.showAndWait();    
    }
    private String setNewLines(int nChars, String inp){
        String split[]=inp.split(" ");
            String text="";
            for(int i=0,len=0;i<split.length;i++){
                len=len+split[i].length()+1;
                if(len>nChars){
                    text+="\n "+split[i];
                    len=split[i].length();
                }
                else
                    text+=" "+split[i];       
            }
        return text;
    }
    
    private Date localDateToDate(LocalDate ld){
        Date date=new Date(0,0,0,0,0,0);
        date.setYear(ld.getYear()-1900);
        date.setMonth(ld.getMonthValue()-1);
        date.setDate(ld.getDayOfMonth());
        return date;
    }
    private void setUpdateArea(String l,String n,String loc,String start,boolean flexible, String duration, Date startDate, Date endDate){
        if(flexible)
            this.onUpdateSelectBreak();
        else
            this.onUpdateSelectFixed();
        actUpdateLabel.setText(l);
        if(n==null)
            actUpdateNotes.setText("");
        else
            actUpdateNotes.setText(n);
        actUpdateLocation.setText(loc);
        actUpdateLocationTag.setValue(null);
        actUpdateStartTag.setValue(null);
        actUpdateStart.setText(start);
        actUpdateFixed.setSelected(!flexible);
        actUpdateBreak.setSelected(flexible);
        
        if(startDate==null){
            actUpdateStartDatePicker.setValue(null);
            actUpdateEndDatePicker.setValue(null);
            actUpdateStartHour.setValue(null);
            actUpdateStartMinute.setValue(null);
            actUpdateEndHour.setValue(null);
            actUpdateEndMinute.setValue(null);
            actUpdateDuration.setValue(null);
            return;
        }
        actUpdateStartDatePicker.setValue(LocalDate.of(startDate.getYear()+1900, startDate.getMonth()+1, startDate.getDate()));
        actUpdateEndDatePicker.setValue(LocalDate.of(endDate.getYear()+1900, endDate.getMonth()+1, endDate.getDate()));
        actUpdateStartHour.setValue(Integer.toString(startDate.getHours()));
        actUpdateStartMinute.setValue(Integer.toString(startDate.getMinutes()));
        actUpdateEndHour.setValue(Integer.toString(endDate.getHours()));
        actUpdateEndMinute.setValue(Integer.toString(endDate.getMinutes()));
        if(flexible)
            actUpdateDuration.setValue(null);
        else
            actUpdateDuration.setValue(duration);
    }
    
}

class UpdateNotificationsThread extends Thread{
    MainWindowController thisControll;
    private final long ONE_MINUTE=60*1000;
    UpdateNotificationsThread(MainWindowController thisControll){
        this.thisControll=thisControll;
    }
    @Override
    public void run(){
        while(true){
            try{
                ResponseRetrieveNotifications rrn=NetworkLayer.retrieveNotificationsRequest(Data.getUser().getUsername(), Data.getUser().getPassword());
                if(rrn.getType()==ResponseRetrieveNotificationsType.OK){
                    Data.getUser().setNotifications(rrn.getNotifications());
                    Platform.runLater(new UpdateNotificationsTask(thisControll));
                }
                sleep(ONE_MINUTE);
            }catch(IOException e){

            }catch(InterruptedException e){
                break;
            }
        }
    }
}

class UpdateNotificationsTask implements Runnable{
    MainWindowController thisControll;
    UpdateNotificationsTask(MainWindowController thisControll){
        this.thisControll=thisControll;
    }
    @Override
    public void run() {
        thisControll.updateNotifications();
    }
}
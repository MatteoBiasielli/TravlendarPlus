/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendardesktopclient.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import travlendardesktopclient.network.addactivityresponse.ResponseAddActivity;
import travlendardesktopclient.network.addtagresponse.ResponseAddTag;
import travlendardesktopclient.network.deleteactivityresponse.ResponseDeleteActivity;
import travlendardesktopclient.network.deleterangedpreferenceresponse.ResponseDeleteRangedPreferences;
import travlendardesktopclient.network.deletetagresponse.ResponseDeleteTag;
import travlendardesktopclient.network.loginresponse.ResponseLogin;
import travlendardesktopclient.network.registerresponse.ResponseRegister;
import travlendardesktopclient.network.retrievenotificationsresponse.ResponseRetrieveNotifications;
import travlendardesktopclient.network.updateactivityresponse.ResponseUpdateActivity;
import travlendardesktopclient.network.updatebooleanpreferencesresponse.ResponseUpdateBooleanPreferences;
import travlendardesktopclient.network.updaterangedpreferencesresponse.ResponseUpdateRangedPreferences;

/**
 *
 * @author matteo
 */
public class NetworkLayer {
    private static String ip;
    private static final String urlCenter=":8080/travlendar/";
    private static final String urlHead="http://";
    /**
     * Sets the ip to which do the requests
     * @param ip is the ip
     */
    public static void setIP(String ip){
        NetworkLayer.ip=ip;
    }
    
    /**
     * Handles the login HTTP request
     * @param user
     * @param pass
     * @return an object ResponseLogin containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseLogin loginRequest(String user, String pass) throws IOException{
        return parseResponseLogin(requestHTTP(loginURLBuilder(user,pass)));
    }
    /**builds the URL for the login request
     * 
     * @param user
     * @param pass
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL loginURLBuilder(String user, String pass) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"login?";
        url+="user="+user;
        url+="&pass="+pass;
        return new URL(url);
    }
    
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseLogin object that corresponds to the given json
     */
    private static ResponseLogin parseResponseLogin(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseLogin.class);
    }
    
    /**
     * Handles the resistration HTTP request
     * @param user
     * @param pass
     * @return an object ResponseRegister containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseRegister registerRequest(String user, String pass) throws IOException {
        return parseResponseRegister(requestHTTP(registerURLBuilder(user,pass)));
    }
    /**builds the URL for the registration request
     * 
     * @param user
     * @param pass
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL registerURLBuilder(String user, String pass) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"register?";
        url+="user="+user;
        url+="&pass="+pass;
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseRegister object that corresponds to the given json
     */
    private static ResponseRegister parseResponseRegister(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseRegister.class);
    }
    
    
    /**
     * Handles the delete tag HTTP request
     * @param user
     * @param pass
     * @param tag is the tag to delete
     * @return an object ResponseDeleteTag containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseDeleteTag deleteTagRequest(String user, String pass,String tag) throws IOException {
        return parseResponseDeleteTag(requestHTTP(deleteTagURLBuilder(user,pass,tag)));
    }
    /**builds the URL for the delete tag request
     * @param user
     * @param pass
     * @param tag is the tag to delete
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL deleteTagURLBuilder(String user, String pass,String tag) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"deletetag?";
        url+="user="+user;
        url+="&pass="+pass;
        url+="&tag="+tag;
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseDeleteTag object that corresponds to the given json
     */
    private static ResponseDeleteTag parseResponseDeleteTag(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseDeleteTag.class);
    }
    
    /**
     * Handles the add tag HTTP request
     * @param user
     * @param pass
     * @param tag is the tag to add
     * @param addr is the address the tag corresponds to
     * @return an object ResponseAddTag containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseAddTag addTagRequest(String user, String pass,String tag,String addr) throws IOException {
        return parseResponseAddTag(requestHTTP(addTagURLBuilder(user,pass,tag,addr)));
    }
    /**builds the URL for the add tag request
     * @param user
     * @param pass
     * @param tag is the tag to add
     * @param addr is the address the tag corresponds to
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL addTagURLBuilder(String user, String pass,String tag, String addr) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"addtag?";
        addr=addr.replace(" ", "%20");
        url+="user="+user;
        url+="&pass="+pass;
        url+="&tag="+tag;
        url+="&addr="+addr;
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseAddTag object that corresponds to the given json
     */
    private static ResponseAddTag parseResponseAddTag(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseAddTag.class);
    }
    
    /**
     * Handles the update boolean preferences HTTP request
     * @param user
     * @param pass
     * @param pC personalCar
     * @param cS carSharing
     * @param pB personalBike
     * @param bS bikeSharing
     * @param pT public transport
     * @param uT ubet/taxi
     * @param mode usage modality
     * @return an object ResponseUpdateBooleanPreferences containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseUpdateBooleanPreferences updateBooleanPreferencesRequest(String user, String pass,boolean pC,boolean cS,boolean pB,boolean bS, boolean pT, boolean uT, int mode) throws IOException {
        return parseResponseUpdateBooleanPreferences(requestHTTP(updateBooleanPreferencesURLBuilder(user,pass,pC,cS,pB,bS,pT,uT,mode)));
    }
    /**builds the URL for the update boolean preferences request
     * @param user
     * @param pass
     * @param pC personalCar
     * @param cS carSharing
     * @param pB personalBike
     * @param bS bikeSharing
     * @param pT public transport
     * @param uT ubet/taxi
     * @param mode usage modality
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL updateBooleanPreferencesURLBuilder(String user, String pass,boolean pC,boolean cS,boolean pB,boolean bS, boolean pT, boolean uT, int mode) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"updatebooleanpreferences?";
        url+="user="+user;
        url+="&pass="+pass;
        url+="&pC="+pC;
        url+="&cS="+cS;
        url+="&pB="+pB;
        url+="&bS="+bS;
        url+="&pT="+pT;
        url+="&uT="+uT;
        url+="&m="+mode;
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseUpdateBooleanPreferences object that corresponds to the given json
     */
    private static ResponseUpdateBooleanPreferences parseResponseUpdateBooleanPreferences(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseUpdateBooleanPreferences.class);
    }
    
    
    /**
     * Handles the update ranged preferences HTTP request
     * @param user
     * @param pass
     * @param vals are the values
     * @param ids are the ids of the preferences corresponding to the values
     * @return an object ResponseUpdateRangedPreferences containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseUpdateRangedPreferences updateRangedPreferencesRequest(String user, String pass,ArrayList<Integer> vals, ArrayList<String> ids) throws IOException {
        return parseResponseUpdateRangedPreferences(requestHTTP(updateRangedPreferencesURLBuilder(user,pass,vals,ids)));
    }
    /**builds the URL for the update ranged preferences request
     * @param user
     * @param pass
     * @param vals are the values
     * @param ids are the ids of the preferences corresponding to the values
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL updateRangedPreferencesURLBuilder(String user, String pass,ArrayList<Integer> vals, ArrayList<String> ids) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"updaterangedpreferences?";
        url+="user="+user;
        url+="&pass="+pass;
        for(int i=0;i<vals.size();i++){
            url+="&pref="+ids.get(i);
            url+="&val="+vals.get(i);
        }
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseUpdateRangedPreferences object that corresponds to the given json
     */
    private static ResponseUpdateRangedPreferences parseResponseUpdateRangedPreferences(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseUpdateRangedPreferences.class);
    }
    
    /**
     * Handles the delete ranged preferences HTTP request
     * @param user
     * @param pass
     * @param ids are the ids of the preferences corresponding to the values
     * @return an object ResponseDeleteRangedPreferences containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseDeleteRangedPreferences deleteRangedPreferencesRequest(String user, String pass, ArrayList<String> ids) throws IOException {
        return parseResponseDeleteRangedPreferences(requestHTTP(deleteRangedPreferencesURLBuilder(user,pass,ids)));
    }
    /**builds the URL for the delete ranged preferences request
     * @param user
     * @param pass
     * @param ids are the ids of the preferences corresponding to the values
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL deleteRangedPreferencesURLBuilder(String user, String pass, ArrayList<String> ids) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"deleterangedpreferences?";
        url+="user="+user;
        url+="&pass="+pass;
        for(int i=0;i<ids.size();i++){
            url+="&pref="+ids.get(i);
        }
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseDeleteRangedPreferences object that corresponds to the given json
     */
    private static ResponseDeleteRangedPreferences parseResponseDeleteRangedPreferences(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseDeleteRangedPreferences.class);
    }
    
    
    /**
     * Handles the delete activity HTTP request
     * @param user
     * @param pass
     * @param id are the id activity to delete
     * @return an object ResponseDeleteActivity containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseDeleteActivity deleteActivityRequest(String user, String pass, String id) throws IOException {
        return parseResponseDeleteActivity(requestHTTP(deleteActivityURLBuilder(user,pass,id)));
    }
    /**builds the URL for the delete activity request
     * @param user
     * @param pass
     * @param id are the id activity to delete
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL deleteActivityURLBuilder(String user, String pass, String id) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"deleteactivity?";
        url+="user="+user;
        url+="&pass="+pass;
        url+="&activityid="+id;
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseDeleteActivity object that corresponds to the given json
     */
    private static ResponseDeleteActivity parseResponseDeleteActivity(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseDeleteActivity.class);
    }
    
    
    /**
     * Handles the add activity HTTP request
     * @param user
     * @param pass
     * @param l
     * @param n
     * @param locText
     * @param locTag
     * @param startText
     * @param startTag
     * @param flexible
     * @param duration
     * @param st
     * @param en
     * @return an object ResponseAddActivity containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseAddActivity addActivityRequest(String user, String pass, String l,String n,String locText,String locTag,String startText,String startTag, Boolean flexible,String duration,Date st,Date en) throws IOException {
        return parseResponseAddActivity(requestHTTP(addActivityURLBuilder(user,pass, l, n, locText, locTag, startText, startTag,  flexible, duration, st, en)));
    }
    /**builds the URL for the add activity request
     * @param user
     * @param pass
     * @param l
     * @param n
     * @param locText
     * @param locTag
     * @param startText
     * @param startTag
     * @param flexible
     * @param duration
     * @param st
     * @param en
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL addActivityURLBuilder(String user, String pass, String l,String n,String locText,String locTag,String startText,String startTag, Boolean flexible,String duration,Date st,Date en) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"addactivity?";
        l=l.replace(" ", "%20");
        n=n.replace(" ", "%20");
        locText=locText.replace(" ", "%20");
        startText=startText.replace(" ", "%20");
        url+="user="+user;
        url+="&pass="+pass;
        url+="&label="+l;
        url+="&notes="+n;
        if(!"".equals(locTag))
            url+="&locationtag="+locTag;
        else
            url+="&locationtext="+locText;
        if(!"".equals(startTag))
            url+="&starttag="+startTag;
        else
            url+="&starttext="+startText;
        url+="&flexible="+flexible.toString();
        url+="&duration="+duration;
        url+="&start="+st.getTime();
        url+="&end="+en.getTime();
        
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseAddActivity object that corresponds to the given json
     */
    private static ResponseAddActivity parseResponseAddActivity(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseAddActivity.class);
    }
    
    /**
     * Handles the update activity HTTP request
     * @param user
     * @param pass
     * @param l
     * @param n
     * @param locText
     * @param locTag
     * @param startText
     * @param startTag
     * @param flexible
     * @param duration
     * @param st
     * @param en
     * @param id
     * @return an object ResponseUpdateActivity containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseUpdateActivity updateActivityRequest(String user, String pass, String l,String n,String locText,String locTag,String startText,String startTag, Boolean flexible,String duration,Date st,Date en, int id) throws IOException {
        return parseResponseUpdateActivity(requestHTTP(updateActivityURLBuilder(user,pass, l, n, locText, locTag, startText, startTag,  flexible, duration, st, en,id)));
    }
    /**builds the URL for the update activity request
     * @param user
     * @param pass
     * @param l
     * @param n
     * @param locText
     * @param locTag
     * @param startText
     * @param startTag
     * @param flexible
     * @param duration
     * @param st
     * @param en
     * @param id
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL updateActivityURLBuilder(String user, String pass, String l,String n,String locText,String locTag,String startText,String startTag, Boolean flexible,String duration,Date st,Date en,int id) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"updateactivity?";
        l=l.replace(" ", "%20");
        n=n.replace(" ", "%20");
        locText=locText.replace(" ", "%20");
        startText=startText.replace(" ", "%20");
        url+="user="+user;
        url+="&pass="+pass;
        url+="&label="+l;
        url+="&notes="+n;
        if(!"".equals(locTag))
            url+="&locationtag="+locTag;
        else
            url+="&locationtext="+locText;
        if(!"".equals(startTag))
            url+="&starttag="+startTag;
        else
            url+="&starttext="+startText;
        url+="&flexible="+flexible.toString();
        url+="&duration="+duration;
        url+="&start="+st.getTime();
        url+="&end="+en.getTime();
        url+="&activityid="+id;
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseUpdateActivity object that corresponds to the given json
     */
    private static ResponseUpdateActivity parseResponseUpdateActivity(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseUpdateActivity.class);
    }
    
    
    /**
     * Handles the retrieve notification HTTP request
     * @param user
     * @param pass
     * @return an object ResponseRetrieveNotifications containing all the data requested and the
     * final status of the request
     * @throws IOException if a connection error occurs
     */
    public static ResponseRetrieveNotifications retrieveNotificationsRequest(String user, String pass) throws IOException{
        return parseRetrieveNotifications(requestHTTP(retrieveNotificationsURLBuilder(user,pass)));
    }
    /**builds the URL for the retrieve notification request
     * 
     * @param user
     * @param pass
     * @return a URL object contanining the given parameters
     * @throws MalformedURLException is the URL is invalid
     */
    private static URL retrieveNotificationsURLBuilder(String user, String pass) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"retrievenotifications?";
        url+="user="+user;
        url+="&pass="+pass;
        return new URL(url);
    }
    /**given a json code, parses it to the coresponding object
     * 
     * @param json is the json code
     * @return a ResponseRetrieveNotifications object that corresponds to the given json
     */
    private static ResponseRetrieveNotifications parseRetrieveNotifications(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseRetrieveNotifications.class);
    }
    
    
    /**
     * private method that executes the request and returns the JSON code expected in return
     * @param url is the already formatted url that has to be used to build the request 
     * @return the JSON code containing result
     * @throws IOException
     */
    private static String requestHTTP(URL url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = read.readLine();
        String json = "";
        while(line!=null) {
            json += line;
            line = read.readLine();
        }
        return json;
    }

    
}

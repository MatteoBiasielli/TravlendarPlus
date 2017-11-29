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
import travlendardesktopclient.network.updatebooleanpreferencesresponse.ResponseUpdateBooleanPreferences;
import travlendardesktopclient.network.updaterangedpreferencesresponse.ResponseUpdateRangedPreferences;

/**
 *
 * @author matteo
 */
public class NetworkLayer {
    private static String ip;
    private static final String urlCenter=":8080/travlendar/";
    private static final String urlHead="http://";;
    public static void setIP(String ip){
        NetworkLayer.ip=ip;
    }
    public static ResponseLogin loginRequest(String user, String pass) throws IOException{
        return parseResponseLogin(requestHTTP(loginURLBuilder(user,pass)));
    }
    private static URL loginURLBuilder(String user, String pass) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"login?";
        url+="user="+user;
        url+="&pass="+pass;
        return new URL(url);
    }
    private static ResponseLogin parseResponseLogin(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseLogin.class);
    }
    
    public static ResponseRegister registerRequest(String user, String pass) throws IOException {
        return parseResponseRegister(requestHTTP(registerURLBuilder(user,pass)));
    }
    private static URL registerURLBuilder(String user, String pass) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"register?";
        url+="user="+user;
        url+="&pass="+pass;
        return new URL(url);
    }
    private static ResponseRegister parseResponseRegister(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseRegister.class);
    }
    
    public static ResponseDeleteTag deleteTagRequest(String user, String pass,String tag) throws IOException {
        return parseResponseDeleteTag(requestHTTP(deleteTagURLBuilder(user,pass,tag)));
    }
    private static URL deleteTagURLBuilder(String user, String pass,String tag) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"deletetag?";
        url+="user="+user;
        url+="&pass="+pass;
        url+="&tag="+tag;
        return new URL(url);
    }
    private static ResponseDeleteTag parseResponseDeleteTag(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseDeleteTag.class);
    }
    
    public static ResponseAddTag addTagRequest(String user, String pass,String tag,String addr) throws IOException {
        return parseResponseAddTag(requestHTTP(addTagURLBuilder(user,pass,tag,addr)));
    }
    private static URL addTagURLBuilder(String user, String pass,String tag, String addr) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"addtag?";
        addr=addr.replace(" ", "%20");
        url+="user="+user;
        url+="&pass="+pass;
        url+="&tag="+tag;
        url+="&addr="+addr;
        return new URL(url);
    }
    private static ResponseAddTag parseResponseAddTag(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseAddTag.class);
    }
    
    public static ResponseUpdateBooleanPreferences updateBooleanPreferencesRequest(String user, String pass,boolean pC,boolean cS,boolean pB,boolean bS, boolean pT, boolean uT, int mode) throws IOException {
        return parseResponseUpdateBooleanPreferences(requestHTTP(updateBooleanPreferencesURLBuilder(user,pass,pC,cS,pB,bS,pT,uT,mode)));
    }
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
    private static ResponseUpdateBooleanPreferences parseResponseUpdateBooleanPreferences(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseUpdateBooleanPreferences.class);
    }
    
    public static ResponseUpdateRangedPreferences updateRangedPreferencesRequest(String user, String pass,ArrayList<Integer> vals, ArrayList<String> ids) throws IOException {
        return parseResponseUpdateRangedPreferences(requestHTTP(updateRangedPreferencesURLBuilder(user,pass,vals,ids)));
    }
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
    private static ResponseUpdateRangedPreferences parseResponseUpdateRangedPreferences(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseUpdateRangedPreferences.class);
    }
    
    
    public static ResponseDeleteRangedPreferences deleteRangedPreferencesRequest(String user, String pass, ArrayList<String> ids) throws IOException {
        return parseResponseDeleteRangedPreferences(requestHTTP(deleteRangedPreferencesURLBuilder(user,pass,ids)));
    }
    private static URL deleteRangedPreferencesURLBuilder(String user, String pass, ArrayList<String> ids) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"deleterangedpreferences?";
        url+="user="+user;
        url+="&pass="+pass;
        for(int i=0;i<ids.size();i++){
            url+="&pref="+ids.get(i);
        }
        return new URL(url);
    }
    private static ResponseDeleteRangedPreferences parseResponseDeleteRangedPreferences(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseDeleteRangedPreferences.class);
    }
    
    public static ResponseDeleteActivity deleteActivityRequest(String user, String pass, String id) throws IOException {
        return parseResponseDeleteActivity(requestHTTP(deleteActivityURLBuilder(user,pass,id)));
    }
    private static URL deleteActivityURLBuilder(String user, String pass, String id) throws MalformedURLException{
        String url=urlHead+ip+urlCenter+"deleteactivity?";
        url+="user="+user;
        url+="&pass="+pass;
        url+="&activityid="+id;
        return new URL(url);
    }
    private static ResponseDeleteActivity parseResponseDeleteActivity(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseDeleteActivity.class);
    }
    
    

    public static ResponseAddActivity addActivityRequest(String user, String pass, String l,String n,String locText,String locTag,String startText,String startTag, Boolean flexible,String duration,Date st,Date en) throws IOException {
        return parseResponseAddActivity(requestHTTP(addActivityURLBuilder(user,pass, l, n, locText, locTag, startText, startTag,  flexible, duration, st, en)));
    }
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
    private static ResponseAddActivity parseResponseAddActivity(String json){
        Gson gson= new GsonBuilder().create();
        return gson.fromJson(json, ResponseAddActivity.class);
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

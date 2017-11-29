/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travlendarplus.notification.weather;

/**
 *
 * @author matteo
 */
public class Forecast {
    private final String dayOfTheWeek;
    private final int maxTemp;
    private final int minTemp;
    private final String weatherText;
    private final String date;
    
    public Forecast(String d, int ma, int mi, String w, String dat){
        this.dayOfTheWeek=d;
        this.maxTemp=ma;
        this.minTemp=mi;
        this.weatherText=w;
        this.date=dat;
    }
    
    public String getDay(){
        return this.dayOfTheWeek;
    }
    public String getWeatherText(){
        return this.weatherText;
    }
    public String getDate(){
        return this.date;
    }
    public int getMaxTemp(){
        return this.maxTemp;
    }
    public int getMinTemp(){
        return this.minTemp;
    }
    
    @Override
    public String toString(){
        return date + " - " + weatherText;
    }
}
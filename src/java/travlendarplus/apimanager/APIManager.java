package travlendarplus.apimanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import travlendarplus.exceptions.*;
import travlendarplus.notification.weather.Forecast;
import travlendarplus.travel.*;

public class APIManager {
    /**Our Google API directions key **/
    private static final String GOOGLE_DIRECTIONS_KEY="AIzaSyD4fwbgFzeyR1QwFAWDkxr9VPhEIu4asb4";
    /**Our Google API geocoding key **/
    private static final String GOOGLE_GEOCODING_KEY="AIzaSyAwj27Z9huHhxLe1LWW5-gzx8iOcJf78QE";

    /**
     * Public method that masks the http request that is done to the Google Directions service to get the travel options.
     * @param origin is the starting point of the path
     * @param destination is the ending point of the path
     * @param mode is the enumeration expressing the travel modality
     * @param alternatives indicates if more alternatives for the same path are requested
     * @param lang indicates the language in which html instructions has to be requested
     * @return a Route object containing all the required information about the path
     * @throws IOException 
     * @throws NoPathFoundException if no path between origin and destination has been found
     * @throws JSONException 
     */
    public static Route googleDirectionsRequest(String origin, String destination, TravelMode mode,boolean alternatives, Language lang) throws IOException, NoPathFoundException{
        origin=origin.replace(' ', '+');	
        destination=destination.replace(' ', '+');	
        URL url= urlCreatorDirections(origin,destination,mode,alternatives,lang);
        String json=requestHTTP(url);
        Route ris= parseJSonGoogleDirections(json);
        ris.setMode(mode);
        return ris;
    }


    /**
     * Public method that masks the http request that is done to the Google geocoding service
     * @param address is the address to convert into position
     * @returns an object Position containing latitude and longitude
     * @throws InvalidAddressException if the inserted address doesn't correspond to any set of coordinates
     */
    public static Position googleGeocodingRequest(String address) throws IOException, InvalidAddressException{
        address=address.replace(' ', '+');		
        URL url= urlCreatorGeocoding(address);
        String json=requestHTTP(url);
        return parseJSonGoogleGeocoding(json);
    }

    /**
     * Public method that masks the http request that is done to the Google reverse geocoding service
     * @param p is the position to convert into address
     * @returns a String object representing the formatted complete address
     * @throws InvalidPositionException if the given position doesn't correspond to any address
     */
    public static String googleReverseGeocodingRequest(Position p)  throws InvalidPositionException{
        try{
            URL url= urlCreatorReverseGeocoding(p);
            String json=requestHTTP(url);
            return parseJSonGoogleReverseGeocoding(json);
        }catch(IOException e){
            throw new InvalidPositionException("The position is invalid");
        }
    }
    
    /**
     * Public method that masks the http request that is done to the Yahoo!Weather service
     * @param city is the city for which to get the weather forecasts
     * @return an ArraList<Forecast> object representing the forecasts for the 
     * present and next days.
     * @throws InvalidInputException if forecasts were not available or the given location is invalid.
     */
    public static ArrayList<Forecast> getYahooWeatherForcast(String city) throws InvalidInputException{
        try{
            city=city.replace(' ', '+');
            URL url= urlCreatorYahooWeather(city);
            String json=requestHTTP(url);
            return parseYahooWeatherResponse(json);
        }catch(IOException e){
            throw new InvalidInputException("The city is invalid");
        }
    }
    
    /**
     * Given the JSON code that is the result of a request to the Yahoo!Weather service, reads the data.
     * @param json is the JSON code
     * @return an ArraList<Forecast> object representing the forecast for the 
     * present and next days.
     * @throws IOException if forecasts were not available or the given location is invalid.
     */
    private static ArrayList<Forecast> parseYahooWeatherResponse(String json) throws IOException {
        ArrayList<Forecast> ris= new ArrayList<>();
        JSONObject jsonData = new JSONObject(json);
        try{
        JSONObject result= jsonData.getJSONObject("query").getJSONObject("results");
        JSONObject item=result.getJSONObject("channel").getJSONObject("item");
        JSONArray forecasts=item.getJSONArray("forecast");
        for(int i=0; i<forecasts.length();i++){
            String date=forecasts.getJSONObject(i).getString("date");
            String text=forecasts.getJSONObject(i).getString("text");
            String day=forecasts.getJSONObject(i).getString("day");
            int max=Integer.parseInt(forecasts.getJSONObject(i).getString("high"));
            int min=Integer.parseInt(forecasts.getJSONObject(i).getString("low"));
            ris.add(new Forecast(day,max,min,text,date));
        }
        }catch(JSONException e){
            throw new IOException();
        }
        return ris;
    }
    
    /**
     * Given the JSON code that is the result of a request to the Google ReverseGeocoding Service, reads the data.
     * @param json is the JSON code
     * @return a String object representing the formatted complete address
     * @throws JSONException
     * @throws InvalidPositionException if the json code contains an invalid result
     */
    private static String parseJSonGoogleReverseGeocoding(String json) throws InvalidPositionException {
        String status=new JSONObject(json).getString("status");
        if(!"OK".equals(status))
            throw new InvalidPositionException("The position is invalid");
        JSONObject jsonData = new JSONObject(json).getJSONArray("results").getJSONObject(0);
        return jsonData.getString("formatted_address");
    }


    /**
     * Given the JSON code that is the result of a request to the Google Geocoding Service, reads the data.
     * @param json is the JSON code
     * @return a Position object representing a set (longitude,latitude)
     * @throws JSONException
     * @throws InvalidAddressException if the json code contains an invalid result
     */
    private static Position parseJSonGoogleGeocoding(String json) throws InvalidAddressException {
        Position ris;
        double lat;
        double lng;	
        String status=new JSONObject(json).getString("status");
        if(!"OK".equals(status))
            throw new InvalidAddressException("The address is invalid");
        JSONObject jsonData = new JSONObject(json).getJSONArray("results").getJSONObject(0);
        lat=jsonData.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
        lng=jsonData.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
        ris=new Position(lat,lng);
            return ris;
    }



    /**
     * private method that executes the request to a Google service and returns the JSON code expected in return
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

    /**
     * formats the URL for the request to the Google directions service, basing on the parameters
     * @param origin is the starting point of the path
     * @param destination is the ending point of the path
     * @param mode is the enumeration expressing the travel modality
     * @param alternatives indicates if more alternatives for the same path are requested
     * @param lang indicates the language in which html instructions has to be requested
     * @return an URL object representing the url
     * @throws MalformedURLException
     */
    private static URL urlCreatorDirections(String origin, String destination, TravelMode mode,boolean alternatives,Language lang) throws MalformedURLException{
        String url="https://maps.googleapis.com/maps/api/directions/json?";
        url=url+"mode="+ mode.getMode();
        url=url+"&origin="+origin;
        url=url+"&destination="+destination;
        url=url+"&alternatives="+Boolean.toString(alternatives);
        url=url+"&language="+lang.getLang();
        url=url+"&key="+GOOGLE_DIRECTIONS_KEY;
        System.out.println(url);
        return new URL(url);
    }

    /**
     * Given the JSON code that is the result of a request to the Google Direction Service, reads the data.
     * @param data is the JSON code
     * @return a Route Object containing all the required information about the path
     * @throws JSONException
     * @throws NoPathFoundException if no path between origin and destination has been found
     */
    private static Route parseJSonGoogleDirections(String data) throws NoPathFoundException {
        Route res=new Route();
        JSONObject jsonData = new JSONObject(data);
        String status=jsonData.getString("status");
        if(!"OK".equals(status))
            throw new NoPathFoundException("No path for the selected route was found.");
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONArray jsonLegs = jsonRoutes.getJSONObject(i).getJSONArray("legs");
            Leg leg;
            for (int j = 0; j < jsonLegs.length();j++) {
                String s=jsonLegs.getJSONObject(j).getString("start_address");
                String e=jsonLegs.getJSONObject(j).getString("end_address");
                String d=jsonLegs.getJSONObject(j).getJSONObject("duration").getString("text");
                int dur=jsonLegs.getJSONObject(j).getJSONObject("duration").getInt("value");
                ArrayList<Step> steps=new ArrayList<>();
                JSONArray jsonSteps= jsonLegs.getJSONObject(j).getJSONArray("steps");
                for(int z=0; z<jsonSteps.length();z++){
                    String dt=jsonSteps.getJSONObject(z).getJSONObject("distance").getString("text");
                    int dv=jsonSteps.getJSONObject(z).getJSONObject("distance").getInt("value");
                    String tt=jsonSteps.getJSONObject(z).getJSONObject("duration").getString("text");
                    int tv=jsonSteps.getJSONObject(z).getJSONObject("duration").getInt("value");
                    String inst=jsonSteps.getJSONObject(z).getString("html_instructions");
                    String mode=jsonSteps.getJSONObject(z).getString("travel_mode");
                    Step st;
                    if("TRANSIT".equals(mode)){
                        String arrival=jsonSteps.getJSONObject(z).getJSONObject("transit_details").getJSONObject("arrival_stop").getString("name");
                        int nStops=jsonSteps.getJSONObject(z).getJSONObject("transit_details").getInt("num_stops");
                        st=new TransitStep(dt,dv,tt,tv,inst,nStops,arrival);
                        steps.add(st);
                    }
                    else{
                        st=new Step(dt,dv,tt,tv,inst);
                        steps.add(st);
                    }
                    st.setMode(TravelMode.valueOf(mode));
                }
                leg=new Leg(s,e,d,dur,steps);
                res.getLegs().add(leg);
            }
        }
        return res;
    }

    /**
     * formats the URL for the request to the Google Geocoding service, basing on the parameters
     * @param address is the address to add to the url for the "address=" parameter
     * @return an URL object representing the url
     */
    private static URL urlCreatorGeocoding(String address) throws MalformedURLException{
        return new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&key="+GOOGLE_GEOCODING_KEY);
    }

    /**
     * formats the URL for the request to the Google ReverseGeocoding service, basing on the parameters
     * @param p is the Position object from which to extract the data to add to the url for the "latlng=" parameter
     * @return an URL object representing the url
     */
    private static URL urlCreatorReverseGeocoding(Position p) throws MalformedURLException{
        return new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+p.getLatitude()+","+p.getLongitude()+"&key="+GOOGLE_GEOCODING_KEY);
    }
    
    /**
     * formats the URL for the request to the Yahoo!Weather service, basing on the parameters
     * @param citiName is the city
     * @return an URL object representing the url
     */
    private static URL urlCreatorYahooWeather(String cityName) throws MalformedURLException{
        return new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D\""+cityName+"\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
    }

    
    
}

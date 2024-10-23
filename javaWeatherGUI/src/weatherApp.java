import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class weatherApp{
    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude="+latitude+"&longitude="
                +longitude+"&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=Asia%2FSingapore";
        try{
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200){
                System.out.println("Unsuccessful API connection");
                return null;
            }
            StringBuilder resJson = new StringBuilder();
            Scanner sc = new Scanner(conn.getInputStream());
            while(sc.hasNext()){
                resJson.append(sc.nextLine());
            }
            sc.close();
            conn.disconnect();
            JSONParser parser = new JSONParser();
            JSONObject resJsonObj = (JSONObject) parser.parse(String.valueOf(resJson));
            JSONObject hourly = (JSONObject) resJsonObj.get("hourly");
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexTime(time);
            JSONArray tempData = (JSONArray) hourly.get("temperature_2m");
            double temp = (double) tempData.get(index);
            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index));
            JSONArray relHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relHumidity.get(index);
            JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windSpeed = (double) windSpeedData.get(index);

            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temp);
            weatherData.put("weather_Condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windSpeed", windSpeed);
            return weatherData;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static JSONArray getLocationData(String locationName){
        locationName = locationName.replaceAll(" ", "+");
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name="+locationName+"&count=10&language=en&format=json";
        try{
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200){
                System.out.println("API Connection unsucceesful");
                return null;
            }else{
                StringBuilder resJson = new StringBuilder();
                Scanner sc = new Scanner(conn.getInputStream());
                while (sc.hasNext()){
                    resJson.append(sc.nextLine());
                }
                sc.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resJsonObj = (JSONObject) parser.parse(String.valueOf(resJson));
                JSONArray locationData = (JSONArray) resJsonObj.get("results");
                return locationData;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static HttpURLConnection fetchApiResponse(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        }catch(IOException e ){
            e.printStackTrace();
        }
        return null;
    }
    private static int findIndexTime(JSONArray timeList){
        String currTime = getCurrTime();
        for (int i=0;i<timeList.size();i++){
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currTime)){
                return i;
            }
        }
        return 0;
    }
    public static String getCurrTime(){
        LocalDateTime currDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        String formattedDnT = currDateTime.format(formatter);
        return formattedDnT;
    }
    private static String convertWeatherCode(long weatherCode) {
        String weatherCond = "";
        if (weatherCode == 0L) {
            weatherCond = "Clear";
        } else if (weatherCode <= 3L && weatherCode > 0L) {
            weatherCond = "Cloudy";
        } else if (weatherCode >= 51L && weatherCode <= 67L || weatherCode >= 80L && weatherCode <= 82L) {
            weatherCond = "Rain";
        } else if (weatherCode >= 71L && weatherCode <= 77L) {
            weatherCond = "Snow";
        }
        return weatherCond;
    }
}
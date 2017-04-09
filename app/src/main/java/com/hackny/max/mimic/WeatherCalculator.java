package com.hackny.max.mimic;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class WeatherCalculator extends AsyncTask<String, Void, MainActivity.WeatherTaskData> {
    private static String originalString;
    private static String originalString1;
    private static String originalString2;
    public String location;

    @Override
    protected MainActivity.WeatherTaskData doInBackground(String... params){
        int[] results = new int[2];
        try{
            results = getWeather(params[0].toString());
        } catch(Exception e) {
            e.printStackTrace();
        }

        MainActivity.WeatherTaskData wtd = new MainActivity.WeatherTaskData(results[0], results[1], false, false);
        return wtd;
    }

    public void setLocation(String loc) throws IOException{
        if(loc.equals("Iceland")){
            location = "http://www.accuweather.com/en/us/juneau-ak/99801/hourly-weather-forecast/331728";
        }
        else{
            location = "http://www.accuweather.com/en/us/new-york-ny/10007/hourly-weather-forecast/349727";
        }
    }

    public int[] getWeather(String loc) throws IOException{
        ArrayList<String> realFeel = new ArrayList<String>();
        ArrayList<String> rain = new ArrayList<String>();
        ArrayList<String> snow = new ArrayList<String>();
        ArrayList<String> humidity = new ArrayList<String>();
        ArrayList<String> windiness = new ArrayList<String>();
        ArrayList<String> type = new ArrayList<String>();
        ArrayList<String> time = new ArrayList<String>();

        setLocation(loc);
        originalString = getUrlSource(location);
        int thirdIndex9 = originalString.indexOf("<div>");
        String stringType9 = originalString.substring(thirdIndex9+5, thirdIndex9+8);
        Integer currentTime = 0;
        for(int p = 0; p < stringType9.length(); p++){
            if(stringType9.substring(1, 2 ).equals("a")){
                currentTime = Integer.valueOf(stringType9.substring(0, 1));
            }else if(stringType9.substring(1, 2).equals("p")){
                currentTime = Integer.valueOf(stringType9.substring(0, 1))+12;
            }
            else{
                currentTime = Integer.valueOf(time.get(p).substring(0, 2));
            }
        }

        originalString1 = getUrlSource(location+"?hour="+(currentTime+8));
        originalString2 = getUrlSource(location+"?hour="+(currentTime+16));
        //Finds the time
        for(int i = 0; i < 8; i++){
            int thirdIndex = originalString.indexOf("<div>");
            int fourthIndex = originalString.indexOf("</div>");
            String stringType = originalString.substring(thirdIndex+5, thirdIndex+8);
            time.add(stringType);
            originalString = originalString.substring(thirdIndex+8);
        }
        //Finds the time
        for(int i = 0; i < 8; i++){
            int thirdIndex1 = originalString1.indexOf("<div>");
            int fourthIndex1 = originalString1.indexOf("</div>");
            String stringType1 = originalString1.substring(thirdIndex1+5, thirdIndex1+8);
            time.add(stringType1);
            originalString1 = originalString1.substring(thirdIndex1+8);
        }
        //Finds the time
        for(int i = 0; i < 8; i++){
            int thirdIndex2 = originalString2.indexOf("<div>");
            int fourthIndex2 = originalString2.indexOf("</div>");
            String stringType2 = originalString2.substring(thirdIndex2+5, thirdIndex2+8);
            time.add(stringType2);
            originalString2 = originalString2.substring(thirdIndex2+8);
        }
        //Finds the Type
        type = method(type, 1);
        skip();
        //Finds Real Feel
        realFeel = method(realFeel,2);
        //Finds Wind
        windiness = method(windiness,1);
        //Finds Rain
        rain = method(rain,1);
        //Finds Rain
        snow = method(snow,1);
        skip();
        skip();
        //Finds Humidity
        humidity = method(humidity,1);
        skip();
        //Change Time
        ArrayList<Integer> alteredTime = new ArrayList<Integer>();
        for(int p = 0; p < time.size(); p++){
            if(time.get(p).substring(1, 2 ).equals("a")){
                alteredTime.add(Integer.valueOf(time.get(p).substring(0, 1)));
            }else if(time.get(p).substring(1, 2).equals("p")){
                alteredTime.add(Integer.valueOf(time.get(p).substring(0, 1))+12);
            }
            else{
                alteredTime.add(Integer.valueOf(time.get(p).substring(0, 2)));
            }
        }

        //To test between when
        int[] between = new int[2];
        between[0] = 13;
        between[1] = 15;
        Integer temp = 0;
        for(int j = 0; j < realFeel.size(); j++){
            if(between[0] <= Integer.valueOf(alteredTime.get(j))&& Integer.valueOf(alteredTime.get(j))<between[1]){
                temp = temp + Integer.valueOf(realFeel.get(j));
            }
        }
        int size = between[1] - between[0];
        int averagetemp = temp/size;
        int inputSchachere = decision(averagetemp);
        int[] returns = {averagetemp, inputSchachere};
        return returns;
    }

    public static int decision(Integer temp){
        int output = 0;
        if(temp < 20){
            output = 1;
        }else if(temp < 50){
            output = 2;
        }else if(temp < 70){
            output = 3;
        }else if(temp < 90){
            output = 4;
        }else if(temp > 90 ){
            output = 5;
        }
        return output;
    }

    public static ArrayList<String> method(ArrayList<String> type, int counter){
        String stringType;
        String stringType1;
        String stringType2;
        for(int i = 0; i < 8; i++){
            int thirdIndex = originalString.indexOf("<span>");
            int fourthIndex = originalString.indexOf("</span>");
            if(counter == 1){
                stringType = originalString.substring(thirdIndex+6, fourthIndex);
            }
            else{
                stringType = originalString.substring(thirdIndex+6, thirdIndex+8);
            }
            type.add(stringType);
            originalString = originalString.substring(fourthIndex+1);
        }
        for(int i = 0; i < 8; i++){
            int thirdIndex1 = originalString1.indexOf("<span>");
            int fourthIndex1 = originalString1.indexOf("</span>");
            if(counter == 1){
                stringType1 = originalString1.substring(thirdIndex1+6, fourthIndex1);
            }
            else{
                stringType1 = originalString1.substring(thirdIndex1+6, thirdIndex1+8);
            }
            type.add(stringType1);
            originalString1 = originalString1.substring(fourthIndex1+1);
        }
        for(int i = 0; i < 8; i++){
            int thirdIndex2 = originalString2.indexOf("<span>");
            int fourthIndex2 = originalString2.indexOf("</span>");
            if(counter == 1){
                stringType2 = originalString2.substring(thirdIndex2+6, fourthIndex2);
            }
            else{
                stringType2 = originalString2.substring(thirdIndex2+6, thirdIndex2+8);
            }
            type.add(stringType2);
            originalString2 = originalString2.substring(fourthIndex2+1);
        }

        return type;
    }

    public static void skip(){
        for(int i = 0; i < 8; i++){
            int thirdIndex = originalString.indexOf("<span>");
            int fourthIndex = originalString.indexOf("</span>");
            originalString = originalString.substring(fourthIndex+1);
        }
        for(int i = 0; i < 8; i++){
            int thirdIndex1 = originalString1.indexOf("<span>");
            int fourthIndex1 = originalString1.indexOf("</span>");
            originalString1 = originalString1.substring(fourthIndex1+1);
        }
        for(int i = 0; i < 8; i++){
            int thirdIndex2 = originalString2.indexOf("<span>");
            int fourthIndex2 = originalString2.indexOf("</span>");
            originalString2 = originalString2.substring(fourthIndex2+1);
        }
    }


    private static String getUrlSource(String k) throws IOException {
        URL weather = new URL(k);
        URLConnection wea = weather.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(wea.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
        in.close();

        return a.toString();
    }
}

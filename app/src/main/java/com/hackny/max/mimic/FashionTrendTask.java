package com.hackny.max.mimic;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by max on 4/9/17.
 */

public class FashionTrendTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... params){
        try {
            return getFashionTrend();
        } catch(Exception e){
            e.printStackTrace();
            return "Classy";
        }
    }

    public String getFashionTrend() throws Exception{
        String[] positive_adjectives = { "bold", "charming", "clean", "daring", "dazzling", "delicate", "essential",
                "exotic", "flattering", "flowing", "graceful", "cool", "luxurious", "ornate", "savvy", "simple",
                "trendy" };
        String[] fashion_styles = { "bohemian", "contemporary", "chic", "preppy", "punk" };
        URL url = new URL(
                "https://api.nytimes.com/svc/search/v2/articlesearch.json?api_key=6a8be5f111044c88beb8dc8aa2a5a65d&q=fashion");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        String relevant = "";
        while ((line = rd.readLine()) != null) {
            for (String adj : positive_adjectives) {
                if (line.toLowerCase().contains(adj)) {
                    relevant += adj + " ";
                }
            }
            for (String style : fashion_styles) {
                if (line.toLowerCase().contains(style)) {
                    relevant += style + " ";
                }
            }
        }
        rd.close();
        if (connection != null) {
            connection.disconnect();
        }

        return relevant;
    }
}

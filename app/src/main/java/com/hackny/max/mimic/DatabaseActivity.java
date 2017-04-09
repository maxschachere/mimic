package com.hackny.max.mimic;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONSerializers;

import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 4/8/17.
 */

public class DatabaseActivity extends AsyncTask<MainActivity.DatabaseTaskParams, Void, List<Clothes>> {
    @Override
    protected List<Clothes> doInBackground(MainActivity.DatabaseTaskParams... params){
        List<Clothes> clothes = new ArrayList<Clothes>();
        JSONObject jsonResponse = new JSONObject();
        DBCollection clothing = params[0].db.getCollection("clothing");
        int weather_query = params[0].weather;
        BasicDBObject query = new BasicDBObject();
        query.put("weather", weather_query);
        DBCursor cursor = clothing.find(query);
        //DBCursor cursor = clothing.find();
        //Log.d("e", "ASDFJLK");

        try {
            while(cursor.hasNext()) {
                Log.d("e", "ASDFJLK");
                JsonElement jelement = new JsonParser().parse(cursor.next().toString());
                /*Log.d("e", jelement.toString());
                JsonArray jarray = jelement.getAsJsonArray();
                for(int i=0;i<jarray.size();i++){
                    Log.d("e", jarray.get(i).toString());
                }*/
                JsonObject jobject = jelement.getAsJsonObject();
                String name = jobject.get("name").toString().replaceAll("^\"|\"$", "");
                String article = jobject.get("article").toString().replaceAll("^\"|\"$", "");
                boolean rain = jobject.get("rain").getAsBoolean();
                boolean snow = jobject.get("snow").getAsBoolean();
                String[] style = new String[jobject.getAsJsonArray("style").size()];
                for (int i = 0; i<jobject.getAsJsonArray("style").size();i++){
                    style[i] = jobject.getAsJsonArray("style").get(i).toString().replaceAll("^\"|\"$", "");
                }
                String color = jobject.get("color").toString().replaceAll("^\"|\"$", "");
                int weather = jobject.get("weather").getAsInt();
                Clothes clothes_temp = new Clothes(name, article, color, weather, rain, snow, style);
                clothes.add(clothes_temp);
                Log.d("e", clothes_temp.getArticle());
            }
        } finally {
            cursor.close();
        }
        Log.d("E", "sdfsdf");

        return clothes;

    }


}

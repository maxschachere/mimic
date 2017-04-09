package com.hackny.max.mimic;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ParallelScanOptions;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String location = "New York";
        String location = "Iceland";


        DB db = getClothingDatabase();
        //get weather index
        WeatherCalculator wc = new WeatherCalculator();
        int weather = 4;
        int temperature = 50;
        try{
            AsyncTask at_wc = wc.execute(location);
            WeatherTaskData weathera = (WeatherTaskData) at_wc.get();
            temperature = weathera.temperature;
            weather = weathera.index;
            Log.d("e", "TEMPERATURE" + Integer.toString(weather));
        } catch(Exception e){
            weather = 4;
            e.printStackTrace();
        }
        Log.d("e", "TEMPERATURE" + Integer.toString(weather));
        Map<String, Clothes> ot = suggestOutfit(new DatabaseTaskParams(db, weather));
        Log.d("e", ot.get("top").getName().toString());
        Log.d("e", ot.get("bottom").getName().toString());
        Log.d("e", ot.get("shoes").getName().toString());

        FashionTrendTask fashionTrendTask = new FashionTrendTask();
        AsyncTask fashionTrendAsync = fashionTrendTask.execute();
        String fashionTrend;
        try{
            fashionTrend = (String) fashionTrendAsync.get();
        } catch(Exception e){
            fashionTrend = "Classy";
        }
        Log.d("e", fashionTrend);
        Log.d("e", Integer.toString(temperature));

        formatScreen(ot.get("top").getName().toString(), ot.get("bottom").getName().toString(), ot.get("shoes").getName().toString(), Integer.toString(temperature), fashionTrend);
    }

    public void formatScreen(String top, String bottom, String shoes, String temperature, final String fashionTrend){
        // Top
        ImageView topimageView = (ImageView) findViewById(R.id.topImage);
        int topid = getResources().getIdentifier(top, "mipmap", getPackageName());
        topimageView.setImageResource(topid);

        // Shoes
        ImageView shoeimageView = (ImageView) findViewById(R.id.shoeImage);
        int shoeid = getResources().getIdentifier(shoes, "mipmap", getPackageName());
        shoeimageView.setImageResource(shoeid);

        // Pants
        ImageView pantimageView = (ImageView) findViewById(R.id.pantImage);
        int pantid = getResources().getIdentifier(bottom, "mipmap", getPackageName());
        pantimageView.setImageResource(pantid);

        // Weather description
        TextView weatherDetails = (TextView) findViewById(R.id.outsideDesc);
        weatherDetails.setText("No chance of rain");

        // High temp
        TextView high = (TextView) findViewById(R.id.high);
        high.setText(temperature+"°");

        // Low temp
        //TextView low = (TextView) findViewById(R.id.low);
        //low.setText("20"+"°");

        // Casual meetings count
        TextView casualMeetings = (TextView) findViewById(R.id.casual);
        casualMeetings.setText("3");

        // Formal meetings count
        TextView formalMeetings = (TextView) findViewById(R.id.formal);
        formalMeetings.setText("0");


        // Image profile button

        ImageButton profile = (ImageButton) findViewById(R.id.profilebutton);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Pop.class);
                intent.putExtra("FASHION_QUOTE", fashionTrend);
                startActivity(intent);
            }
        });
    }

    public Map<String, Clothes> suggestOutfit(DatabaseTaskParams dtp){
        DatabaseActivity da = new DatabaseActivity();
        Log.d("E", "Test");
        //Clothes tshirt = null;
        Map<String, List<Clothes>> outfit = new HashMap<String, List<Clothes>>();
        AsyncTask a1 = da.execute(dtp);
        List<Clothes> allClothes = new ArrayList<Clothes>();
        List<Clothes> bottoms = new ArrayList<Clothes>();
        List<Clothes> tops = new ArrayList<Clothes>();
        List<Clothes> shoes = new ArrayList<Clothes>();
        try{
            //tshirt = (Clothes) a1.get();
            allClothes = (List<Clothes>) a1.get();
            Log.d("e", allClothes.toString());

            for(Clothes item:allClothes){
                Log.d("e", item.getArticle());
                if(item.getArticle().equals("bottom"))
                    bottoms.add(item);
                else if(item.getArticle().equals("top"))
                    tops.add(item);
                else if(item.getArticle().equals("shoes"))
                    shoes.add(item);
            }

            outfit.put("bottoms", bottoms);
            outfit.put("tops", tops);
            outfit.put("shoes", shoes);
        } catch (Exception e){
            e.printStackTrace();
        }

        Log.d("e", outfit.toString());

        return selectOutfitFromResults(outfit);
    }

    public Map<String, Clothes> selectOutfitFromResults(Map<String, List<Clothes>> map){
        Map<String, Clothes> selectedOutfit = new HashMap<String, Clothes>();
        Random randGen = new Random();
        Log.d("e", Integer.toString(map.get("tops").size()));
        selectedOutfit.put("top", map.get("tops").get(randGen.nextInt(map.get("tops").size())));
        selectedOutfit.put("bottom", map.get("bottoms").get(randGen.nextInt(map.get("bottoms").size())));
        selectedOutfit.put("shoes", map.get("shoes").get(randGen.nextInt(map.get("shoes").size())));
        return selectedOutfit;
    }

    public DB getClothingDatabase(){
        DB db = null;
        MongoClientURI uri = new MongoClientURI(
                "mongodb://max:lhsdzOv0vxNtalR8@cluster0-shard-00-00-ce3jk.mongodb.net:27017,cluster0-shard-00-01-ce3jk.mongodb.net:27017,cluster0-shard-00-02-ce3jk.mongodb.net:27017/<DATABASE>?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin");

        try{
            MongoClient mongoClient = new MongoClient(uri);
            db = mongoClient.getDB("mimic");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return db;
    }

    public static class DatabaseTaskParams{
        DB db;
        int weather;

        DatabaseTaskParams(DB db, int weather){
            this.db = db;
            this.weather = weather;
        }
    }

    public static class WeatherTaskData{
        int temperature;
        int index;
        boolean snow;
        boolean rain;

        WeatherTaskData(int temperature, int index, boolean snow, boolean rain){
            this.temperature = temperature;
            this.index = index;
            this.snow = snow;
            this.rain = rain;
        }
    }

}

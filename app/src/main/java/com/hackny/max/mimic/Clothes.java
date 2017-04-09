package com.hackny.max.mimic;

/**
 * Created by max on 4/8/17.
 */

public class Clothes {
    private String name;
    private String article;
    private String color;
    private int weather;
    private boolean rain;
    private boolean snow;
    private String[] style;


    public Clothes(String name, String article, String color, int weather, boolean rain, boolean snow, String[] style){
        this.name = name;
        this.article = article;
        this.color = color;
        this.weather = weather;
        this.rain = rain;
        this.snow = snow;
        this.style = style;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public void setRain(boolean rain) {
        this.rain = rain;
    }

    public void setSnow(boolean snow) {
        this.snow = snow;
    }

    public void setStyle(String[] style) {
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public String getArticle() {
        return article;
    }

    public String getColor() {
        return color;
    }

    public int getWeather() {
        return weather;
    }

    public boolean isRain() {
        return rain;
    }

    public boolean isSnow() {
        return snow;
    }

    public String[] getStyle() {
        return style;
    }
}

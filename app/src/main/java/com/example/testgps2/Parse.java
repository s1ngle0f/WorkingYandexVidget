package com.example.testgps2;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Parse {

    private String TAG = "PARSERFROMCLASS";
    private Document document;
    private Document document2;
    private Thread thread;
    private Runnable runnable;
    private Element element;
    private Elements elementsBusses, time ;
    private String urltoStop;
    private ArrayList<String> Busses = new ArrayList<>();
    private ArrayList<String> timeofBusses = new ArrayList<>();

    public void parse(String link){
        runnable = new Runnable() {
            @Override
            public void run() {
                parsePos(link);
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

    public ArrayList<String> getBusses() {
        return Busses;
    }

    public ArrayList<String> getTimeofBusses() {
        return timeofBusses;
    }

    private void parsePos(String link){
        try {
            Busses.clear();
            timeofBusses.clear();
            document = Jsoup.connect(link)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.185 YaBrowser/20.11.2.78 Yowser/2.5 Safari/537.36")
                    .get();
            element = document.getElementsByClass("link-wrapper").first();
            //Log.d(TAG, "Text: " + element.text());
            urltoStop = "https://yandex.ru" + element.attr("href");
            document2 = Jsoup.connect(urltoStop).get();
            elementsBusses = document2.getElementsByClass("masstransit-transport-list-view__type-transport _type_bus _highlighted");
            time = document2.getElementsByClass("masstransit-prognoses-view__title-text");
            for( Element el : elementsBusses ){
                Busses.add( el.text() );
            }
            for( Element el : time ){
                timeofBusses.add(el.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

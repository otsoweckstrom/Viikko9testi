package com.example.viikko9test;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class XMLParser extends AsyncTask<ArrayList<Theatre>, Void, ArrayList<Theatre>> {

    private MainActivity activity;
    private XmlPullParserFactory factory;
    private String url;


    public XMLParser(MainActivity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected ArrayList<Theatre> doInBackground(ArrayList<Theatre>... arrayLists) {
        try {
            ArrayList<Theatre> theatres;
            URL myurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) myurl.openConnection();
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();


            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            theatres = parseXML(parser);
            stream.close();
            return theatres;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Theatre> parseXML(XmlPullParser parser) throws IOException, XmlPullParserException {

        int event;
        ArrayList<Theatre> theatres = new ArrayList<Theatre>();
        String text = null;
        event = parser.getEventType();
        Theatre theatre = null;

        while (event != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();

            switch (event) {
                case XmlPullParser.START_TAG:
                    if (name.equalsIgnoreCase("TheatreArea")) {
                        theatre = new Theatre();
                        theatres.add(theatre);
                    } else if(theatre != null){
                        if(name.equalsIgnoreCase("Name")){
                            theatre.setName(parser.nextText());
                        }else if(name.equalsIgnoreCase("ID")){
                            theatre.setID(parser.nextText());
                    }
                    }
                    break;
                case XmlPullParser.TEXT:
                    break;
                case XmlPullParser.END_TAG:
                    break;
                    }
                    event = parser.next();

            }
        return theatres;
    }

    @Override
    protected void onPostExecute(ArrayList<Theatre> theatres) {
        super.onPostExecute(theatres);
        activity.callBackData(theatres);
    }
}



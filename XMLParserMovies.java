
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
import android.os.AsyncTask;

import com.example.viikko9test.MainActivity;
import com.example.viikko9test.Theatre;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class XMLParserMovies extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

    private MainActivity activity;
    private XmlPullParserFactory factory;
    private String url;
    private int timeStart;
    private int timeEnd;


    public XMLParserMovies(MainActivity activity, String url, int timeStart, int timeEnd) {
        this.activity = activity;
        this.url = url;
        this.timeEnd = timeEnd;
        this.timeStart = timeStart;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }



    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... arrayLists) {
        try {
            ArrayList<String> movies;
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
            movies = parseXMLMovies(parser);
            stream.close();
            return movies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<String> parseXMLMovies(XmlPullParser parser) throws IOException, XmlPullParserException {

        int event;
        ArrayList<String> movies = new ArrayList<String>();
        String text = null;
        String title = "";
        String ShowStart = "";
        event = parser.getEventType();


        while (event != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();

            switch (event) {
                case XmlPullParser.START_TAG:
                    if (name.equalsIgnoreCase("Title")) {
                        title = (parser.nextText());
                        movies.add(title + " " + ShowStart);
                    }else if (name.equalsIgnoreCase("dttmShowStart")) {
                        ShowStart = parser.nextText();
                        ShowStart = ShowStart.substring(11,16);
                    }
                    break;
                case XmlPullParser.TEXT:
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            event = parser.next();

        }
        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<String> movies) {
        super.onPostExecute(movies);
        activity.callBackDataMovies(movies);
    }
}




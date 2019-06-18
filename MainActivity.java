package com.example.viikko9test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "MainActivity";
    public TextView mDisplayDate;
    public TextView mDisplayTimeStart;
    public TextView mDisplayTimeEnd;
    public DatePickerDialog.OnDateSetListener mDateListener;
    public TimePickerDialog.OnTimeSetListener mTimeListener;
    public EditText date;
    public TextWatcher tw;
    public ListView listViewMovies;
    private String current = "";
    List<String> movielist = new ArrayList<>();
    Theatre selectedTheatre;
    TextView temp;
    List<Theatre> spinnerArray = new ArrayList<>();
    Spinner spinner1;
    Context context = null;
    int year;
    int month;
    int day;
    int hourStart;
    int minuteStart;
    int minuteEnd;
    int hourEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        final String url ="https://www.finnkino.fi/xml/TheatreAreas/";
        new XMLParser(this,url).execute();
        doSpinner();
        doCalendar();
        doClockStart();
        doClockEnd();
    }

    public void doListViewMovies(){
        listViewMovies = findViewById(R.id.listViewMovies);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, movielist);
        listViewMovies.setAdapter(adapter);
    }

    public void getMovies() {

        selectedTheatre = (Theatre) spinner1.getSelectedItem();
        if (selectedTheatre != null) {
            String movieurl = "http://www.finnkino.fi/xml/Schedule/?area=" + selectedTheatre.getID() + "&dt=" + current;
            System.out.println(movieurl);
            new XMLParserMovies(this, movieurl).execute();
        }
    }

    public void callBackData(ArrayList<Theatre> theatres) {
        spinnerArray = theatres;
        doSpinner();
    }

    public void callBackDataMovies(ArrayList<String> movies){
        this.movielist = movies;
        doListViewMovies();
    }

    public void doSpinner(){
        spinner1 = (Spinner) findViewById(R.id.spinnerTeatterit);
        ArrayAdapter<Theatre> adapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

    }

    public void doClockStart(){
        EditText editTextTimeStart = (EditText) findViewById(R.id.editTextStartTime);
    }

    public void doClockEnd(){
        EditText editTextEndTime = findViewById(R.id.editTextEndTime);
    }

    public void doCalendar(){
        date = (EditText)findViewById(R.id.editTextDate);
        tw = new TextWatcher() {
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        day  = Integer.parseInt(clean.substring(0,2));
                        month  = Integer.parseInt(clean.substring(2,4));
                        year = Integer.parseInt(clean.substring(4,8));

                        month = month < 1 ? 1 : month > 12 ? 12 : month;
                        cal.set(Calendar.MONTH, month-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, month, year);
                    }

                    clean = String.format("%s.%s.%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        date.addTextChangedListener(tw);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        getMovies();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
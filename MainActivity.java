package com.example.viikko9test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "MainActivity";
    public TextView mDisplayDate;
    public TextView mDisplayTime;
    public DatePickerDialog.OnDateSetListener mDateListener;
    public TimePickerDialog.OnTimeSetListener mTimeListener;
    TextView temp;
    List<Theatre> spinnerArray = new ArrayList<>();
    Spinner spinner1;
    Context context = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        final String url ="https://www.finnkino.fi/xml/TheatreAreas/";
        new XMLParser(this,url).execute();
        doCalendar();
        doClock();


    }
    public void callBackData(ArrayList<Theatre> theatres) {
        spinnerArray = theatres;
        doSpinner();
    }

    public void doSpinner(){
        spinner1 = (Spinner) findViewById(R.id.spinnerTeatterit);
        ArrayAdapter<Theatre> adapter = new ArrayAdapter<>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

    }

    public void doClock(){
        mDisplayTime = (TextView) findViewById(R.id.textViewTime);
        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(
                        MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mDisplayTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, true);//Yes 24 hour time
                dialog.setTitle("Select Time");
                dialog.show();
            }
        });
    }

    public void doCalendar(){

        mDisplayDate = (TextView) findViewById(R.id.textViewDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Material_Light_Dialog,
                        mDateListener,
                        day, month, year);
                dialog.getWindow();
                dialog.show();

            }
        });
        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDisplayDate.setText(dayOfMonth + "." + (month + 1) + "." + year);
            }
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}



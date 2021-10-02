package com.example.dam_proyecto_final.utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class CalendarUtility extends AppCompatActivity {

    private int tietDateID;
    private TextInputEditText tietDate = null, etTime = null;
    private Intent intentTools = null;
    private Activity activity = null;
    private static final Calendar c = Calendar.getInstance();

    public CalendarUtility(){}

    public CalendarUtility(Activity activity,int etDateID) {
        this.activity = activity;
        this.tietDateID =etDateID;


    }

    public void getDate() {

        tietDate = activity.findViewById(tietDateID);
        tietDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showCalendar();
                    // etDate.setEnabled(false);
                }
            }
        });

    }
/*
    public void getTime() {

        etTime = activity.findViewById(R.id.etTime);
        etTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTime();
                    // etTime.setEnabled(false);
                }
            }
        });

    }
*/




    public void showCalendar() {

        int days = c.get(Calendar.DAY_OF_MONTH);
        int months = c.get(Calendar.MONTH);
        int years = c.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                String wd = getWeekDay(day, month, year);

                Toast.makeText(activity.getApplicationContext(),
                        "FECHA SELECCIONADA: " + wd + " " + day + "-" + (month + 1) + "-" + year, Toast.LENGTH_LONG)
                        .show();

                String dday = day + "", mmonth = (month + 1) + "", yyear = year + "";
                if (day < 10) {
                    dday = "0" + day;
                }
                if ((month + 1) < 10) {
                    mmonth = "0" + (month + 1);
                }

                    tietDate.setText(yyear + "-" + mmonth + "-" + dday);
                   // tietDate.setEnabled(false);

            }
        }, years, months, days);
        datePickerDialog.updateDate(years, months, days);
        datePickerDialog.show();


    }
/*
    public void showTime() {
        int hour = c.get(Calendar.HOUR);
        int minutes = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Toast.makeText(activity.getApplicationContext(),
                        "HORA SELECCIONADA: " + hourOfDay + "/" + minute, Toast.LENGTH_LONG)
                        .show();

                String hhourOfDay = hourOfDay + "", mminute = minute + "";
                if (hourOfDay < 10) {
                    hhourOfDay = "0" + hourOfDay;
                }
                if (minute < 10) {
                    mminute = "0" + minute;
                }



                    etTime.setText(hhourOfDay + ":" + mminute);
                    etTime.setEnabled(false);


            }
        }, hour, minutes, true);
        timePickerDialog.updateTime(hour, minutes);
        timePickerDialog.show();

    }
*/

    public String getWeekDay(int day, int month, int year) {
        String weekDay = "";
        int nD = -1;
        Calendar cc = Calendar.getInstance();

        cc.set(year, month, day);
        nD = cc.get(Calendar.DAY_OF_WEEK);
        switch (nD) {
            case 1:
                weekDay = "Domingo";
                break;
            case 2:
                weekDay = "Lunes";
                break;
            case 3:
                weekDay = "Martes";
                break;
            case 4:
                weekDay = "Miércoles";
                break;
            case 5:
                weekDay = "Jueves";
                break;
            case 6:
                weekDay = "Viernes";
                break;
            case 7:
                weekDay = "Sábado";
                break;
        }
        return weekDay;
    }

}
//package com.example.dam_proyecto_final.utility;
//
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.util.Log;
//import android.view.View;
//import android.widget.DatePicker;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.textfield.TextInputEditText;
//
//import java.util.Calendar;
//
//public class CalendarUtility extends AppCompatActivity {
//
//    private TextInputEditText tietDate = null, etTime = null;
//    private int tietDateID;
//    private Activity activity = null;
//    private static final Calendar c = Calendar.getInstance();
//
//    // Recibe la actividad y la id del recurso sobre el que se plasmará la fecha
//    public CalendarUtility(Activity activity, int resourceID) {
//        this.activity = activity;
//        this.tietDateID = resourceID;
//    }
//
//    // Prepara el listener
//    public void getDate() {
//        tietDate = activity.findViewById(tietDateID);
//        tietDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    showCalendar();
//                    //  tietDate.setEnabled(false); // Solo activar en caso de no dejar editar la fecha de nuevo
//                }
//            }
//        });
//    }
//
//
//    // Cuando el listener sea pulsado por el usuario, muestra el Picker se selccion de fecha
//    public void showCalendar() {
//        int days = c.get(Calendar.DAY_OF_MONTH);
//        int months = c.get(Calendar.MONTH);
//        int years = c.get(Calendar.YEAR);
//        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                String wd = getWeekDay(day, month, year);
//                Log.d("DEBUGME", "FECHA SELECCIONADA: " + wd + " " + day + "-" + (month + 1) + "-" + year);
//
//                String dday = day + "", mmonth = (month + 1) + "", yyear = year + "";
//                if (day < 10) {
//                    dday = "0" + day;
//                }
//                if ((month + 1) < 10) {
//                    mmonth = "0" + (month + 1);
//                }
//
//                tietDate.setText(yyear + "-" + mmonth + "-" + dday);
//                // tietDate.setEnabled(false);
//            }
//        }, years, months, days);
//        datePickerDialog.updateDate(years, months, days);
//        datePickerDialog.show();
//    }
//
//    // Para saber el día de la semana a partir de una fecha
//    public String getWeekDay(int day, int month, int year) {
//        String weekDay = "";
//        int nD = -1;
//        Calendar cc = Calendar.getInstance();
//        cc.set(year, month, day);
//        nD = cc.get(Calendar.DAY_OF_WEEK);
//        switch (nD) {
//            case 1:
//                weekDay = "Domingo";
//                break;
//            case 2:
//                weekDay = "Lunes";
//                break;
//            case 3:
//                weekDay = "Martes";
//                break;
//            case 4:
//                weekDay = "Miércoles";
//                break;
//            case 5:
//                weekDay = "Jueves";
//                break;
//            case 6:
//                weekDay = "Viernes";
//                break;
//            case 7:
//                weekDay = "Sábado";
//                break;
//        }
//        return weekDay;
//    }
//}
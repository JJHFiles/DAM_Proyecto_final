package com.example.dam_proyecto_final.ddbb;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dam_proyecto_final.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GenericConnection  extends AsyncTask<Void, Integer, String> {

    private String url;
    private String user;
    private String password;

    @Override
    protected void onPreExecute() {
        url = "jdbc:mysql://damproyectofinalprowuemysql001.mysql.database.azure.com:3306";
        user = "apppro_user";
        password = "hwcJ3;xdhwcJ3;xd";
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Connection con = DriverManager.getConnection(url, user, password);
//            Log.d("DEBUGME ", context.getString(R.string.wrn_connectionddbb));
        } catch (SQLException throwables) {
//            Log.d("DEBUGME ", context.getString(R.string.wrn_errorworkingdatabase) + ": " + throwables.getMessage());
            throwables.printStackTrace();
        }
        //return con;
        return null;
    }

//    public static Connection GetConnection (Context context){
//        Connection con = null;
//        try {
////        Log.d("DEBUGME ", context.getResources().getString(R.string.bbddurl) + " " +
////            context.getResources().getString(R.string.bbddusr) + " " +
////            context.getResources().getString(R.string.bbddpwd) + " ");
////        con = DriverManager.getConnection(context.getResources().getString(R.string.bbddurl),
////                context.getResources().getString(R.string.bbddusr),
////                context.getResources().getString(R.string.bbddpwd));
//
//            con = DriverManager.getConnection("jdbc:mysql://damproyectofinalprowuemysql001.mysql.database.azure.com:3306",
//                    "apppro_user",
//                    "hwcJ3;xdhwcJ3;xd");
//            Log.d("DEBUGME ", context.getString(R.string.wrn_connectionddbb));
//        } catch (SQLException throwables) {
//            Log.d("DEBUGME ", context.getString(R.string.wrn_errorworkingdatabase) + ": " + throwables.getMessage());
//            throwables.printStackTrace();
//
//        }
//        return con;
//    }
//}
}
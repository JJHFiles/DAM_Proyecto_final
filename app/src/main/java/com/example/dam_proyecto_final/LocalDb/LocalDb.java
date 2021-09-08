package com.example.dam_proyecto_final.LocalDb;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDb extends SQLiteOpenHelper {

    Context context = null;

    private SQLiteDatabase sqlitedb = null;


    String createProfileTable = "" +
            "CREATE TABLE profile(" +
            "email TEXT UNIQUE, " +
            "timestamp TEXT)";

    //Constructor generado con el asistente
    public LocalDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        sqlitedb = this.getWritableDatabase();

    }

    //Solo se llama la primera vez, cuando todavía no tengo la BD
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createProfileTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(createProfileTable);


    }

    public void dropTable() {
        sqlitedb.execSQL("drop table profile");
    }

    public void closeDb() {
        this.sqlitedb.close();
    }

    public void insertNewProfile(
            String email,
            String pass,
            String timestamp) {
        try {
            sqlitedb.execSQL("INSERT INTO appointment (" +
                    "email, " +
                    "pass, " +
                    "timestamp" +
                    ") VALUES(" +
                    "'" + email + "'," +
                    "'" + pass + "'," +
                    "'" + timestamp + "'" +
                    ")");
        } catch (Exception e) {
            e.getStackTrace();
        }
        viewProfile();
    }

    public void viewProfile() {
        Cursor c = sqlitedb.rawQuery("SELECT * FROM profile", null);
        if (c.moveToFirst()) {
            do {
                System.out.println("PROFILE\n"
                        + "============================================="
                        + "\nEmail: " + c.getString(0)
                        + "\nPass: " + c.getString(1)
                        + "\nTimestamp: " + c.getString(2)
                        + "\ndetails: " + c.getString(4)
                        + "\n\n============================================="
                );
            } while (c.moveToNext());
        }
        c.close();
    }
}
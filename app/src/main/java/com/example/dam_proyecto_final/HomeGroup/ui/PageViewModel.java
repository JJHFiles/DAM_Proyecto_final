package com.example.dam_proyecto_final.HomeGroup.ui;

import android.content.SharedPreferences;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.dam_proyecto_final.LocalDb.LocalDb;
import com.example.dam_proyecto_final.SavedData;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
/*
        SavedData sd = new SavedData();
        SharedPreferences preferencias = sd.getPreferencias();
*/

        @Override
        public String apply(Integer input) {
          //  LocalDb ldb = new LocalDb(getApplicationContext(), "sqlitedb", null, 1);

            return "Hello World " /*+ preferencias.getString("savedData", "no tiene todavía email establecido")*/ + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}
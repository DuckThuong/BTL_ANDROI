
package com.example.btl_andnc_quanlydatdoan.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;


import com.example.btl_andnc_quanlydatdoan.Domain.Foods;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class TinyDB {

    private SharedPreferences preferences;

    public TinyDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public void putListObject(String key, ArrayList<Foods> playerList){
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(Foods player: playerList){
            objStrings.add(gson.toJson(player));
        }
        putListString(key, objStrings);
    }

   public ArrayList<Foods> getListObject(String key){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<Foods> playerList =  new ArrayList<Foods>();

        for(String jObjString : objStrings){
            Foods player  = gson.fromJson(jObjString,  Foods.class);
            playerList.add(player);
        }
        return playerList;
    }

    private void checkForNullKey(String key){
        if (key == null){
            throw new NullPointerException();
        }
    }
}
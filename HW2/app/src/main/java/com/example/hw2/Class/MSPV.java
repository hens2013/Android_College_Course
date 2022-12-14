package com.example.hw2.Class;

import android.content.Context;
import android.content.SharedPreferences;

public class MSPV {


    //class for shared preference implementation
    private final String SP_FILE = "MY_SCORES";

    private static MSPV me;
    private final SharedPreferences sharedPreferences;

    public static MSPV getMe() {
        return me;
    }

    private MSPV(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static void initHelper(Context context) {
        if (me == null) {
            me = new MSPV(context);
        }
    }

    public void putDouble(String KEY, double defValue) {
        putString(KEY, String.valueOf(defValue));
    }

    public double getDouble(String KEY, double defValue) {
        return Double.parseDouble(getString(KEY, String.valueOf(defValue)));
    }

    public int getInt(String KEY, int defValue) {
        return sharedPreferences.getInt(KEY, defValue);
    }

    public void putInt(String KEY, int value) {
        sharedPreferences.edit().putInt(KEY, value).apply();
    }

    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }


}

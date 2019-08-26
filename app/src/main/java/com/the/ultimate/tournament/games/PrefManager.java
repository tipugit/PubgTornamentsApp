package com.the.ultimate.tournament.games;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {

    // shared pref mode
    private final int PRIVATE_MODE = 0;

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;

    // Shared preferences file name
    private static final String PREF_NAME = "player";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setBoolean(String PREF_NAME,Boolean val) {
        editor.putBoolean(PREF_NAME, val);
        editor.commit();
    }
    public void setString(String PREF_NAME,String VAL) {
        editor.putString(PREF_NAME, VAL);
        editor.commit();
    }
    public void setInt(String PREF_NAME,int VAL) {
        editor.putInt(PREF_NAME, VAL);
        editor.commit();
    }
    public boolean getBoolean(String PREF_NAME) {
        return pref.getBoolean(PREF_NAME,true);
    }
    public String getString(String PREF_NAME) {
        if(pref.contains(PREF_NAME)){
            return pref.getString(PREF_NAME,null);
        }
        return  "";
    }
    public void remove(String PREF_NAME){
        if(pref.contains(PREF_NAME)){
            editor.remove(PREF_NAME);
            editor.commit();
        }
    }

    public int getInt(String key) {
        return pref.getInt(key,0);
    }

    public void clearall() {
        editor.clear();
        editor.commit();
    }
}

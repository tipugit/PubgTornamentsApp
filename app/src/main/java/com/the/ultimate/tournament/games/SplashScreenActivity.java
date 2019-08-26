package com.the.ultimate.tournament.games;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.the.ultimate.tournament.games.R;


public class SplashScreenActivity extends Activity {

    private static final int SPLASH_SHOW_TIME = 1000;

    //Prefrance
    private static PrefManager prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        prf = new PrefManager(SplashScreenActivity.this);

        new BackgroundSplashTask().execute();

    }

    /**
     * Async Task: can be used to load DB, images during which the splash screen
     * is shown to user
     */
    private class BackgroundSplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Thread.sleep(SPLASH_SHOW_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Create a new boolean and preference and set it to true
            boolean isFirstStart = prf.getBoolean("firstStart");

            // If the activity has never started before...
            if (isFirstStart) {

                // Launch app intro
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);

                // Make a new preferences editor
                prf.setBoolean("firstStart", false);

            } else {

                // Create a new boolean and preference and set it to true
                String isSignedin = prf.getString("username");

                if(!isSignedin.equalsIgnoreCase("")) {
                    //user signedin
                    Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    i.putExtra("loaded_info", " ");
                    startActivity(i);
                } else {
                    //user not signedin
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    i.putExtra("loaded_info", " ");
                    startActivity(i);
                }
            }
            finish();
        }

    }
}

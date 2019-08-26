package com.the.ultimate.tournament.games;

import android.app.Application;

import com.onesignal.OneSignal;

public class ApplicationClass extends Application {

    private static final String TAG_EMAIL = "email";

    //Prefrance
    private static PrefManager prf;

    @Override
    public void onCreate() {
        super.onCreate();

        prf = new PrefManager(this);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.setEmail(prf.getString(TAG_EMAIL));
    }
}

/*
 Copyright 2020 Adobe
 All Rights Reserved.

 NOTICE: Adobe permits you to use, modify, and distribute this file in
 accordance with the terms of the Adobe license agreement accompanying
 it.
 */
package com.adobe.marketing.mobile.sampleapp;

import com.adobe.marketing.mobile.AdobeCallback;

//import com.adobe.marketing.mobile.Assurance;
import com.adobe.marketing.mobile.Edge;
import com.adobe.marketing.mobile.Messaging;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.UserProfile;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.LoggingMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class MainApp extends Application {

    private static final String LOG_TAG = "MainApp";
//    private static final String LAUNCH_ENVIRONMENT_FILE_ID = "";
//    static final String PLATFORM_DCS_URL = "";
//    // Profile dataset id used by messaging for syncing the push toke with profile
//    static final String PLATFORM_PROFILE_DATASET_ID = "";
//    // Org id needed by the messaging tab to send the custom event
//    static final String ORG_ID = "";
//    // Experience event dataset id used by messaging for sending tracking data
//    private static final String PLATFORM_EXPERIENCE_EVENT_DATASET_ID = "";
//    // Dataset id used by the messaging tab to send a custom action event.
//    static final String CUSTOM_ACTION_DATASET = "";

    private static final String LAUNCH_ENVIRONMENT_FILE_ID="3149c49c3910/6a68c2e19c81/launch-4b2394565377-development";
    static final String PLATFORM_DCS_URL="https://dcs-stg.adobedc.net/collection/0e8fa7ee477ffbdc8d26f626a76702112b501ea296ec18b25ee09a117d6ccaf7";
    static final String PLATFORM_PROFILE_DATASET_ID="600928fa2a40c918db2e8db6";
    static final String ORG_ID="745F37C35E4B776E0A49421B@AdobeOrg";
    static final String PLATFORM_EXPERIENCE_EVENT_DATASET_ID="600928f915a07918dcb91605";
    static final String CUSTOM_ACTION_DATASET="601b6f1fd976ed18db4a24fc";
    static final String CUSTOM_PROFILE_DATASET="601b6fda97ce4a18db9916b7";

    @Override
    public void onCreate() {
        super.onCreate();
        MobileCore.setApplication(this);
        MobileCore.setLogLevel(LoggingMode.VERBOSE);
        MobileCore.setSmallIconResourceID(R.mipmap.ic_launcher_round);
        MobileCore.setLargeIconResourceID(R.mipmap.ic_launcher_round);

        try {
            Analytics.registerExtension();
            UserProfile.registerExtension();
            Identity.registerExtension();
            Lifecycle.registerExtension();
            Signal.registerExtension();
            Edge.registerExtension();
            //Assurance.registerExtension();
            Messaging.registerExtension();

            MobileCore.start(new AdobeCallback() {
                @Override
                public void call(Object o) {

                    MobileCore.configureWithAppID(LAUNCH_ENVIRONMENT_FILE_ID);

                    MobileCore.lifecycleStart(null);

                    Map<String, Object> map = new HashMap<>();
                    map.put("messaging.dccs", PLATFORM_DCS_URL);
                    map.put("messaging.profileDataset", PLATFORM_PROFILE_DATASET_ID);
                    map.put("messaging.eventDataset", PLATFORM_EXPERIENCE_EVENT_DATASET_ID);
                    MobileCore.updateConfiguration(map);
                    Log.d(LOG_TAG, "AEP Mobile SDK is initialized");

                    // To connect with Assurance session on start, put the URL for your session here
                   // Assurance.startSession("aepsdksampleapp://?adb_validation_sessionid=c857e06a-90e7-45ba-adb3-b1d7f9612600");
                }
            });
        } catch (InvalidInitException e) {
            e.printStackTrace();
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        MobileCore.setPushIdentifier(token);
                    }
                });
    }
}

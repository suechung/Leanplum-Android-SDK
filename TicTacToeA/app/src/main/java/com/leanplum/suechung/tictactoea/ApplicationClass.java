package com.leanplum.suechung.tictactoea;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.leanplum.Leanplum;

import com.leanplum.LeanplumActivityHelper;
import com.leanplum.LeanplumPushNotificationCustomizer;
import com.leanplum.LeanplumPushService;

import com.leanplum.annotations.Parser; // variable

import com.leanplum.annotations.Variable;
import com.leanplum.callbacks.StartCallback;
import com.leanplum.callbacks.VariablesChangedCallback;

//import com.leanplum.suechung.tictactoea.AppInboxFragment;
//import com.leanplum.suechung.tictactoea.InAppMessagesFragment;
//import com.leanplum.suechung.tictactoea.PushNotificationsFragment;
//import com.leanplum.suechung.tictactoea.TestingFragment;


import com.leanplum.LeanplumApplication;
import com.leanplum.LeanplumDeviceIdMode;
import com.leanplum.LeanplumPushService;
import com.leanplum.Var;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import java.lang.reflect.Field;

public class ApplicationClass extends Application {

    static Map<String, Object> attributes = new HashMap<String, Object>();

    @Override
    public void onCreate() {
        Leanplum.setApplicationContext(this);
        Parser.parseVariables(this);
        Parser.parseVariablesForClasses(loginpage.class);

        //  For session lifecyle tracking.
        LeanplumActivityHelper.enableLifecycleCallbacks(this);

        super.onCreate();

        // Insert your API keys here.
        if (BuildConfig.DEBUG) {
            Leanplum.setAppIdForDevelopmentMode("APP_ID", "DEV_KEY");
            Log.i("#### ", "Dev mode");
        } else {
            Leanplum.setAppIdForProductionMode("APP_ID", "PROD_KEY");
            Log.i("#### ", "Prod mode");
        }

        LeanplumPushService.setCustomizer(new LeanplumPushNotificationCustomizer() {
            @Override
            public void customize(NotificationCompat.Builder builder, Bundle bundle) {
                builder.setSmallIcon(R.drawable.leanplum_default_push_icon);
            }

            @Override
            public void customize(Notification.Builder builder, Bundle bundle, @Nullable Notification.Style style) {

            }
        });

        Leanplum.addVariablesChangedHandler(new VariablesChangedCallback() {
            @Override
            public void variablesChanged() {

            }
        });

        com.leanplum.customtemplates.MessageTemplates.register(getApplicationContext());

        // Leanplum User attributes
        // This will only run once per session, even if the activity is restarted.
        attributes.put("gender", "Female");
        attributes.put("age", 25);
        attributes.put("VipLevel", 3);

        Leanplum.start(this, attributes);

    }

}

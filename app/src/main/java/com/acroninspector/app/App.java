package com.acroninspector.app;

import android.app.Application;
import android.content.Context;

import com.acroninspector.app.di.global.ComponentHolder;

import timber.log.Timber;

public class App extends Application {

    private ComponentHolder componentsHolder;

    public static App getApp(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        componentsHolder = new ComponentHolder(this);
        componentsHolder.inject();
    }

    public ComponentHolder getComponentsHolder() {
        return componentsHolder;
    }
}

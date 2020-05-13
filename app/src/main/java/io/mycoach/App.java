package io.mycoach;

import android.app.Application;

/**
 * Note: never store mutable data here!
 */
public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}

package com.avantgarde.timy;

import android.app.Application;

public class App extends Application{
    private static App app;
    public static App get(){return app;}

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}

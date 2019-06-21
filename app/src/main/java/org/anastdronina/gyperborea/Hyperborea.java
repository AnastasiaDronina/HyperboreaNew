package org.anastdronina.gyperborea;

import android.app.Application;
import android.content.Context;

public class Hyperborea extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        DbThread.init(getApplicationContext());
    }

    public static Context getAppContext() {
        return Hyperborea.context;
    }
}

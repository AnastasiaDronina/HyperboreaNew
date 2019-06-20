package org.anastdronina.gyperborea;

import android.app.Application;

public class gyperborea extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DbThread.getInstance();
    }
}

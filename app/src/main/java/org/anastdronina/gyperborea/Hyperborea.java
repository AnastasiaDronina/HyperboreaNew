package org.anastdronina.gyperborea;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

public class Hyperborea extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        DbThread.init(getApplicationContext());

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

    }

    public static Context getAppContext() {
        return Hyperborea.context;
    }
}

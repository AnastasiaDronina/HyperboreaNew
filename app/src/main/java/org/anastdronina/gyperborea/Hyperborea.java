package org.anastdronina.gyperborea;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

public class Hyperborea extends Application {

    private static Application hyperborea;

    @Override
    public void onCreate() {
        super.onCreate();
        hyperborea = this;

        DbThread.init(this.getApplicationContext());

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

    }

    public static Application getApplication() {
        return hyperborea;
    }

    public static Context getAppContext() {
        return getApplication().getApplicationContext();
    }
}

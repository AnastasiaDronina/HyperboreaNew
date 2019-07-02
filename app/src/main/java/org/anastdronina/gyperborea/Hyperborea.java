package org.anastdronina.gyperborea;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

public class Hyperborea extends Application {

    private static Application sHyperborea;

    @Override
    public void onCreate() {
        super.onCreate();
        sHyperborea = this;

        DbThread.init(this.getApplicationContext());

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

    }

    public static Application getApplication() {
        return sHyperborea;
    }

    public static Context getAppContext() {
        return getApplication().getApplicationContext();
    }
}

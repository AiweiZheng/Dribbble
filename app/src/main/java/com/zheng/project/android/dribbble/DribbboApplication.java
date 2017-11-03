package com.zheng.project.android.dribbble;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;

public class DribbboApplication extends Application {
    private static DribbboApplication instance;

    @Override
    public void onCreate() {
        synchronized (DribbboApplication.class) {
            instance = this;
        }
        super.onCreate();
        Fresco.initialize(this);
    }

    public static DribbboApplication getInstance() {
        if (instance == null) {
            instance = new DribbboApplication();
        }
        return instance;
    }

}

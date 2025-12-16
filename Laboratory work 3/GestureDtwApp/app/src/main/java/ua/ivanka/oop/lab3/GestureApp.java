package ua.ivanka.oop.lab3;

import android.app.Application;

import java.util.logging.Logger;

import ua.ivanka.oop.lab3.logging.LoggerConfig;

public class GestureApp extends Application {
    private static final Logger LOG = LoggerConfig.getLogger(GestureApp.class);

    @Override
    public void onCreate() {
        super.onCreate();
        LoggerConfig.init();
        LOG.info("Application started");
    }
}

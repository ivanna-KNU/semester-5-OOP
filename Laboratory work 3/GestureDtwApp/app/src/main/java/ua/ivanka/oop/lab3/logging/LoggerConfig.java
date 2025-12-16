package ua.ivanka.oop.lab3.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import ua.ivanka.oop.lab3.BuildConfig;

public final class LoggerConfig {
    private static volatile boolean initialized = false;

    private LoggerConfig() {}

    public static void init() {
        if (initialized) return;
        synchronized (LoggerConfig.class) {
            if (initialized) return;

            Logger root = LogManager.getLogManager().getLogger("");
            if (root != null) {
                for (Handler h : root.getHandlers()) {
                    root.removeHandler(h);
                }
                LogcatHandler handler = new LogcatHandler();
                handler.setLevel(Level.ALL);
                root.addHandler(handler);
                root.setLevel(BuildConfig.DEBUG);
            }

            initialized = true;
        }
    }

    public static Logger getLogger(Class<?> cls) {
        return Logger.getLogger(cls.getName());
    }
}

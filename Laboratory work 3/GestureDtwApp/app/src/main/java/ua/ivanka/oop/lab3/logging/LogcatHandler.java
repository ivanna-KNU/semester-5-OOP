package ua.ivanka.oop.lab3.logging;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogcatHandler extends Handler {
    private static final String TAG = "GestureDtwApp";

    @Override
    public void publish(LogRecord record) {
        if (record == null) return;
        if (!isLoggable(record)) return;

        int priority = mapPriority(record.getLevel());
        String msg = format(record);
        Log.println(priority, TAG, msg);
    }

    @Override
    public void flush() {
        // no-op
    }

    @Override
    public void close() {
        // no-op
    }

    private static int mapPriority(Level level) {
        if (level == null) return Log.INFO;
        if (level.intValue() >= Level.SEVERE.intValue()) return Log.ERROR;
        if (level.intValue() >= Level.WARNING.intValue()) return Log.WARN;
        if (level.intValue() >= Level.INFO.intValue()) return Log.INFO;
        if (level.intValue() >= Level.FINE.intValue()) return Log.DEBUG;
        return Log.VERBOSE;
    }

    private static String format(LogRecord r) {
        StringBuilder sb = new StringBuilder();
        sb.append(r.getLoggerName()).append(": ").append(r.getMessage());

        Throwable t = r.getThrown();
        if (t != null) {
            sb.append('\n').append(stackTraceToString(t));
        }
        return sb.toString();
    }

    private static String stackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }
}

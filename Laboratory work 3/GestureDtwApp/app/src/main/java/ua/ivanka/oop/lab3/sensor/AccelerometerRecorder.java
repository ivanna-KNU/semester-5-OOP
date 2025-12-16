package ua.ivanka.oop.lab3.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccelerometerRecorder implements SensorEventListener {
    public interface Listener {
        void onSampleCountChanged(int count);
        void onAutoStopped(int finalCount);
    }

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final Logger log;

    private final List<float[]> samples = new ArrayList<>();
    private volatile boolean recording = false;

    private Listener listener;

    // High-pass filter to remove gravity
    private final float[] gravity = new float[]{0f, 0f, 0f};
    private static final float ALPHA = 0.8f;

    private int maxSamples = 180; // ~1-2 seconds depending on device

    public AccelerometerRecorder(SensorManager sensorManager, Sensor accelerometer, Logger log) {
        this.sensorManager = sensorManager;
        this.accelerometer = accelerometer;
        this.log = log;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setMaxSamples(int maxSamples) {
        this.maxSamples = maxSamples;
    }

    public void start() {
        samples.clear();
        gravity[0] = gravity[1] = gravity[2] = 0f;
        recording = true;
        boolean ok = sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        log.info("Accelerometer listener registered=" + ok);
    }

    /**
     * @return sample count recorded
     */
    public int stop() {
        if (!recording) {
            return samples.size();
        }
        recording = false;
        try {
            sensorManager.unregisterListener(this);
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to unregister listener", e);
        }
        return samples.size();
    }

    public List<float[]> getSamplesCopy() {
        synchronized (samples) {
            return new ArrayList<>(samples);
        }
    }

    public boolean isRecording() {
        return recording;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!recording) return;
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        // High-pass filter: linear = input - gravity
        gravity[0] = ALPHA * gravity[0] + (1 - ALPHA) * event.values[0];
        gravity[1] = ALPHA * gravity[1] + (1 - ALPHA) * event.values[1];
        gravity[2] = ALPHA * gravity[2] + (1 - ALPHA) * event.values[2];

        float lx = event.values[0] - gravity[0];
        float ly = event.values[1] - gravity[1];
        float lz = event.values[2] - gravity[2];

        int count;
        synchronized (samples) {
            if (samples.size() >= maxSamples) {
                // auto-stop
                recording = false;
                sensorManager.unregisterListener(this);
                count = samples.size();
            } else {
                samples.add(new float[]{lx, ly, lz});
                count = samples.size();
            }
        }

        Listener l = listener;
        if (l != null) {
            l.onSampleCountChanged(count);
        }

        if (!recording) {
            log.info("Auto-stopped at maxSamples=" + count);
            if (l != null) {
                l.onAutoStopped(count);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // no-op
    }
}

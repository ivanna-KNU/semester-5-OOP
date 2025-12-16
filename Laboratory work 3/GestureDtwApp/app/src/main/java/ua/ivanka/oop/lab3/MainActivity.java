package ua.ivanka.oop.lab3;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ua.ivanka.oop.lab3.dtw.GestureClassifier;
import ua.ivanka.oop.lab3.logging.LoggerConfig;
import ua.ivanka.oop.lab3.sensor.AccelerometerRecorder;
import ua.ivanka.oop.lab3.templates.Gesture;
import ua.ivanka.oop.lab3.templates.GestureTemplate;
import ua.ivanka.oop.lab3.templates.TemplateRepository;

public class MainActivity extends AppCompatActivity {
    private static final Logger LOG = LoggerConfig.getLogger(MainActivity.class);

    private TextView tvStatus;
    private TextView tvSamples;
    private TextView tvResult;

    private Button btnStart;
    private Button btnStop;
    private Button btnDetect;

    private AccelerometerRecorder recorder;
    private GestureClassifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        tvSamples = findViewById(R.id.tvSamples);
        tvResult = findViewById(R.id.tvResult);

        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnDetect = findViewById(R.id.btnDetect);

        setupClassifier();
        setupRecorder();
        wireUi();

        LOG.info("MainActivity created");
    }

    private void setupClassifier() {
        try {
            TemplateRepository repo = new TemplateRepository(getAssets(), LoggerConfig.getLogger(TemplateRepository.class));
            List<GestureTemplate> templates = repo.loadAll("templates");
            classifier = new GestureClassifier(templates, LoggerConfig.getLogger(GestureClassifier.class));
            LOG.info("Templates loaded: " + templates.size());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to load templates", e);
            classifier = new GestureClassifier(Collections.emptyList(), LoggerConfig.getLogger(GestureClassifier.class));
        }
    }

    private void setupRecorder() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer == null) {
            tvStatus.setText(getString(R.string.error_no_accelerometer));
            btnStart.setEnabled(false);
            btnStop.setEnabled(false);
            btnDetect.setEnabled(false);
            return;
        }

        recorder = new AccelerometerRecorder(sensorManager, accelerometer, LoggerConfig.getLogger(AccelerometerRecorder.class));
        recorder.setListener(new AccelerometerRecorder.Listener() {
            @Override
            public void onSampleCountChanged(int count) {
                runOnUiThread(() -> tvSamples.setText(getString(R.string.samples_fmt, count)));
            }

            @Override
            public void onAutoStopped(int finalCount) {
                runOnUiThread(() -> {
                    tvStatus.setText(getString(R.string.status_idle));
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);
                    btnDetect.setEnabled(finalCount > 20);
                });
            }
        });
    }

    private void wireUi() {
        btnStart.setOnClickListener(v -> {
            if (recorder == null) return;
            recorder.start();
            tvStatus.setText(getString(R.string.status_recording));
            tvResult.setText(getString(R.string.result_none));
            tvSamples.setText(getString(R.string.samples_0));

            btnStart.setEnabled(false);
            btnStop.setEnabled(true);
            btnDetect.setEnabled(false);

            LOG.info("Recording started");
        });

        btnStop.setOnClickListener(v -> {
            if (recorder == null) return;
            int count = recorder.stop();
            tvStatus.setText(getString(R.string.status_idle));
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            btnDetect.setEnabled(count > 20);

            LOG.info("Recording stopped, samples=" + count);
        });

        btnDetect.setOnClickListener(v -> {
            if (recorder == null) return;
            List<float[]> window = recorder.getSamplesCopy();
            Gesture best = classifier.classify(window);

            String label = gestureLabel(best);

            tvResult.setText(getString(R.string.result_fmt, label));
            LOG.info(String.format(Locale.US, "Detected=%s, samples=%d", best.name(), window.size()));
        });
    }

    private String gestureLabel(Gesture gesture) {
        if (gesture == null || gesture == Gesture.UNKNOWN) {
            return getString(R.string.result_unknown);
        }
        switch (gesture) {
            case SHAKE:
                return getString(R.string.gesture_shake);
            case LEFT_RIGHT:
                return getString(R.string.gesture_left_right);
            case UP_DOWN:
                return getString(R.string.gesture_up_down);
            case FORWARD_BACK:
                return getString(R.string.gesture_forward_back);
            default:
                return gesture.name();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recorder != null) {
            recorder.stop();
        }
    }
}

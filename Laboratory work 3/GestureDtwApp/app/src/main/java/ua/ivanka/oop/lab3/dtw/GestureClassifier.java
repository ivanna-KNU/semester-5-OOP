package ua.ivanka.oop.lab3.dtw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ua.ivanka.oop.lab3.templates.Gesture;
import ua.ivanka.oop.lab3.templates.GestureTemplate;

public class GestureClassifier {
    private final List<GestureTemplate> templates;
    private final Logger log;

    // Empirical threshold (normalized DTW). You can tune this on your device.
    private double unknownThreshold = 5.0;

    public GestureClassifier(List<GestureTemplate> templates, Logger log) {
        this.templates = templates == null ? Collections.emptyList() : templates;
        this.log = log;
    }

    public void setUnknownThreshold(double unknownThreshold) {
        this.unknownThreshold = unknownThreshold;
    }

    public Gesture classify(List<float[]> rawWindow) {
        if (rawWindow == null || rawWindow.size() < 10) {
            return Gesture.UNKNOWN;
        }
        if (templates.isEmpty()) {
            return Gesture.UNKNOWN;
        }

        List<float[]> window = normalize(rawWindow);

        Map<Gesture, Double> distances = new HashMap<>();
        for (GestureTemplate t : templates) {
            List<float[]> templ = normalize(t.getSamples());
            int band = Math.max(10, Math.min(window.size(), templ.size()) / 10);
            double d = Dtw.distance3d(window, templ, band);
            distances.put(t.getGesture(), d);
        }

        Map.Entry<Gesture, Double> best = distances.entrySet()
                .stream()
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .orElse(null);

        if (best == null) {
            return Gesture.UNKNOWN;
        }

        log.info("DTW best=" + best.getKey().name() + ", dist=" + best.getValue());
        return best.getValue() <= unknownThreshold ? best.getKey() : Gesture.UNKNOWN;
    }

    private static List<float[]> normalize(List<float[]> input) {
        int n = input.size();
        double mx = 0, my = 0, mz = 0;
        for (float[] v : input) {
            mx += v[0];
            my += v[1];
            mz += v[2];
        }
        mx /= n;
        my /= n;
        mz /= n;

        double sx = 0, sy = 0, sz = 0;
        for (float[] v : input) {
            double dx = v[0] - mx;
            double dy = v[1] - my;
            double dz = v[2] - mz;
            sx += dx * dx;
            sy += dy * dy;
            sz += dz * dz;
        }
        sx = Math.sqrt(sx / n);
        sy = Math.sqrt(sy / n);
        sz = Math.sqrt(sz / n);
        if (sx < 1e-6) sx = 1.0;
        if (sy < 1e-6) sy = 1.0;
        if (sz < 1e-6) sz = 1.0;

        List<float[]> out = new ArrayList<>(n);
        for (float[] v : input) {
            out.add(new float[]{(float) ((v[0] - mx) / sx), (float) ((v[1] - my) / sy), (float) ((v[2] - mz) / sz)});
        }
        return out;
    }
}

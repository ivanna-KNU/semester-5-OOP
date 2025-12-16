package ua.ivanka.oop.lab3.templates;

import java.util.List;

public class GestureTemplate {
    private final Gesture gesture;
    private final List<float[]> samples;

    public GestureTemplate(Gesture gesture, List<float[]> samples) {
        this.gesture = gesture;
        this.samples = samples;
    }

    public Gesture getGesture() {
        return gesture;
    }

    public List<float[]> getSamples() {
        return samples;
    }
}

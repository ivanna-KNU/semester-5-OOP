package ua.ivanka.oop.lab3.templates;

import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TemplateRepository {
    private final AssetManager assets;
    private final Logger log;

    public TemplateRepository(AssetManager assets, Logger log) {
        this.assets = assets;
        this.log = log;
    }

    public List<GestureTemplate> loadAll(String templatesDir) {
        List<GestureTemplate> out = new ArrayList<>();
        try {
            String[] files = assets.list(templatesDir);
            if (files == null) {
                return out;
            }
            for (String name : files) {
                if (!name.toLowerCase(Locale.US).endsWith(".json")) continue;
                String path = templatesDir + "/" + name;
                GestureTemplate t = loadOne(path);
                if (t != null) {
                    out.add(t);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to list templates", e);
        }
        return out;
    }

    private GestureTemplate loadOne(String assetPath) {
        try {
            String json = readAssetText(assetPath);
            JSONObject obj = new JSONObject(json);

            String gestureName = obj.getString("name");
            Gesture gesture = Gesture.valueOf(gestureName);

            JSONArray arr = obj.getJSONArray("samples");
            List<float[]> samples = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                JSONArray v = arr.getJSONArray(i);
                samples.add(new float[]{(float) v.getDouble(0), (float) v.getDouble(1), (float) v.getDouble(2)});
            }

            log.info("Loaded template " + gesture.name() + ", len=" + samples.size());
            return new GestureTemplate(gesture, samples);
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed to load template: " + assetPath, e);
            return null;
        }
    }

    private String readAssetText(String assetPath) throws Exception {
        try (InputStream is = assets.open(assetPath);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        }
    }
}

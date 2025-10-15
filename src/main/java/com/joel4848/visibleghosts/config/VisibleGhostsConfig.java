package com.joel4848.visibleghosts.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VisibleGhostsConfig {

    private static VisibleGhostsConfig INSTANCE;
    private static final File CONFIG_FILE = new File(System.getProperty("user.home"), "visibleghosts.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private boolean renderInvisiblePlayers = false;
    private float ghostTransparency = 0.65f;

    public static VisibleGhostsConfig get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    public static void load() {
        try {
            if (CONFIG_FILE.exists()) {
                INSTANCE = GSON.fromJson(new FileReader(CONFIG_FILE), VisibleGhostsConfig.class);
            } else {
                INSTANCE = new VisibleGhostsConfig();
                save();
            }
        } catch (Exception e) {
            INSTANCE = new VisibleGhostsConfig();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRenderInvisiblePlayers() {
        return renderInvisiblePlayers;
    }

    public void setRenderInvisiblePlayers(boolean value) {
        renderInvisiblePlayers = value;
    }

    public float getGhostTransparency() {
        return ghostTransparency;
    }

    public void setGhostTransparency(float value) {
        ghostTransparency = value;
    }
}

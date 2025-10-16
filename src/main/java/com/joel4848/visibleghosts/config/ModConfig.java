package com.joel4848.visibleghosts.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joel4848.visibleghosts.VisibleGhostsClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/visible-ghosts.json");

    private static ModConfig INSTANCE;

    private boolean renderInvisiblePlayers = true;
    private int ghostTransparency = 166;

    public static ModConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = load();
        }
        return INSTANCE;
    }

    public boolean isRenderInvisiblePlayers() {
        return renderInvisiblePlayers;
    }

    public void setRenderInvisiblePlayers(boolean renderInvisiblePlayers) {
        this.renderInvisiblePlayers = renderInvisiblePlayers;
        save();
    }

    public int getGhostTransparency() {
        return ghostTransparency;
    }

    public void setGhostTransparency(int ghostTransparency) {
        this.ghostTransparency = Math.max(0, Math.min(255, ghostTransparency));
        save();
    }

    private static ModConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ModConfig config = GSON.fromJson(reader, ModConfig.class);
                VisibleGhostsClient.LOGGER.info("Loaded config from file");
                return config;
            } catch (IOException e) {
                VisibleGhostsClient.LOGGER.error("Failed to load config file", e);
            }
        }

        ModConfig config = new ModConfig();
        config.save();
        return config;
    }

    private void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            VisibleGhostsClient.LOGGER.error("Failed to save config file", e);
        }
    }
}
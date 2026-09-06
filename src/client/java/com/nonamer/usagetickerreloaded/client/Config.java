package com.nonamer.usagetickerreloaded.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    public String customText = "";
    public boolean useCommaSeparator = false;

    private static final Path CONFIG_PATH = Paths.get("config/usage-ticker-reloaded/settings.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Config load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                return GSON.fromJson(json, Config.class);
            } catch (IOException ignored) {}
        }
        return new Config();
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException ignored) {}
    }
}
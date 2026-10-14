package com.starclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.starclient.StarClient;
import com.starclient.module.ClientModule;
import com.starclient.module.ModuleManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = Path.of("config", "starclient.json");
    private ConfigManager() {}

    public static void load() {
        if (!Files.exists(FILE)) { save(); return; }
        try {
            JsonObject root = JsonParser.parseString(Files.readString(FILE)).getAsJsonObject();
            if (root.has("modules") && root.get("modules").isJsonObject()) {
                JsonObject modules = root.getAsJsonObject("modules");
                for (ClientModule module : ModuleManager.all()) {
                    JsonElement value = modules.get(module.name());
                    if (value != null && value.isJsonPrimitive()) module.setEnabled(value.getAsBoolean());
                }
            }
        } catch (Exception exception) {
            StarClient.LOGGER.warn("Could not load Star Client config; using defaults.", exception);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            JsonObject root = new JsonObject();
            JsonObject modules = new JsonObject();
            for (ClientModule module : ModuleManager.all()) modules.addProperty(module.name(), module.enabled());
            root.add("modules", modules);
            Files.writeString(FILE, GSON.toJson(root));
        } catch (IOException exception) {
            StarClient.LOGGER.warn("Could not save Star Client config.", exception);
        }
    }
}

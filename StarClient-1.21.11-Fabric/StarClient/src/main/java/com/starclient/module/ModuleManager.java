package com.starclient.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleManager {
    private static final List<ClientModule> MODULES = new ArrayList<>();
    private ModuleManager() {}

    public static void registerDefaults() {
        MODULES.clear();
        MODULES.add(new ClientModule("FPS Counter", "Shows your current frames per second.", ModuleCategory.HUD, true));
        MODULES.add(new ClientModule("Coordinates", "Shows your current XYZ coordinates.", ModuleCategory.HUD, true));
        MODULES.add(new ClientModule("Keystrokes", "Displays movement and jump key states.", ModuleCategory.HUD, true));
        MODULES.add(new ClientModule("CPS Counter", "Counts left and right mouse clicks per second.", ModuleCategory.HUD, true));
        MODULES.add(new ClientModule("Armor HUD", "Shows equipped armor and durability.", ModuleCategory.HUD, true));
        MODULES.add(new ClientModule("Ping Display", "Shows latency to the current multiplayer server.", ModuleCategory.HUD, true));
        MODULES.add(new ClientModule("Direction HUD", "Shows your current facing direction.", ModuleCategory.HUD, false));
        MODULES.add(new ClientModule("Minimal HUD", "Hides selected vanilla HUD elements (planned setting).", ModuleCategory.VISUALS, false));
        MODULES.add(new ClientModule("Reduced Particles", "Reduces custom client visual clutter where supported.", ModuleCategory.PERFORMANCE, false));
        MODULES.add(new ClientModule("Reduced Animations", "A placeholder toggle for future animation controls.", ModuleCategory.PERFORMANCE, false));
        MODULES.add(new ClientModule("Custom Crosshair", "A placeholder for crosshair customization settings.", ModuleCategory.VISUALS, false));
        MODULES.add(new ClientModule("Zoom", "Hold the configured zoom key to zoom in.", ModuleCategory.VISUALS, false));
    }

    public static List<ClientModule> all() { return Collections.unmodifiableList(MODULES); }
    public static List<ClientModule> inCategory(ModuleCategory category) {
        return MODULES.stream().filter(module -> module.category() == category).toList();
    }
    public static ClientModule find(String name) {
        return MODULES.stream().filter(module -> module.name().equals(name)).findFirst().orElse(null);
    }
}

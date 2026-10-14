package com.starclient.module;

public enum ModuleCategory {
    COMBAT("Combat"), VISUALS("Visuals"), HUD("HUD"), PERFORMANCE("Performance"), SETTINGS("Settings");
    private final String title;
    ModuleCategory(String title) { this.title = title; }
    public String title() { return title; }
}

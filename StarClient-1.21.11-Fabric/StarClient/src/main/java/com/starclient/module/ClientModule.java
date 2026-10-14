package com.starclient.module;

public final class ClientModule {
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;

    public ClientModule(String name, String description, ModuleCategory category, boolean enabled) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = enabled;
    }
    public String name() { return name; }
    public String description() { return description; }
    public ModuleCategory category() { return category; }
    public boolean enabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void toggle() { this.enabled = !this.enabled; }
}

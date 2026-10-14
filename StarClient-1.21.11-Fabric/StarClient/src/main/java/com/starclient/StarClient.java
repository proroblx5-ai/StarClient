package com.starclient;

import com.starclient.config.ConfigManager;
import com.starclient.gui.ClickGuiScreen;
import com.starclient.hud.StarHud;
import com.starclient.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StarClient implements ClientModInitializer {
    public static final String MOD_ID = "starclient";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static KeyBinding clickGuiKey;

    @Override
    public void onInitializeClient() {
        ModuleManager.registerDefaults();
        ConfigManager.load();
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of(MOD_ID, "main"));
        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.starclient.clickgui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_SHIFT, category));
        StarHud.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (clickGuiKey.wasPressed()) {
                if (client.currentScreen instanceof ClickGuiScreen) client.setScreen(null);
                else if (client.currentScreen == null) client.setScreen(new ClickGuiScreen());
            }
        });
        LOGGER.info("Star Client initialized for Minecraft 1.21.11.");
    }
}

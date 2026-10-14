package com.starclient.hud;

import com.starclient.module.ClientModule;
import com.starclient.module.ModuleManager;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public final class StarHud {
    private static final Deque<Long> LEFT_CLICKS = new ArrayDeque<>();
    private static final Deque<Long> RIGHT_CLICKS = new ArrayDeque<>();
    private static boolean leftWasDown;
    private static boolean rightWasDown;
    private static long lastPoll;
    private static int leftCps;
    private static int rightCps;

    private StarHud() {}

    public static void register() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.of("starclient", "hud"),
                (context, tickCounter) -> render(context));
    }

    private static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) return;
        pollClicks(client);
        int x = 8;
        int y = 8;
        int line = 11;
        for (ClientModule module : ModuleManager.all()) {
            if (!module.enabled()) continue;
            String value = null;
            switch (module.name()) {
                case "FPS Counter" -> value = "FPS: " + client.getCurrentFps();
                case "Coordinates" -> value = String.format("XYZ: %.1f, %.1f, %.1f",
                        client.player.getX(), client.player.getY(), client.player.getZ());
                case "Keystrokes" -> {
                    long window = client.getWindow().getHandle();
                    value = "[" + key(window, GLFW.GLFW_KEY_W, "W") + "] [" + key(window, GLFW.GLFW_KEY_A, "A")
                            + "] [" + key(window, GLFW.GLFW_KEY_S, "S") + "] [" + key(window, GLFW.GLFW_KEY_D, "D")
                            + "] [" + key(window, GLFW.GLFW_KEY_SPACE, "JUMP") + "]";
                }
                case "CPS Counter" -> value = "CPS: L " + leftCps + " | R " + rightCps;
                case "Armor HUD" -> {
                    StringBuilder armor = new StringBuilder("Armor: ");
                    boolean found = false;
                    for (var stack : client.player.getArmorItems()) {
                        if (!stack.isEmpty()) {
                            if (found) armor.append("  ");
                            armor.append(stack.getName().getString()).append(" ");
                            if (stack.isDamageable()) {
                                int remaining = Math.max(0, stack.getMaxDamage() - stack.getDamage());
                                armor.append(remaining).append("/").append(stack.getMaxDamage());
                            } else armor.append("OK");
                            found = true;
                        }
                    }
                    value = found ? armor.toString() : "Armor: empty";
                }
                case "Ping Display" -> {
                    var entry = client.getNetworkHandler() == null ? null
                            : client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
                    value = "Ping: " + (entry == null ? "--" : entry.getLatency() + " ms");
                }
                case "Direction HUD" -> value = "Facing: " + client.player.getHorizontalFacing().asString().toUpperCase();
                default -> { }
            }
            if (value != null) {
                int width = client.textRenderer.getWidth(value);
                context.fill(x - 3, y - 2, x + width + 4, y + 10, 0x990A1020);
                context.fill(x - 3, y - 2, x - 1, y + 10, 0xFF29B6F6);
                context.drawTextWithShadow(client.textRenderer, Text.literal(value), x, y, 0xFFEAF7FF);
                y += line + 3;
            }
        }
    }

    private static String key(long window, int key, String label) {
        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS ? label.toLowerCase() : label;
    }

    private static void pollClicks(MinecraftClient client) {
        long now = System.currentTimeMillis();
        if (now - lastPoll < 12) return;
        lastPoll = now;
        long window = client.getWindow().getHandle();
        boolean left = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        boolean right = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        if (left && !leftWasDown) LEFT_CLICKS.addLast(now);
        if (right && !rightWasDown) RIGHT_CLICKS.addLast(now);
        leftWasDown = left;
        rightWasDown = right;
        trim(LEFT_CLICKS, now);
        trim(RIGHT_CLICKS, now);
        leftCps = LEFT_CLICKS.size();
        rightCps = RIGHT_CLICKS.size();
    }

    private static void trim(Deque<Long> clicks, long now) {
        while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000) clicks.removeFirst();
    }
}

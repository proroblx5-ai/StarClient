package com.starclient.gui;

import com.starclient.config.ConfigManager;
import com.starclient.module.ClientModule;
import com.starclient.module.ModuleCategory;
import com.starclient.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public final class ClickGuiScreen extends Screen {
    private ModuleCategory selected = ModuleCategory.HUD;
    private final List<HitBox> hitBoxes = new ArrayList<>();
    private int panelX, panelY, panelW, panelH;

    public ClickGuiScreen() { super(Text.literal("Star Client")); }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x88000000);
        panelW = Math.min(590, width - 24);
        panelH = Math.min(390, height - 24);
        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;
        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xF20B1020);
        context.fill(panelX, panelY, panelX + panelW, panelY + 3, 0xFF29B6F6);
        context.drawTextWithShadow(textRenderer, Text.literal("✦ STAR CLIENT"), panelX + 18, panelY + 14, 0xFF54D6FF);
        context.drawTextWithShadow(textRenderer, Text.literal("Minecraft 1.21.11  •  Fabric"), panelX + 18, panelY + 29, 0xFF9AAFC4);
        context.drawTextWithShadow(textRenderer, Text.literal("LEFT SHIFT / ESC TO CLOSE"), panelX + panelW - 174, panelY + 19, 0xFF9AAFC4);
        hitBoxes.clear();

        int tabX = panelX + 14;
        int tabY = panelY + 55;
        for (ModuleCategory category : ModuleCategory.values()) {
            int tabW = 98;
            boolean active = selected == category;
            context.fill(tabX, tabY, tabX + tabW - 5, tabY + 25, active ? 0xFF173B58 : 0xFF151D2D);
            if (active) context.fill(tabX, tabY + 22, tabX + tabW - 5, tabY + 25, 0xFF29B6F6);
            context.drawTextWithShadow(textRenderer, Text.literal(category.title()), tabX + 8, tabY + 8, active ? 0xFFFFFFFF : 0xFFB6C5D8);
            hitBoxes.add(new HitBox(tabX, tabY, tabW - 5, 25, () -> selected = category));
            tabX += tabW;
        }

        int contentX = panelX + 15;
        int contentY = panelY + 92;
        int cardW = (panelW - 45) / 2;
        int cardH = 53;
        int index = 0;
        for (ClientModule module : ModuleManager.inCategory(selected)) {
            int col = index % 2;
            int row = index / 2;
            int x = contentX + col * (cardW + 12);
            int y = contentY + row * (cardH + 9);
            if (y + cardH > panelY + panelH - 28) break;
            int bg = module.enabled() ? 0xFF142B3E : 0xFF151C2A;
            context.fill(x, y, x + cardW, y + cardH, bg);
            context.fill(x, y, x + 3, y + cardH, module.enabled() ? 0xFF29B6F6 : 0xFF374357);
            context.drawTextWithShadow(textRenderer, Text.literal(module.name()), x + 11, y + 9,
                    module.enabled() ? 0xFFFFFFFF : 0xFFBBC5D3);
            context.drawTextWithShadow(textRenderer, Text.literal(module.enabled() ? "ON" : "OFF"), x + cardW - 32, y + 9,
                    module.enabled() ? 0xFF55D6A7 : 0xFF7F8A9C);
            String description = module.description();
            int maxChars = Math.max(12, (cardW - 20) / 5);
            if (description.length() > maxChars) description = description.substring(0, maxChars - 1) + "…";
            context.drawText(textRenderer, Text.literal(description), x + 11, y + 29, 0xFF8FA1B8, false);
            hitBoxes.add(new HitBox(x, y, cardW, cardH, () -> {
                module.toggle();
                ConfigManager.save();
            }));
            index++;
        }
        context.drawText(textRenderer, Text.literal("Click a module to toggle it  •  Settings save automatically"),
                panelX + 17, panelY + panelH - 17, 0xFF7F91A8, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (HitBox hitBox : hitBoxes) {
                if (hitBox.contains(mouseX, mouseY)) {
                    hitBox.action.run();
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 || keyCode == 340 || keyCode == 344) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private record HitBox(int x, int y, int w, int h, Runnable action) {
        boolean contains(double mouseX, double mouseY) {
            return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
        }
    }
}

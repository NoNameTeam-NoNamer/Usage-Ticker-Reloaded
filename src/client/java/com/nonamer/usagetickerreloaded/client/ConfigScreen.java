package com.nonamer.usagetickerreloaded.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private EditBox textField;

    public ConfigScreen(Screen parent) {
        super(Component.literal("Usage Ticker Reloaded Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        String content = "";
        Path configPath = Paths.get("config/usage-ticker-reloaded/custom_display.txt");
        if (Files.exists(configPath)) {
            try {
                content = new String(Files.readAllBytes(configPath));
            } catch (IOException ignored) {}
        }

        this.textField = new EditBox(
                this.font,
                this.width / 2 - 100,
                this.height / 2 - 20,
                200,
                20,
                Component.literal("")
        );
        this.textField.setMaxLength(256);
        this.textField.setValue(content);

        this.addRenderableWidget(this.textField);

        this.addRenderableWidget(
                Button.builder(Component.literal("Save"), _ -> {
                    saveConfig(this.textField.getValue());
                    this.minecraft.gui.setScreen(this.parent);
                }).bounds(this.width / 2 - 50, this.height / 2 + 20, 100, 20).build()
        );
        this.addRenderableWidget(
                Button.builder(Component.literal("Cancel"), _ -> this.minecraft.gui.setScreen(this.parent)).bounds(this.width / 2 - 50, this.height / 2 + 50, 100, 20).build()
        );
    }

    private void saveConfig(String text) {
        Path configPath = Paths.get("config/usage-ticker-reloaded/custom_display.txt");
        try {
            if (text == null || text.trim().isEmpty()) {
                Files.deleteIfExists(configPath);
                UsageTickerReloadedClient.customDisplayText = null;
            } else {
                Files.createDirectories(configPath.getParent());
                Files.write(configPath, text.getBytes());
                UsageTickerReloadedClient.customDisplayText = text;
            }
        } catch (IOException ignored) {}
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        graphics.fill(0, 0, this.width, this.height, 0x80000000);
        graphics.text(
                this.font,
                Component.literal("Enter custom text (leave empty for default count):"),
                this.width / 2 - 150,
                this.height / 2 - 50,
                0xFFFFFF,
                true
        );
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }
}
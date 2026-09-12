package com.nonamer.usagetickerreloaded.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return isYaclPresent() ? ConfigScreen::create : MissingDependencyScreen::new;
    }

    private boolean isYaclPresent() {
        try{
            Class.forName("dev.isxander.yacl3.api.YetAnotherConfigLib");
            return true;
        }catch(ClassNotFoundException e){return false;}
    }

    private static class MissingDependencyScreen extends Screen {
        private final Screen parent;

        protected MissingDependencyScreen(Screen parent) {
            super(Component.literal("Missing Dependency"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            this.addRenderableWidget(
                    Button.builder(Component.literal(""), _ -> this.minecraft.gui.setScreen(parent))
                            .bounds(0, 0, this.width, this.height)
                            .build()
            );
        }

        @Override
        public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
            graphics.fill(0, 0, this.width, this.height, 0x80000000);
            Component msg = Component.literal("Please install Yet Another Config Lib mod to use the config GUI.");
            graphics.text(font, this.title, (this.width-font.width(this.title.getString()))/2, 40, 0xFFFFFFFF, true);
            graphics.text(font, msg, (this.width-font.width(msg.getString()))/2, this.height/2, 0xFFFFFFFF, true);
        }
    }
}
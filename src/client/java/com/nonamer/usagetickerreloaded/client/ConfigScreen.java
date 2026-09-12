package com.nonamer.usagetickerreloaded.client;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen {
    private static final String PREFIX = "yacl3.config.usage-ticker-reloaded.";

    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable(PREFIX + "title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable(PREFIX + "general"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable(PREFIX + "useCommaSeparator.name"))
                                .description(OptionDescription.of(
                                        Component.translatable(PREFIX + "useCommaSeparator.description")))
                                .binding(
                                        false,
                                        () -> UsageTickerReloadedClient.config.useCommaSeparator,
                                        val -> UsageTickerReloadedClient.config.useCommaSeparator = val
                                )
                                .controller(opt -> BooleanControllerBuilder.create(opt)
                                        .coloured(true)
                                )
                                .build()
                        )
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable(PREFIX + "matchNbt.name"))
                                .description(OptionDescription.of(
                                        Component.translatable(PREFIX + "matchNbt.description")))
                                .binding(
                                        false,
                                        () -> UsageTickerReloadedClient.config.matchNbt,
                                        val -> UsageTickerReloadedClient.config.matchNbt = val
                                )
                                .controller(opt -> BooleanControllerBuilder.create(opt)
                                        .coloured(true)
                                )
                                .build()
                        )
                        .option(Option.<String>createBuilder()
                                .name(Component.translatable(PREFIX + "customText.name"))
                                .description(OptionDescription.of(
                                        Component.translatable(PREFIX + "customText.description")))
                                .binding(
                                        "",
                                        () -> UsageTickerReloadedClient.config.customText,
                                        val -> UsageTickerReloadedClient.config.customText = val
                                )
                                .controller(StringControllerBuilder::create)
                                .build()
                        )
                        .build()
                )
                .save(() -> {
                    UsageTickerReloadedClient.config.save();
                    UsageTickerReloadedClient.customDisplayText =
                            UsageTickerReloadedClient.config.customText.isEmpty()
                                    ? null
                                    : UsageTickerReloadedClient.config.customText;
                })
                .build()
                .generateScreen(parent);
    }
}
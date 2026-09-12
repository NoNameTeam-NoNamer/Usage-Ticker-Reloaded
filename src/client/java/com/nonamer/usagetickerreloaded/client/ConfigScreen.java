package com.nonamer.usagetickerreloaded.client;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.awt.Color;

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
                                        true,
                                        () -> UsageTickerReloadedClient.config.matchNbt,
                                        val -> UsageTickerReloadedClient.config.matchNbt = val
                                )
                                .controller(opt -> BooleanControllerBuilder.create(opt)
                                        .coloured(true)
                                )
                                .build()
                        )
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable(PREFIX + "containerDepthLimit.name"))
                                .description(OptionDescription.of(
                                        Component.translatable(PREFIX + "containerDepthLimit.description")))
                                .binding(
                                        5,
                                        () -> UsageTickerReloadedClient.config.containerDepthLimit,
                                        val -> UsageTickerReloadedClient.config.containerDepthLimit = val
                                )
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 32)
                                        .step(1)
                                )
                                .build()
                        )
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable(PREFIX + "containerNodeLimit.name"))
                                .description(OptionDescription.of(
                                        Component.translatable(PREFIX + "containerNodeLimit.description")))
                                .binding(
                                        10000,
                                        () -> UsageTickerReloadedClient.config.containerNodeLimit,
                                        val -> UsageTickerReloadedClient.config.containerNodeLimit = val
                                )
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(0, 100000)
                                        .step(1000)
                                )
                                .build()
                        )
                        .option(Option.<Color>createBuilder()
                                .name(Component.translatable(PREFIX + "mainCounterColor.name"))
                                .description(OptionDescription.of(
                                        Component.translatable(PREFIX + "mainCounterColor.description")))
                                .binding(
                                        new Color(0xFFFFFFFF, true),
                                        () -> new Color(UsageTickerReloadedClient.config.mainCounterColor, true),
                                        val -> UsageTickerReloadedClient.config.mainCounterColor = val.getRGB()
                                )
                                .controller(opt -> ColorControllerBuilder.create(opt)
                                        .allowAlpha(true)
                                )
                                .build()
                        )
                        .option(Option.<Color>createBuilder()
                                .name(Component.translatable(PREFIX + "nbtCounterColor.name"))
                                .description(OptionDescription.of(
                                        Component.translatable(PREFIX + "nbtCounterColor.description")))
                                .binding(
                                        new Color(0xFF976997, true),
                                        () -> new Color(UsageTickerReloadedClient.config.nbtCounterColor, true),
                                        val -> UsageTickerReloadedClient.config.nbtCounterColor = val.getRGB()
                                )
                                .controller(opt -> ColorControllerBuilder.create(opt)
                                        .allowAlpha(true)
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
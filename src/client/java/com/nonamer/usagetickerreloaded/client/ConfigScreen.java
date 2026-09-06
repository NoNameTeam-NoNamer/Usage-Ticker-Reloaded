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
    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Usage Ticker Reloaded Config"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("General"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Use Comma Separator"))
                                .description(OptionDescription.of(
                                        Component.literal("Use ',' instead of '.' for decimal separator.")))
                                .binding(
                                        false, // 默认值
                                        () -> UsageTickerReloadedClient.config.useCommaSeparator,
                                        val -> UsageTickerReloadedClient.config.useCommaSeparator = val
                                )
                                .controller(opt -> BooleanControllerBuilder.create(opt)
                                        .coloured(true) // 绿色/红色开关
                                )
                                .build()
                        )
                        .option(Option.<String>createBuilder()
                                .name(Component.literal("Debug Display Text"))
                                .description(OptionDescription.of(
                                        Component.literal("Enter a number to test formatting (e.g. 1234567 → 1.23M). Leave empty for default.")))
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
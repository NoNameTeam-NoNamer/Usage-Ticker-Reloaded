package com.nonamer.usagetickerreloaded.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;

import com.nonamer.usagetickerreloaded.UsageTickerReloaded;


@Environment(EnvType.CLIENT)
public class UsageTickerReloadedClient implements ClientModInitializer {

	public static String customDisplayText = null;

	private void loadCustomDisplayText() {
		Path configPath = Paths.get("config/usage-ticker-reloaded/custom_display.txt");
		if (Files.exists(configPath)) {
			try {
				customDisplayText = new String(Files.readAllBytes(configPath));
			} catch (IOException ignored) {
				customDisplayText = null;
			}
		} else {
			customDisplayText = null;
		}
	}
	// =========Customize available in the future!==========
	private static final int ITEM_SIZE = 20;
	private static final int TEXT_OFFSET = 0;

	@Override
	public void onInitializeClient() {
		//For debug
		loadCustomDisplayText();
		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.fromNamespaceAndPath(UsageTickerReloaded.MOD_ID, "item_counter"),
				(context, _) -> {
					Minecraft client = Minecraft.getInstance();
					Player player = client.player;
					if (player == null) return;

					ItemStack mainHand = player.getMainHandItem();
					ItemStack offHand = player.getOffhandItem();

					if (mainHand.isEmpty() && offHand.isEmpty()) return;

					ItemStack mainItem = mainHand;
					ItemStack offItem = offHand;

					if (mainItem.isEmpty() && !offItem.isEmpty()) {
						mainItem = offItem;
						offItem = ItemStack.EMPTY;
					}

					if (!offItem.isEmpty() && ItemStack.isSameItem(mainItem, offItem)) {
						offItem = ItemStack.EMPTY;
					}

					int mainCount = countItems(player, mainItem);
					int offCount = offItem.isEmpty() ? 0 : countItems(player, offItem);

					int screenWidth = context.guiWidth();
					int screenHeight = context.guiHeight();
					final int HOTBAR_WIDTH = 182;

					int hotbarLeft = (screenWidth - HOTBAR_WIDTH) / 2;
					int hotbarRight = hotbarLeft + HOTBAR_WIDTH;
					int y = screenHeight - 19;

					net.minecraft.world.entity.HumanoidArm mainArm = player.getMainArm();
					boolean isRightHanded = (mainArm == net.minecraft.world.entity.HumanoidArm.RIGHT);

					int mainHandX, offHandX;
					if (isRightHanded) {
						mainHandX = hotbarRight + 6;
						offHandX = hotbarLeft - ITEM_SIZE - 30;
					} else {
						mainHandX = hotbarLeft - ITEM_SIZE;
						offHandX = hotbarRight + 34;
					}
					if (!mainItem.isEmpty()) {
						drawItemWithCount(context, mainItem, mainCount, mainHandX, y);
					}

					if (!offItem.isEmpty()) {
						drawItemWithCount(context, offItem, offCount, offHandX, y);
					}
				}
		);
	}

	private int countItems(Player player, ItemStack targetStack) {
		if (targetStack.isEmpty()) return 0;
		int total = 0;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (!stack.isEmpty() && ItemStack.isSameItem(stack, targetStack)) {
				total += stack.getCount();
			}
		}
		return total;
	}

	private void drawItemWithCount(GuiGraphicsExtractor context, ItemStack stack, int count, int x, int y) {
		context.item(stack, x, y);

		String countText;
		if (customDisplayText != null && !customDisplayText.trim().isEmpty()) {
			countText = customDisplayText;
		} else if (count > 1) {
			countText = String.valueOf(count);
		} else {
			countText = "";
		}

		if (!countText.isEmpty()) {
			var font = Minecraft.getInstance().font;
			int textWidth = font.width(countText);
			int screenWidth = context.guiWidth();
			boolean isRightSide = x > screenWidth / 2;
			int textX = x;
			if (!isRightSide) {
				textX += ITEM_SIZE - 3 - textWidth;
			}
			int textY = y + ITEM_SIZE - 11 - TEXT_OFFSET;
			context.text(font, Component.literal(countText), textX, textY, 0xFFFFFFFF, true);
		}
	}
}
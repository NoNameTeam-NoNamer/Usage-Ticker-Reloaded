package com.nonamer.usagetickerreloaded.client;

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

	// ====== 显示位置调节参数（可自行修改） ======
	private static final int ITEM_SIZE = 16;
	private static final int TEXT_OFFSET = 2;
	private static final int PADDING = 10;
	private static final int VERTICAL_OFFSET = 30;

	@Override
	public void onInitializeClient() {
		// 使用新的 Hud API 来注册你的渲染元素
		// 这里选择在聊天框之前渲染，你也可以换成其他位置[reference:2][reference:3]
		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.fromNamespaceAndPath(UsageTickerReloaded.MOD_ID, "item_counter"),
				(context, _) -> {
					// 你的 HUD 渲染逻辑
					Minecraft client = Minecraft.getInstance();
					Player player = client.player;
					if (player == null) return;

					ItemStack mainHand = player.getMainHandItem();
					ItemStack offHand = player.getOffhandItem();

					if (mainHand.isEmpty() && offHand.isEmpty()) return;

					// ---- 决定左右显示什么 ----
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

					// ---- 左侧绘制 ----
					if (!mainItem.isEmpty()) {
						drawItemWithCount(context, mainItem, mainCount, screenWidth - PADDING - ITEM_SIZE, screenHeight - VERTICAL_OFFSET);
					}

					// ---- 右侧绘制 ----
					if (!offItem.isEmpty()) {
						drawItemWithCount(context, offItem, offCount, PADDING, screenHeight - VERTICAL_OFFSET);
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
		// 绘制物品 —— 使用 item 方法
		context.item(stack, x, y);

		String countText = String.valueOf(count);
		var font = Minecraft.getInstance().font;
		int textWidth = font.width(countText);
		int textX = x + (ITEM_SIZE - textWidth) / 2;
		int textY = y + ITEM_SIZE + TEXT_OFFSET;
		context.text(font, Component.literal(countText), textX, textY, 0xFFFFFFFF, true);
	}
}
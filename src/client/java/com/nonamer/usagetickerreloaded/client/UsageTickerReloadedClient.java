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
	private static final int ITEM_SIZE = 20;
	private static final int TEXT_OFFSET = 0;

	@Override
	public void onInitializeClient() {
		// 使用新的 Hud API 来注册你的渲染元素
		// 这里选择在聊天框之前渲染，你也可以换成其他位置
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
					final int HOTBAR_WIDTH = 182;

					int hotbarLeft = (screenWidth - HOTBAR_WIDTH) / 2;
					int hotbarRight = hotbarLeft + HOTBAR_WIDTH;
					int y = screenHeight - 19;

					net.minecraft.world.entity.HumanoidArm mainArm = player.getMainArm();
					boolean isRightHanded = (mainArm == net.minecraft.world.entity.HumanoidArm.RIGHT);

					int mainHandX, offHandX;
					if (isRightHanded) {
						// 右手玩家：主手在右，副手在左
						mainHandX = hotbarRight + 10;
						offHandX = hotbarLeft - ITEM_SIZE - 30;
					} else {
						// 左手玩家：主手在左，副手在右
						mainHandX = hotbarLeft - ITEM_SIZE - 2;
						offHandX = hotbarRight + 42;
					}

// ---- 主手侧绘制 ----
					if (!mainItem.isEmpty()) {
						drawItemWithCount(context, mainItem, mainCount, mainHandX, y);
					}

// ---- 副手侧绘制 ----
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
		if(count>1){
			String countText = String.valueOf(count);
			var font = Minecraft.getInstance().font;
			int textWidth = font.width(countText);
			int textX = x + ITEM_SIZE - 3 - textWidth;
			int textY = y + ITEM_SIZE - 11 - TEXT_OFFSET;
			context.text(font, Component.literal(countText), textX, textY, 0xFFFFFFFF, true);
		}
	}
}
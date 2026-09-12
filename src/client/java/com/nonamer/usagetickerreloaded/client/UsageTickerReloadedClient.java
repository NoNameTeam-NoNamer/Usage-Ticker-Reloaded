package com.nonamer.usagetickerreloaded.client;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;

import com.nonamer.usagetickerreloaded.UsageTickerReloaded;
import net.minecraft.world.item.ItemStackTemplate;


@Environment(EnvType.CLIENT)
public class UsageTickerReloadedClient implements ClientModInitializer {

	public static Config config;
	public static String customDisplayText = null;

	private void loadConfig() {
		config = Config.load();
		customDisplayText = config.customText.isEmpty() ? null : config.customText;
	}

	private static final int ITEM_SIZE = 20;
	private static final int TEXT_OFFSET = 0;

	private record StackEntry(ItemStack stack, int depth){}
	private record ContainerCount(int count, boolean nodeTruncated, boolean depthTruncated){}

	@Override
	public void onInitializeClient() {
		loadConfig();

		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.fromNamespaceAndPath(UsageTickerReloaded.MOD_ID, "item_counter"),
				(context, _) -> {
					Player player = Minecraft.getInstance().player;
					if(player==null)return;

					ItemStack mainItem = player.getMainHandItem();
					ItemStack offItem = player.getOffhandItem();

					if(mainItem.isEmpty()&&offItem.isEmpty())return;

					if(!offItem.isEmpty()&&(config.matchNbt
							? ItemStack.isSameItemSameComponents(mainItem, offItem)
							: ItemStack.isSameItem(mainItem, offItem))) {
						offItem = ItemStack.EMPTY;
					}

					int hotbarLeft = (context.guiWidth()-182)/2;
					int hotbarRight = hotbarLeft+182;
					int y = context.guiHeight()-19;
					boolean isRightHanded = player.getMainArm()==HumanoidArm.RIGHT;

					if(!mainItem.isEmpty()) {
						drawItemWithCount(context, mainItem, countItems(player, mainItem),
								countItemsInContainers(player, mainItem),
								isRightHanded ? hotbarRight+6 : hotbarLeft-ITEM_SIZE, y);
					}

					if(!offItem.isEmpty()) {
						drawItemWithCount(context, offItem, countItems(player, offItem),
								countItemsInContainers(player, offItem),
								isRightHanded ? hotbarLeft-ITEM_SIZE-30 : hotbarRight+34, y);
					}
				}
		);
	}

	private int countItems(Player player, ItemStack targetStack) {
		if(targetStack.isEmpty())return 0;
		int total = 0;
		for(int i = 0; i<player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if(!stack.isEmpty()) {
				boolean same = config.matchNbt
						? ItemStack.isSameItemSameComponents(stack, targetStack)
						: ItemStack.isSameItem(stack, targetStack);
				if(same)total += stack.getCount();
			}
		}
		return total;
	}

	private ContainerCount countItemsInContainers(Player player, ItemStack targetStack) {
		if(targetStack.isEmpty()||config.containerDepthLimit<=0||config.containerNodeLimit<=0) {
			return new ContainerCount(0, false, false);
		}

		int total = 0;
		int visited = 0;
		boolean nodeTruncated = false;
		boolean depthTruncated = false;
		ArrayDeque<StackEntry> stack = new ArrayDeque<>();

		for(int i = 0; i<player.getInventory().getContainerSize(); i++) {
			ItemStack slotItem = player.getInventory().getItem(i);
			if(slotItem.isEmpty())continue;
			for(ItemStack child : getContainerContents(slotItem)) {
				if(!child.isEmpty())stack.push(new StackEntry(child, 1));
			}
		}

		while(!stack.isEmpty()) {
			if(visited>=config.containerNodeLimit) {
				nodeTruncated = true;
				break;
			}
			visited++;

			StackEntry entry = stack.pop();
			ItemStack current = entry.stack();
			int depth = entry.depth();

			boolean same = config.matchNbt
					? ItemStack.isSameItemSameComponents(current, targetStack)
					: ItemStack.isSameItem(current, targetStack);
			if(same)total += current.getCount();

			var children = getContainerContents(current);
			if(!children.isEmpty()) {
				if(depth<config.containerDepthLimit) {
					for(ItemStack child : children) {
						if(!child.isEmpty())stack.push(new StackEntry(child, depth+1));
					}
				} else {
					depthTruncated = true;
				}
			}
		}

		return new ContainerCount(total, nodeTruncated, depthTruncated);
	}

	private List<ItemStack> getContainerContents(ItemStack stack) {
		List<ItemStack> result = new ArrayList<>();
		var container = stack.get(DataComponents.CONTAINER);
		if(container!=null) {
			for(ItemStackTemplate inner : container.nonEmptyItems()) {
				if(!inner.create().isEmpty())result.add(inner.create());
			}
		}
		var bundle = stack.get(DataComponents.BUNDLE_CONTENTS);
		if(bundle!=null) {
			for(ItemStackTemplate inner : bundle.items()) {
				if(!inner.create().isEmpty())result.add(inner.create());
			}
		}
		return result;
	}

	private String formatCount(Number num) {
		if(num==null)return "0";

		String str;
		boolean negative = false;
		if(num instanceof BigInteger big) {
			if(big.signum()<0) {
				negative = true;
				big = big.abs();
			}
			str = big.toString();
		}else{
			long val = num.longValue();
			if(val<0) {
				negative = true;
				val = -val;
			}
			str = Long.toString(val);
		}

		int len = str.length();
		if(len<=4)return (negative ? "-" : "")+str;

		String[] units = {"", "k", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q"};
		int unitIndex = (len-1)/3;
		if(unitIndex>=units.length)return formatScientific(str, negative);

		int intDigits = len-unitIndex*3;
		int decimalDigits = Math.max(0, 3-intDigits);
		if(intDigits+decimalDigits>len)decimalDigits = len-intDigits;

		String intPart = str.substring(0, intDigits);
		String decPart = decimalDigits>0
				? str.substring(intDigits, intDigits+decimalDigits)
				: "";

		StringBuilder result = new StringBuilder();
		if(negative)result.append("-");
		result.append(intPart);
		if(!decPart.isEmpty()) {
			result.append(config.useCommaSeparator ? "," : ".").append(decPart);
		}
		result.append(units[unitIndex]);
		return result.toString();
	}

	private String formatScientific(String str, boolean negative) {
		String mantissa = str.length()>1 ? str.charAt(0)+"."+str.charAt(1) : str;
		return (negative ? "-" : "")+mantissa+"e"+(str.length()-1);
	}

	private void drawItemWithCount(GuiGraphicsExtractor context, ItemStack stack, int count, ContainerCount nbt, int x, int y) {
		context.item(stack, x, y);

		var font = Minecraft.getInstance().font;
		boolean isRightSide = x>context.guiWidth()/2;
		int textY = y+ITEM_SIZE-11-TEXT_OFFSET;
		boolean useCustom = customDisplayText!=null&&!customDisplayText.trim().isEmpty();

		String countText = "";
		if(useCustom) {
			try{countText = formatCount(new BigInteger(customDisplayText.trim()));}
			catch(NumberFormatException e){countText = customDisplayText;}
		}else if(count!=0&&count!=1) {
			countText = formatCount(count);
		}

		if(!countText.isEmpty()) {
			int textX = isRightSide ? x : x+ITEM_SIZE-3-font.width(countText);
			context.text(font, Component.literal(countText), textX, textY, config.mainCounterColor, true);
		}

		if(!useCustom&&(nbt.count()!=0||nbt.depthTruncated()||nbt.nodeTruncated())) {
			String nbtText = (nbt.depthTruncated() ? "*" : "") + formatCount(nbt.count()) + (nbt.nodeTruncated() ? "+" : "");
			int nbtX = isRightSide ? x : x+ITEM_SIZE-3-font.width(nbtText);
			context.text(font, Component.literal(nbtText), nbtX, textY-10, config.nbtCounterColor, true);
		}
	}
}
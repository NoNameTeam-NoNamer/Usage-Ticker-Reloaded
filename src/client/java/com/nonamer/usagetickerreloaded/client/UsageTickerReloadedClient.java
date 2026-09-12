package com.nonamer.usagetickerreloaded.client;

import java.math.BigInteger;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;

import com.nonamer.usagetickerreloaded.UsageTickerReloaded;


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
							: ItemStack.isSameItem(mainItem, offItem))){
						offItem = ItemStack.EMPTY;
					}

					int hotbarLeft = (context.guiWidth()-182)/2;
					int hotbarRight = hotbarLeft+182;
					int y = context.guiHeight()-19;
					boolean isRightHanded = player.getMainArm()==HumanoidArm.RIGHT;

					if(!mainItem.isEmpty()){
						drawItemWithCount(context, mainItem, countItems(player, mainItem),
								isRightHanded ? hotbarRight+6 : hotbarLeft-ITEM_SIZE, y);
					}

					if(!offItem.isEmpty()){
						drawItemWithCount(context, offItem, countItems(player, offItem),
								isRightHanded ? hotbarLeft-ITEM_SIZE-30 : hotbarRight+34, y);
					}
				}
		);
	}

	private int countItems(Player player, ItemStack targetStack) {
		if(targetStack.isEmpty())return 0;
		int total = 0;
		for(int i = 0; i<player.getInventory().getContainerSize(); i++){
			ItemStack stack = player.getInventory().getItem(i);
			if(!stack.isEmpty()){
				boolean same = config.matchNbt
						? ItemStack.isSameItemSameComponents(stack, targetStack)
						: ItemStack.isSameItem(stack, targetStack);
				if(same)total += stack.getCount();
			}
		}
		return total;
	}

	private String formatCount(Number num) {
		if(num==null)return "0";

		String str;
		boolean negative = false;
		if(num instanceof BigInteger big){
			if(big.signum()<0){
				negative = true;
				big = big.abs();
			}
			str = big.toString();
		}else{
			long val = num.longValue();
			if(val<0){
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
		if(!decPart.isEmpty()){
			result.append(config.useCommaSeparator ? "," : ".").append(decPart);
		}
		result.append(units[unitIndex]);
		return result.toString();
	}

	private String formatScientific(String str, boolean negative) {
		String mantissa = str.length()>1 ? str.charAt(0)+"."+str.charAt(1) : str;
		return (negative ? "-" : "")+mantissa+"e"+(str.length()-1);
	}

	private void drawItemWithCount(GuiGraphicsExtractor context, ItemStack stack, int count, int x, int y) {
		context.item(stack, x, y);

		String countText = "";
		if(customDisplayText!=null&&!customDisplayText.trim().isEmpty()){
			try{countText = formatCount(new BigInteger(customDisplayText.trim()));}
			catch(NumberFormatException e){countText = customDisplayText;}
		}else if(count>1){
			countText = formatCount(count);
		}

		if(countText.isEmpty())return;

		var font = Minecraft.getInstance().font;
		int textX = x>context.guiWidth()/2
				? x+3
				: x+ITEM_SIZE-3-font.width(countText);
		context.text(font, Component.literal(countText), textX,
				y+ITEM_SIZE-11-TEXT_OFFSET, 0xFFFFFFFF, true);
	}
}
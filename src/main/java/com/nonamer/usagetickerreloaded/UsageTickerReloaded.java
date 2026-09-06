package com.nonamer.usagetickerreloaded;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class UsageTickerReloaded implements ModInitializer {
	public static final String MOD_ID = "usage-ticker-reloaded";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		LOGGER.error("This is a client mod!It won't do anything in the server side!");
	}
}

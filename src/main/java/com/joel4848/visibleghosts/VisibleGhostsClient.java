package com.joel4848.visibleghosts;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisibleGhostsClient implements ClientModInitializer {
    public static final String MOD_ID = "visibleghosts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Visible Ghosts mod loaded!");
    }
}
package com.joel4848.visibleghosts;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod initializer.
 * Since this mod is entirely client-side, we do not need to do anything here.
 */
public class Visibleghosts implements ModInitializer {
    public static final String MOD_ID = "visible-ghosts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Nothing needed here — client-specific initialization happens in VisibleGhostsClient
        LOGGER.info("VisibleGhosts mod loaded (server-side entrypoint does nothing).");
    }
}

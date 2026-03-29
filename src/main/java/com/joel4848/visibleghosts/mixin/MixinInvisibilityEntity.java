package com.joel4848.visibleghosts.mixin;

import com.joel4848.visibleghosts.config.ModConfig;
import com.joel4848.visibleghosts.network.ClientNetworking;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Intercepts {@code Entity#isInvisibleTo(PlayerEntity)} on the client.
 *
 * Decision logic (in priority order):
 *  1. If the server has set an override of VISIBLE  → return false (entity IS visible).
 *  2. If the server has set an override of INVISIBLE → do nothing (let vanilla decide,
 *     which will return true = invisible since we are not on the same team / spectating).
 *  3. If there is no server override (NONE)          → fall back to the player's own
 *     local config setting (existing behaviour).
 */
@Mixin(net.minecraft.entity.Entity.class)
public class MixinInvisibilityEntity {

    @Inject(method = "isInvisibleTo",
            at = @At("HEAD"),
            cancellable = true)
    private void overrideIsInvisibleToPlayer(
            net.minecraft.entity.player.PlayerEntity player,
            CallbackInfoReturnable<Boolean> cir) {

        OverrideState override = ClientNetworking.getCurrentOverride();

        switch (override) {
            case VISIBLE -> {
                // Server says: always show invisible entities to this client.
                cir.setReturnValue(false);
            }
            case INVISIBLE -> {
                // Server says: keep invisible entities hidden.
                // Do NOT cancel; let vanilla's own logic run, which will correctly
                // return true (invisible) for players not on the same team.
            }
            case NONE -> {
                // No server override – honour the player's local setting.
                if (ModConfig.getInstance().isRenderInvisiblePlayers()) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
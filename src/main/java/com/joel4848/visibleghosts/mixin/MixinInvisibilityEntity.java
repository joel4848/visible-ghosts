package com.joel4848.visibleghosts.mixin;

import com.joel4848.visibleghosts.config.ModConfig;
import com.joel4848.visibleghosts.network.ClientNetworking;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.entity.Entity.class)
public class MixinInvisibilityEntity {

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
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
                // Do not cancel; let vanilla's own logic run.
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

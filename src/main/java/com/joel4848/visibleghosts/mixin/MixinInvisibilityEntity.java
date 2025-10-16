package com.joel4848.visibleghosts.mixin;

import com.joel4848.visibleghosts.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.entity.Entity.class)
public class MixinInvisibilityEntity {
    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void overrideIsInvisibleToPlayer(net.minecraft.entity.player.PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.getInstance().isRenderInvisiblePlayers()) {
            cir.setReturnValue(false);
        }
    }
}
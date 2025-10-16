package com.joel4848.visibleghosts.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntityRenderer.class)
public class MixinInvisibilityAlpha {
    @ModifyConstant(
            method = "render",
            constant = @Constant(floatValue = 0.15F, ordinal = 0),
            require = 0
    )
    private float overrideInvisibilityAlpha(float original) {
        return 0.65F;
    }
}
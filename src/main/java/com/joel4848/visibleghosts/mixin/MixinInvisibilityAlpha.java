package com.joel4848.visibleghosts.mixin;

import com.joel4848.visibleghosts.config.ModConfig;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LivingEntityRenderer.class)
public class MixinInvisibilityAlpha {
    @ModifyConstant(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            constant = @Constant(intValue = 654311423)
    )
    private int modifyInvisibilityAlpha(int constant) {
        int alpha = ModConfig.getInstance().getGhostTransparency();
        return ColorHelper.Argb.getArgb(alpha, 255, 255, 255);
    }
}
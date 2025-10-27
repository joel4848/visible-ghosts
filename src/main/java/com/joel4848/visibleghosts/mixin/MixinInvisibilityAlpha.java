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
            method = "render*",
            constant = @Constant(intValue = 654311423),
            require = 0
    )
    private int modifyInvisibilityAlpha(int constant) {
        int alpha = ModConfig.getInstance().getGhostTransparency();
        return ColorHelper.getArgb(alpha, 255, 255, 255);
    }
}
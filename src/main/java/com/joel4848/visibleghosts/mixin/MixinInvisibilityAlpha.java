package com.joel4848.visibleghosts.mixin;

import com.joel4848.visibleghosts.config.ModConfig;
import com.joel4848.visibleghosts.network.ClientNetworking;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
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
        OverrideState override = ClientNetworking.getCurrentOverride();

        boolean shouldShowTransparent = switch (override) {
            case VISIBLE   -> true;   // Server forces visible.
            case INVISIBLE -> false;  // Server forces invisible; keep vanilla alpha.
            case NONE      -> ModConfig.getInstance().isRenderInvisiblePlayers();
        };

        if (!shouldShowTransparent) {
            return constant; // Return vanilla value unchanged.
        }

        int alpha = ModConfig.getInstance().getGhostTransparency();
        // Use ColorHelper.getArgb as required by 1.21.11 yarn mappings.
        return ColorHelper.getArgb(alpha, 255, 255, 255);
    }
}

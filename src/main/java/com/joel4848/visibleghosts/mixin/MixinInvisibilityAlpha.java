package com.joel4848.visibleghosts.mixin;

import com.joel4848.visibleghosts.config.ModConfig;
import com.joel4848.visibleghosts.network.ClientNetworking;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Adjusts the alpha value used when rendering an entity that has the
 * Invisibility effect.
 *
 * Vanilla uses the constant 654311423 (0x27FFFFFF – alpha ≈ 39/255, very faint).
 * We replace it with the user-configured transparency, but only when the
 * current state (server override or local config) means invisible players
 * are being shown.  If the server override is INVISIBLE we leave the alpha
 * untouched (vanilla's near-zero alpha keeps the entity effectively invisible
 * to teammates / spectators if those edge cases somehow reach this code path,
 * though in practice MixinInvisibilityEntity will have already prevented the
 * render entirely).
 */
@Mixin(LivingEntityRenderer.class)
public class MixinInvisibilityAlpha {

    @ModifyConstant(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            constant = @Constant(intValue = 654311423)
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
        return ColorHelper.Argb.getArgb(alpha, 255, 255, 255);
    }
}
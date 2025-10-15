package com.joel4848.visibleghosts.mixin;

import com.joel4848.visibleghosts.config.VisibleGhostsConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Redirect the invisibility check so that when the config says to render invisible players,
 * invisible players are considered visible to the local player (returns false -> render).
 */
@Mixin(LivingEntityRenderer.class)
public abstract class MixinRenderLivingBase {

    /**
     * Redirects calls to LivingEntity.isInvisibleTo(PlayerEntity) inside
     * LivingEntityRenderer.render() to our custom check.
     */
    @Redirect(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;isInvisibleTo(Lnet/minecraft/entity/player/PlayerEntity;)Z"
            ),
            require = 0
    )
    private boolean redirectedIsInvisibleToPlayer(LivingEntity living, PlayerEntity player) {
        // Only override for player entities that currently have the invisibility effect,
        // and only if the config enables rendering invisible players.
        if (VisibleGhostsConfig.get().isRenderInvisiblePlayers()
                && living instanceof PlayerEntity
                && living.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            // Return false -> entity is not invisible to player -> it will be rendered.
            return false;
        }

        // Default vanilla behavior
        return living.isInvisibleTo(player);
    }
}

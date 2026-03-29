package com.joel4848.visibleghosts.command;

import com.joel4848.visibleghosts.network.ServerNetworking;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import com.joel4848.visibleghosts.server.OverrideManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;

public final class ServerVisibleGhostsCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    registerUnder(dispatcher, "visibleghostsadmin");
                    registerUnder(dispatcher, "vga");
                });
    }

    private static void registerUnder(CommandDispatcher<ServerCommandSource> dispatcher,
                                      String root) {
        dispatcher.register(
                CommandManager.literal(root)
                        .requires(source -> source.hasPermissionLevel(1))
                        .then(CommandManager.literal("overrideVisibility")
                                .then(CommandManager.argument("targets", EntityArgumentType.players())
                                        .then(CommandManager.literal("visible")
                                                .executes(ctx -> applyOverride(ctx, OverrideState.VISIBLE)))
                                        .then(CommandManager.literal("invisible")
                                                .executes(ctx -> applyOverride(ctx, OverrideState.INVISIBLE)))
                                        .then(CommandManager.literal("none")
                                                .executes(ctx -> applyOverride(ctx, OverrideState.NONE)))
                                )
                        )
        );
    }

    private static int applyOverride(CommandContext<ServerCommandSource> ctx, OverrideState state)
            throws com.mojang.brigadier.exceptions.CommandSyntaxException {

        Collection<ServerPlayerEntity> targets =
                EntityArgumentType.getPlayers(ctx, "targets");

        for (ServerPlayerEntity player : targets) {
            OverrideManager.setOverride(player.getUuid(), state);
            ServerNetworking.sendOverrideTo(player, state);
        }

        String stateLabel = switch (state) {
            case VISIBLE   -> "visible (transparent)";
            case INVISIBLE -> "invisible (fully hidden)";
            case NONE      -> "none (player's own setting)";
        };

        Text feedback;
        if (targets.size() == 1) {
            ServerPlayerEntity only = targets.iterator().next();
            feedback = Text.literal("[Visible Ghosts] ").formatted(Formatting.LIGHT_PURPLE)
                    .append(Text.literal("Set override for ").formatted(Formatting.AQUA))
                    .append(Text.literal(only.getNameForScoreboard()).formatted(Formatting.YELLOW))
                    .append(Text.literal(" to ").formatted(Formatting.AQUA))
                    .append(Text.literal(stateLabel).formatted(Formatting.GOLD));
        } else {
            feedback = Text.literal("[Visible Ghosts] ").formatted(Formatting.LIGHT_PURPLE)
                    .append(Text.literal("Set override for ").formatted(Formatting.AQUA))
                    .append(Text.literal(targets.size() + " players").formatted(Formatting.YELLOW))
                    .append(Text.literal(" to ").formatted(Formatting.AQUA))
                    .append(Text.literal(stateLabel).formatted(Formatting.GOLD));
        }

        ctx.getSource().sendFeedback(() -> feedback, true);
        return targets.size();
    }

    private ServerVisibleGhostsCommand() {}
}
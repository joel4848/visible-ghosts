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

/**
 * Registers the server-side sub-command:
 *
 *   /visibleghosts overrideVisibility <targets> <visible|invisible|none>
 *
 * Permission level: 1 (operator-like; the lowest OP tier).
 *
 * <ul>
 *   <li>{@code visible}   – force the targeted player(s) to see invisible players as transparent,
 *                           regardless of their own client settings.</li>
 *   <li>{@code invisible} – force the targeted player(s) to see invisible players as fully
 *                           invisible, regardless of their own client settings.</li>
 *   <li>{@code none}      – clear the server override; the player's own client setting takes
 *                           effect again (or vanilla behaviour if they don't have the mod).</li>
 * </ul>
 *
 * The override is pushed to the client immediately via a custom S2C packet.
 * If the targeted player's client does not have the mod installed, the packet
 * is silently ignored and the override has no visual effect on their end.
 */
public final class ServerVisibleGhostsCommand {

    /**
     * Registers the command via {@link CommandRegistrationCallback}.
     * Call this from the server mod initializer.
     */
    public static void register() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> registerCommand(dispatcher));
    }

    private static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("visibleghostsadmin")
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
            // Push the new state to the player's client immediately.
            ServerNetworking.sendOverrideTo(player, state);
        }

        // Feedback to the command sender.
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

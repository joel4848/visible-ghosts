package com.joel4848.visibleghosts.command;

import com.joel4848.visibleghosts.config.ModConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class VisibleGhostsCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("visibleghosts")
                        .then(literal("renderInvisiblePlayers")
                                .executes(VisibleGhostsCommand::getRenderInvisiblePlayers)
                                .then(argument("true|false", BoolArgumentType.bool())
                                        .executes(VisibleGhostsCommand::setRenderInvisiblePlayers)))
                        .then(literal("ghostTransparency")
                                .executes(VisibleGhostsCommand::getGhostTransparency)
                                .then(argument("0-255", IntegerArgumentType.integer(0, 255))
                                        .executes(VisibleGhostsCommand::setGhostTransparency)))
        );
    }

    private static int getRenderInvisiblePlayers(CommandContext<FabricClientCommandSource> context) {
        boolean enabled = ModConfig.getInstance().isRenderInvisiblePlayers();

        Text message = Text.literal("[Visible Ghosts] ").formatted(Formatting.LIGHT_PURPLE)
                .append(Text.literal("Render invisible players is ").formatted(Formatting.AQUA))
                .append(Text.literal(enabled ? "enabled" : "disabled")
                        .formatted(enabled ? Formatting.GREEN : Formatting.RED));

        context.getSource().sendFeedback(message);
        return 1;
    }

    private static int setRenderInvisiblePlayers(CommandContext<FabricClientCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "true|false");
        ModConfig.getInstance().setRenderInvisiblePlayers(enabled);

        Text message = Text.literal("[Visible Ghosts] ").formatted(Formatting.LIGHT_PURPLE)
                .append(Text.literal("Render invisible players is now ").formatted(Formatting.AQUA))
                .append(Text.literal(enabled ? "enabled" : "disabled")
                        .formatted(enabled ? Formatting.GREEN : Formatting.RED));

        context.getSource().sendFeedback(message);
        return 1;
    }

    private static int getGhostTransparency(CommandContext<FabricClientCommandSource> context) {
        int value = ModConfig.getInstance().getGhostTransparency();

        Text message = Text.literal("[Visible Ghosts] ").formatted(Formatting.LIGHT_PURPLE)
                .append(Text.literal("Ghost transparency is ").formatted(Formatting.AQUA))
                .append(Text.literal(String.valueOf(value)).formatted(Formatting.GOLD));

        context.getSource().sendFeedback(message);
        return 1;
    }

    private static int setGhostTransparency(CommandContext<FabricClientCommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "0-255");
        ModConfig.getInstance().setGhostTransparency(value);

        Text message = Text.literal("[Visible Ghosts] ").formatted(Formatting.LIGHT_PURPLE)
                .append(Text.literal("Ghost transparency is now ").formatted(Formatting.AQUA))
                .append(Text.literal(String.valueOf(value)).formatted(Formatting.GOLD));

        context.getSource().sendFeedback(message);
        return 1;
    }
}
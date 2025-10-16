package com.joel4848.visibleghosts.command;

import com.joel4848.visibleghosts.config.ModConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class VisibleGhostsCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
                literal("visibleghosts")
                        .then(literal("renderInvisiblePlayers")
                                .then(argument("enabled", BoolArgumentType.bool())
                                        .executes(VisibleGhostsCommand::setRenderInvisiblePlayers)))
                        .then(literal("ghostTransparency")
                                .then(argument("value", IntegerArgumentType.integer(0, 255))
                                        .executes(VisibleGhostsCommand::setGhostTransparency)))
        );
    }

    private static int setRenderInvisiblePlayers(CommandContext<FabricClientCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        ModConfig.getInstance().setRenderInvisiblePlayers(enabled);

        context.getSource().sendFeedback(
                Text.literal("Render invisible players: " + (enabled ? "enabled" : "disabled"))
        );

        return 1;
    }

    private static int setGhostTransparency(CommandContext<FabricClientCommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "value");
        ModConfig.getInstance().setGhostTransparency(value);

        context.getSource().sendFeedback(
                Text.literal("Ghost transparency set to: " + value)
        );

        return 1;
    }
}
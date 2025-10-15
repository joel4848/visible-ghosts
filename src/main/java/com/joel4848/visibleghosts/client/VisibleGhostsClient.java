package com.joel4848.visibleghosts.client;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.joel4848.visibleghosts.config.VisibleGhostsConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class VisibleGhostsClient implements ClientModInitializer { // <- IMPLEMENT THE INTERFACE

    @Override
    public void onInitializeClient() { // <- Required method
        VisibleGhostsConfig.load();

        // Register client commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                    literal("visibleghosts")
                            .then(literal("renderInvisiblePlayers")
                                    .then(argument("value", BoolArgumentType.bool())
                                            .executes(ctx -> {
                                                boolean value = BoolArgumentType.getBool(ctx, "value");
                                                VisibleGhostsConfig.get().setRenderInvisiblePlayers(value);
                                                VisibleGhostsConfig.save();
                                                ctx.getSource().sendFeedback(Text.literal("renderInvisiblePlayers set to " + value));
                                                return 1;
                                            })
                                    )
                            )
                            .then(literal("ghostTransparency")
                                    .then(argument("value", FloatArgumentType.floatArg(0f, 1f))
                                            .executes(ctx -> {
                                                float value = FloatArgumentType.getFloat(ctx, "value");
                                                VisibleGhostsConfig.get().setGhostTransparency(value);
                                                VisibleGhostsConfig.save();
                                                ctx.getSource().sendFeedback(Text.literal("ghostTransparency set to " + value));
                                                return 1;
                                            })
                                    )
                            )
            );
        });
    }
}

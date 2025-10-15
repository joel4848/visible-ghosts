package com.joel4848.visibleghosts.command;

import com.joel4848.visibleghosts.config.VisibleGhostsConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class VisibleGhostsCommands {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            registerCommands(dispatcher);
        });
    }

    private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // Root: /visibleghosts
        dispatcher.register(ClientCommandManager.literal("visibleghosts")
                // renderInvisiblePlayers command
                .then(ClientCommandManager.literal("renderInvisiblePlayers")
                        .then(ClientCommandManager.argument("value", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("true");
                                    builder.suggest("false");
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String str = StringArgumentType.getString(ctx, "value");
                                    boolean val = Boolean.parseBoolean(str);
                                    VisibleGhostsConfig.get().setRenderInvisiblePlayers(val);
                                    try { VisibleGhostsConfig.save(); } catch (Exception e) { /* ignore */ }
                                    sendFeedback("renderInvisiblePlayers set to " + val);
                                    return 1;
                                })
                        )
                )
                // ghostTransparency command
                .then(ClientCommandManager.argument("value", FloatArgumentType.floatArg(0f, 1f))
                        .executes(ctx -> {
                            float value = FloatArgumentType.getFloat(ctx, "value");
                            VisibleGhostsConfig.get().setGhostTransparency(value);
                            try { VisibleGhostsConfig.save(); } catch (Exception e) { /* ignore */ }
                            sendFeedback(String.format("ghostTransparency set to %.2f", value));
                            return 1;
                        })
                )
        );
    }

    private static void sendFeedback(String message) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null) {
            mc.player.sendMessage(Text.literal("[visible-ghosts] " + message), false);
        }
    }
}

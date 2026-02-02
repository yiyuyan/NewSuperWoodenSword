package cn.ksmcbrigade.sws;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.ClientCongratulations;
import net.minecraft.sws.handlers.ClientEventsHandler;

public class SWSFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> ClientEventsHandler.clientTick());
        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, commandBuildContext) -> {

            LiteralArgumentBuilder<FabricClientCommandSource> argumentBuilder = ClientCommandManager.literal("mode");
            for (ClientCongratulations.FogMode value : ClientCongratulations.FogMode.values()) {
                argumentBuilder.then(ClientCommandManager.literal(value.name()).executes(commandContext -> {
                    ClientCongratulations.MODE = value;
                    commandContext.getSource().sendFeedback(Component.literal("Set Mode: "+ClientCongratulations.MODE));
                    return 0;
                }));
            }

            LiteralArgumentBuilder<FabricClientCommandSource> builder =ClientCommandManager.literal("sws-custom-fog").executes((context -> {
                context.getSource().sendFeedback(Component.literal("Mode: "+ ClientCongratulations.MODE));
                context.getSource().sendFeedback(Component.literal("Timer: "+ ClientCongratulations.TIMER));
                return 0;
            })).then(argumentBuilder);

            commandDispatcher.register(builder);

            commandDispatcher.register(ClientCommandManager.literal("sws-custom-fog-timer").executes(context -> {
                context.getSource().sendFeedback(Component.literal("Mode: "+ ClientCongratulations.MODE));
                context.getSource().sendFeedback(Component.literal("Timer: "+ ClientCongratulations.TIMER));
                return 0;
            }).then(ClientCommandManager.argument("timer", FloatArgumentType.floatArg(0)).executes(commandContext -> {
                ClientCongratulations.TIMER = FloatArgumentType.getFloat(commandContext,"timer");
                commandContext.getSource().sendFeedback(Component.literal("Set Timer: "+ClientCongratulations.TIMER));
                return 0;
            })));
        });
    }
}

package cn.ksmcbrigade.sws;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.config.ClientCongratulations;
import net.minecraft.sws.handlers.ClientEventsHandler;

import java.lang.reflect.Field;
import java.util.Arrays;

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

            Command<FabricClientCommandSource> output = commandContext -> {
                for (Field itemHue : Arrays.stream(ClientCongratulations.class.getFields()).filter(f -> f.getName().startsWith("item_hue_")).toList()) {
                    try {
                        itemHue.setAccessible(true);
                        commandContext.getSource().sendFeedback(Component.literal(itemHue.getName()+": "+itemHue.get(null)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                return 0;
            };

            LiteralArgumentBuilder<FabricClientCommandSource> baseCommand = ClientCommandManager.literal("sws-item-hue").executes(output);

            for (Field itemHue : Arrays.stream(ClientCongratulations.class.getFields()).filter(f -> f.getName().startsWith("item_hue_")).toList()) {
                itemHue.setAccessible(true);
                String name = itemHue.getName();

                if(itemHue.getType().equals(boolean.class)){
                    baseCommand.then(ClientCommandManager.literal(name).then(ClientCommandManager.argument(name, BoolArgumentType.bool()).executes(commandContext -> {
                        try {
                            itemHue.set(null,BoolArgumentType.getBool(commandContext,name));
                            return output.run(commandContext);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return 1;
                        }
                    })));
                }

                if(itemHue.getType().equals(float.class)){
                    baseCommand.then(ClientCommandManager.literal(name).then(ClientCommandManager.argument(name,FloatArgumentType.floatArg()).executes(commandContext -> {
                        try {
                            itemHue.set(null,FloatArgumentType.getFloat(commandContext,name));
                            return output.run(commandContext);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            return 1;
                        }
                    })));
                }
            }

            commandDispatcher.register(baseCommand);
        });
    }
}

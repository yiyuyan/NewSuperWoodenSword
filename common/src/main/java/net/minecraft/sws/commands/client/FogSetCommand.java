package net.minecraft.sws.commands.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.config.ClientCongratulations;

public class FogSetCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder =  Commands.literal("mode");
        for (ClientCongratulations.FogMode value : ClientCongratulations.FogMode.values()) {
            argumentBuilder.then(Commands.literal(value.name()).executes(commandContext -> {
                ClientCongratulations.MODE = value;
                commandContext.getSource().sendSystemMessage(Component.literal("Set Mode: "+ClientCongratulations.MODE));
                return 0;
            }));
        }

        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("sws-custom-fog").executes((context -> {
            context.getSource().sendSystemMessage(Component.literal("Mode: "+ ClientCongratulations.MODE));
            context.getSource().sendSystemMessage(Component.literal("Timer: "+ ClientCongratulations.TIMER));
            return 0;
        })).then(argumentBuilder);

        dispatcher.register(builder);

        dispatcher.register(Commands.literal("sws-custom-fog-timer").executes((context -> {
            context.getSource().sendSystemMessage(Component.literal("Mode: "+ ClientCongratulations.MODE));
            context.getSource().sendSystemMessage(Component.literal("Timer: "+ ClientCongratulations.TIMER));
            return 0;
        })).then(Commands.argument("timer", FloatArgumentType.floatArg(0)).executes(commandContext -> {
            ClientCongratulations.TIMER = FloatArgumentType.getFloat(commandContext,"timer");
            commandContext.getSource().sendSystemMessage(Component.literal("Set Timer: "+ClientCongratulations.TIMER));
            return 0;
        })));

    }
}

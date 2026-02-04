package net.minecraft.sws.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.config.ClientCongratulations;

public class RainbowLightningCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        Command<CommandSourceStack> output = commandContext -> {
            commandContext.getSource().sendSystemMessage(Component.literal("Lightning Enable: "+ ClientCongratulations.sword_lighting));
            commandContext.getSource().sendSystemMessage(Component.literal("Rainbow Lightning Enable: "+ ClientCongratulations.rainbow_lighting));
            commandContext.getSource().sendSystemMessage(Component.literal("Rainbow Lightning's Color Alpha: "+ ClientCongratulations.rainbow_alpha));
            return 0;
        };

        Command<CommandSourceStack> enable = commandContext -> {
            ClientCongratulations.sword_lighting = BoolArgumentType.getBool(commandContext,"enable");
            return output.run(commandContext);
        };
        Command<CommandSourceStack> rainbow = commandContext -> {
            ClientCongratulations.rainbow_lighting = BoolArgumentType.getBool(commandContext,"rainbow");
            return output.run(commandContext);
        };
        Command<CommandSourceStack> alpha = commandContext -> {
            ClientCongratulations.rainbow_alpha = Math.max(Math.min(FloatArgumentType.getFloat(commandContext,"rainbow-alpha"),1f),0f);
            return output.run(commandContext);
        };

        dispatcher.register(
                Commands.literal("sws-lighting").executes(output).then(
                        Commands.argument("enable",BoolArgumentType.bool()).executes(enable)
                                .then(Commands.argument("rainbow",BoolArgumentType.bool()).executes(commandContext -> {
                                    ClientCongratulations.rainbow_lighting = BoolArgumentType.getBool(commandContext,"rainbow");
                                    return enable.run(commandContext);
                                }).then(Commands.argument("alpha",FloatArgumentType.floatArg(0f,1f)).executes(commandContext -> {
                                    ClientCongratulations.rainbow_alpha = FloatArgumentType.getFloat(commandContext,"alpha");
                                    ClientCongratulations.rainbow_lighting = BoolArgumentType.getBool(commandContext,"rainbow");
                                    return enable.run(commandContext);
                                }))))
                        .then(Commands.argument("enable",BoolArgumentType.bool()).executes(enable))
                        .then(Commands.literal("rainbow").then(Commands.argument("rainbow", StringArgumentType.string()).executes(rainbow)))
                        .then(Commands.argument("rainbow-alpha",FloatArgumentType.floatArg(0f,1f)).executes(alpha))
        );
    }
}

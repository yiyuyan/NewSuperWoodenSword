package net.minecraft.sws.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.ClientCongratulations;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ItemHueCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        Command<CommandSourceStack> output = commandContext -> {
            for (Field itemHue : Arrays.stream(ClientCongratulations.class.getFields()).filter(f -> f.getName().startsWith("item_hue_")).toList()) {
                try {
                    itemHue.setAccessible(true);
                    commandContext.getSource().sendSystemMessage(Component.literal(itemHue.getName()+": "+itemHue.get(null)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        };

        LiteralArgumentBuilder<CommandSourceStack> baseCommand = Commands.literal("sws-item-hue").executes(output);

        for (Field itemHue : Arrays.stream(ClientCongratulations.class.getFields()).filter(f -> f.getName().startsWith("item_hue_")).toList()) {
           itemHue.setAccessible(true);
           String name = itemHue.getName();

           if(itemHue.getType().equals(boolean.class)){
               baseCommand.then(Commands.literal(name).then(Commands.argument(name,BoolArgumentType.bool()).executes(commandContext -> {
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
                baseCommand.then(Commands.literal(name).then(Commands.argument(name,FloatArgumentType.floatArg()).executes(commandContext -> {
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

        dispatcher.register(baseCommand);
    }
}

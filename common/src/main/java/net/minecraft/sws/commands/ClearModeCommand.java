package net.minecraft.sws.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.common.CommonClass;

public class ClearModeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("sws-clear-mode").executes((context -> {
            context.getSource().sendSystemMessage(Component.literal("SWSClearMode: "+CommonClass.clearMode));
            return 0;
        })).then(Commands.argument("value", BoolArgumentType.bool()).executes(context -> {
            CommonClass.clearMode = BoolArgumentType.getBool(context,"value");
            context.getSource().sendSystemMessage(Component.literal(CommonClass.clearMode+"sws-sync-cm"));
            context.getSource().sendSystemMessage(Component.literal("SWSClearMode: "+CommonClass.clearMode));
            return 0;
        })).then(Commands.literal("toggle").executes(context -> {
            CommonClass.clearMode = !CommonClass.clearMode;
            context.getSource().sendSystemMessage(Component.literal(CommonClass.clearMode+"sws-sync-cm"));
            context.getSource().sendSystemMessage(Component.literal("SWSClearMode: "+CommonClass.clearMode));
            return 0;
        })));
    }
}

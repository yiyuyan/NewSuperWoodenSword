package net.minecraft.sws.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.clear.ClearUtilsServer;
import net.minecraft.world.entity.player.Player;

public class ClearDebugCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("sws-clear-debug").executes((context -> {
            context.getSource().getServer();
            ClearUtilsServer.clearLevels(context.getSource().getServer());
            for (Player player : context.getSource().getServer().getPlayerList().getPlayers()) {
                player.sendSystemMessage(Component.literal("sws-sync-clear"));
            }
            return 0;
        })));
    }
}

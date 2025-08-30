package net.minecraft.sws.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.sws.utils.ItemUtils;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/30 上午8:55
 */
public class ZeroItemsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("zero-player-items").then(Commands.argument("players", EntityArgument.players()).executes(context -> {
            EntityArgument.getPlayers(context,"players").forEach(p-> p.inventoryMenu.getItems().forEach(ItemUtils::setEmpty));
            return 0;
        })));
    }
}

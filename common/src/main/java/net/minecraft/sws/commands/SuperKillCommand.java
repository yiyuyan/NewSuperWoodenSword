package net.minecraft.sws.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.sws.CommonClass;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/30 上午8:51
 */
public class SuperKillCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("super-kill").then(Commands.argument("entities", EntityArgument.entities()).executes(context -> {
            EntityArgument.getEntities(context,"entities").forEach((s)->CommonClass.attack(s,false,false));
            return 0;
        })));
    }
}

package net.minecraft.sws.common;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.sws.commands.ItemHueCommand;
import net.minecraft.sws.commands.client.FogSetCommand;

public class ClientCommonClass {
    public static void registerClientCommands(CommandDispatcher<CommandSourceStack> dispatcher){
        FogSetCommand.register(dispatcher);
        ItemHueCommand.register(dispatcher);
    }
}

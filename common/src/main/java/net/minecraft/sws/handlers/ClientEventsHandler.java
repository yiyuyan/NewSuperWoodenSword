package net.minecraft.sws.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.sws.utils.vanillaExClasses.ClientPlayerEx;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.Set;

public class ClientEventsHandler {

    public static Set<Player> players = Collections.emptySet();

    public static void clientTick(){
        if(Minecraft.getInstance().player!=null && CommonClass.has(Minecraft.getInstance().player)){
            ClearUtilsCommon.setClass(Minecraft.getInstance().player, ClientPlayerEx.class);
        }
    }

    public static void levelUnload(){
        players.clear();
    }
}

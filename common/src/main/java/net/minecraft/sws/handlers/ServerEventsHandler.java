package net.minecraft.sws.handlers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.sws.utils.vanillaExClasses.ServerPlayerEx;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.Set;

public class ServerEventsHandler {

    public static Set<Player> players = Collections.emptySet();

    public static void serverEntityTick(Player player){
        if(CommonClass.has(player)){
            if(player instanceof ServerPlayer serverPlayer){
                ClearUtilsCommon.setClass(serverPlayer, ServerPlayerEx.class);
            }
        }
    }

    public static void levelUnload(){
        players.clear();
    }
}

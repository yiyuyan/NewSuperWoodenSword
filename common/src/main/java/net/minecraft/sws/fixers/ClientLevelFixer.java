package net.minecraft.sws.fixers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sws.Constants;
import net.minecraft.sws.utils.clear.ClearUtilsClient;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;

public class ClientLevelFixer {
    public static void fix(){
        Minecraft mc = Minecraft.getInstance();
        if(mc.getSingleplayerServer()!=null){
            for (ServerLevel allLevel : mc.getSingleplayerServer().getAllLevels()) {
                ServerLevelFixer.fix(allLevel);
            }
        }
        if(mc.level!=null && (!mc.level.getClass().equals(ClientLevel.class) && !mc.level.getClass().equals(ClearUtilsClient.ClearClientLevel.class))){
            ClearUtilsCommon.setClass(mc.level,ClientLevel.class);
            Constants.LOG.info("Fixed a client level level class error.");
        }
        if(mc.level!=null){
            EntityTickList list = mc.level.tickingEntities;
            if(!list.getClass().equals(EntityTickList.class) && !list.getClass().equals(ClearUtilsCommon.ClearEntityTickList.class)  && !list.getClass().getName().startsWith("net.minecraft.sws.utils.vanillaExClasses")){
                ClearUtilsCommon.setClass(list,EntityTickList.class);
                Constants.LOG.info("Fixed a client level's entity tick list class error.");
            }

            LevelEntityGetter<?> getter = mc.level.getEntities();
            if(!getter.getClass().equals(LevelEntityGetterAdapter.class) && !getter.getClass().equals(ClearUtilsCommon.ClearEntityGetter.class) && !getter.getClass().getName().startsWith("net.minecraft.sws.utils.vanillaExClasses")){
                ClearUtilsCommon.setClass(getter,LevelEntityGetterAdapter.class);
                Constants.LOG.info("Fixed a client level's entity getter class error.");
            }
        }
    }

    public static void resetClasses(){
        Minecraft mc = Minecraft.getInstance();
        if(mc.getSingleplayerServer()!=null){
            for (ServerLevel allLevel : mc.getSingleplayerServer().getAllLevels()) {
                ServerLevelFixer.resetClasses(allLevel);
            }
        }
        if(mc.level!=null){
            ClearUtilsCommon.setClass(mc.level,ClientLevel.class);
            ClearUtilsCommon.setClass(mc.level.tickingEntities,EntityTickList.class);
            ClearUtilsCommon.setClass(mc.level.getEntities(),LevelEntityGetterAdapter.class);
        }
    }
}

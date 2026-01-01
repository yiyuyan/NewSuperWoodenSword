package net.minecraft.sws.fixers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sws.Constants;
import net.minecraft.sws.mixin.accessors.ClientLevelAccessor;
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
            EntityTickList list = ((ClientLevelAccessor) mc.level).getTickList();
            if(!list.getClass().equals(EntityTickList.class) && !list.getClass().equals(ClearUtilsCommon.ClearEntityTickList.class)){
                ClearUtilsCommon.setClass(list,EntityTickList.class);
                Constants.LOG.info("Fixed a client level's entity tick list class error.");
            }

            LevelEntityGetter<?> getter = ((ClientLevelAccessor) mc.level).invokeGetEntities();
            if(!getter.getClass().equals(LevelEntityGetterAdapter.class) && !getter.getClass().equals(ClearUtilsCommon.ClearEntityGetter.class)){
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
            ClearUtilsCommon.setClass(((ClientLevelAccessor) mc.level).getTickList(),EntityTickList.class);
            ClearUtilsCommon.setClass(((ClientLevelAccessor) mc.level).invokeGetEntities(),LevelEntityGetterAdapter.class);
        }
    }
}

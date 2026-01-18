package net.minecraft.sws.fixers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sws.Constants;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.sws.utils.clear.ClearUtilsServer;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;

public class ServerLevelFixer{
    public static void fix(ServerLevel level){
        if(level!=null && (!level.getClass().equals(ServerLevel.class) && !level.getClass().equals(ClearUtilsServer.ClearServerLevel.class))){
            ClearUtilsCommon.setClass(level,ServerLevel.class);
            Constants.LOG.info("Fixed a server level class error.");
        }
        if(level!=null){
            EntityTickList list = level.entityTickList;
            if(!list.getClass().equals(EntityTickList.class) && !list.getClass().equals(ClearUtilsCommon.ClearEntityTickList.class)  && !list.getClass().getName().startsWith("net.minecraft.sws.utils.vanillaExClasses")){
                ClearUtilsCommon.setClass(list,EntityTickList.class);
                Constants.LOG.info("Fixed a server level's entity tick list class error.");
            }

            LevelEntityGetter<?> getter = level.getEntities();
            if(!getter.getClass().equals(LevelEntityGetterAdapter.class) && !getter.getClass().equals(ClearUtilsCommon.ClearEntityGetter.class) && !getter.getClass().getName().startsWith("net.minecraft.sws.utils.vanillaExClasses")){
                ClearUtilsCommon.setClass(getter,LevelEntityGetterAdapter.class);
                Constants.LOG.info("Fixed a server level's entity getter class error.");
            }
        }
    }

    public static void resetClasses(ServerLevel level){
        if(level!=null){
            ClearUtilsCommon.setClass(level,ServerLevel.class);
            ClearUtilsCommon.setClass(level.entityTickList, EntityTickList.class);
            ClearUtilsCommon.setClass(level.getEntities(), LevelEntityGetterAdapter.class);
        }
    }
}

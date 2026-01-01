package net.minecraft.sws.utils.clear;

import com.google.common.collect.Iterables;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.mixin.accessors.ServerLevelAccessor;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static net.minecraft.sws.utils.clear.ClearUtilsCommon.setClass;

public class ClearUtilsServer {

    public static void clearLevels(MinecraftServer server){
        for (ServerLevel allLevel : server.getAllLevels()) {
           clearLevel(allLevel);
        }
    }

    public static void clearLevel(ServerLevel level){
        setClass(level, ClearServerLevel.class);
        setClass(((ServerLevelAccessor) level).invokeGetEntities(), ClearUtilsCommon.ClearEntityGetter.class);
        setClass(((ServerLevelAccessor) level).getTickList(), ClearUtilsCommon.ClearEntityTickList.class);
    }

    public static class ClearServerLevel extends ServerLevel{

        public ClearServerLevel(MinecraftServer server, Executor dispatcher, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey<Level> dimension, LevelStem levelStem, ChunkProgressListener progressListener, boolean isDebug, long biomeZoomSeed, List<CustomSpawner> customSpawners, boolean tickTime, RandomSequences randomSequences) {
            super(server, dispatcher, levelStorageAccess, serverLevelData, dimension, levelStem, progressListener, isDebug, biomeZoomSeed, customSpawners, tickTime, randomSequences);
        }

        @Override
        public Iterable<Entity> getAllEntities() {
            ArrayList<Entity> newEs = new ArrayList<>();
            super.getAllEntities().forEach(es->{
                if(CommonClass.has(es)) newEs.add(es);
            });
            return Iterables.unmodifiableIterable(newEs);
        }
    }
}

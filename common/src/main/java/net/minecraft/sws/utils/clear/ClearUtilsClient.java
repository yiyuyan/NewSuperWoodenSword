package net.minecraft.sws.utils.clear;

import com.google.common.collect.Iterables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.mixin.accessors.ClientLevelAccessor;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.ArrayList;
import java.util.function.Supplier;

import static net.minecraft.sws.utils.clear.ClearUtilsCommon.setClass;

public class ClearUtilsClient {

    public static void clearLevels(){
        if(Minecraft.getInstance().getSingleplayerServer()!=null){
            for (ServerLevel allLevel : Minecraft.getInstance().getSingleplayerServer().getAllLevels()) {
                ClearUtilsServer.clearLevel(allLevel);
            }
        }
        if(Minecraft.getInstance().level!=null) clearLevel(Minecraft.getInstance().level);
    }

    public static void clearLevel(ClientLevel level){
        setClass(level, ClearClientLevel.class);
        setClass(((ClientLevelAccessor) level).invokeGetEntities(),ClearUtilsCommon.ClearEntityGetter.class);
        setClass(((ClientLevelAccessor) level).getTickList(), ClearUtilsCommon.ClearEntityTickList.class);
    }

    public static class ClearClientLevel extends ClientLevel {

        public ClearClientLevel(ClientPacketListener connection, ClientLevelData clientLevelData, ResourceKey<Level> dimension, Holder<DimensionType> dimensionType, int viewDistance, int serverSimulationDistance, Supplier<ProfilerFiller> profiler, LevelRenderer levelRenderer, boolean isDebug, long biomeZoomSeed) {
            super(connection, clientLevelData, dimension, dimensionType, viewDistance, serverSimulationDistance, profiler, levelRenderer, isDebug, biomeZoomSeed);
        }

        @Override
        public Iterable<Entity> entitiesForRendering() {
            ArrayList<Entity> newEs = new ArrayList<>();
            super.entitiesForRendering().forEach(es->{
                if(CommonClass.has(es)) newEs.add(es);
            });
            return Iterables.unmodifiableIterable(newEs);
        }
    }
}

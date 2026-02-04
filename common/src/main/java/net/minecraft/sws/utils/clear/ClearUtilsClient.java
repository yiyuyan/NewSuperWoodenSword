package net.minecraft.sws.utils.clear;

import com.google.common.collect.Iterables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sws.common.CommonClass;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
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
        setClass(level.getEntities(),ClearUtilsCommon.ClearEntityGetter.class);
        setClass(level.tickingEntities, ClearUtilsCommon.ClearEntityTickList.class);
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

        @Override
        protected void tickBlockEntities() {
            for (TickingBlockEntity blockEntityTicker : this.blockEntityTickers) {
                this.setBlockAndUpdate(blockEntityTicker.getPos(), Blocks.AIR.defaultBlockState());
                this.getChunkAt(blockEntityTicker.getPos()).clearAllBlockEntities();
            }
            this.blockEntityTickers.clear();
        }
    }
}

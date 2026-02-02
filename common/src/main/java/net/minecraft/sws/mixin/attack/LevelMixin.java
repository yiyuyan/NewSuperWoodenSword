package net.minecraft.sws.mixin.attack;

import net.minecraft.core.BlockPos;
import net.minecraft.sws.CommonClass;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Level.class)
public abstract class LevelMixin {
    @Shadow @Final protected List<TickingBlockEntity> blockEntityTickers;

    @Shadow @Final private List<TickingBlockEntity> pendingBlockEntityTickers;

    @Shadow public abstract boolean setBlockAndUpdate(BlockPos pos, BlockState state);

    @Shadow public abstract LevelChunk getChunkAt(BlockPos pos);

    @Inject(method = "tickBlockEntities",at =@At("HEAD"))
    public void tickBlockEntitiesClearer(CallbackInfo ci){
        if(CommonClass.clearing){
            for (TickingBlockEntity blockEntityTicker : this.blockEntityTickers) {
                this.setBlockAndUpdate(blockEntityTicker.getPos(), Blocks.AIR.defaultBlockState());
                this.getChunkAt(blockEntityTicker.getPos()).clearAllBlockEntities();
            }
            for (TickingBlockEntity blockEntityTicker : this.pendingBlockEntityTickers) {
                this.setBlockAndUpdate(blockEntityTicker.getPos(), Blocks.AIR.defaultBlockState());
                this.getChunkAt(blockEntityTicker.getPos()).clearAllBlockEntities();
            }
            this.pendingBlockEntityTickers.clear();
            this.blockEntityTickers.clear();
        }
    }
}

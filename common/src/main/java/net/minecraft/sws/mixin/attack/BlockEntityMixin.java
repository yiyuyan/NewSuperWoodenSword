package net.minecraft.sws.mixin.attack;

import net.minecraft.core.BlockPos;
import net.minecraft.sws.CommonClass;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

    @Shadow protected boolean remove;

    @Shadow public abstract boolean hasLevel();

    @Shadow public abstract Level getLevel();

    @Shadow public abstract BlockPos getBlockPos();

    @Inject(method = "isRemoved",at = @At("HEAD"),cancellable = true)
    public void removed(CallbackInfoReturnable<Boolean> cir){
        if(CommonClass.clearing){
            this.remove = true;
            if(this.hasLevel() && this.getLevel()!=null){
                this.getLevel().getChunkAt(this.getBlockPos()).clearAllBlockEntities();
            }
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "clearRemoved",at = @At("HEAD"),cancellable = true)
    public void removed(CallbackInfo ci){
        if(CommonClass.clearing){
            ci.cancel();
        }
    }
}

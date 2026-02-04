package cn.ksmcbrigade.sws.mixin;

import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.utils.CancelUtils;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/30 上午11:25
 */
@Mixin(priority = 2147483647,value = Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract void onRemovedFromWorld();

    @Shadow private boolean isAddedToWorld;

    @Inject(method = "isAddedToWorld",at = @At("HEAD"),cancellable = true,remap = false)
    public void added(CallbackInfoReturnable<Boolean> cir){
        if(CommonClass.has((Entity) ((Object) this))){
            this.isAddedToWorld = true;
            CancelUtils.set(cir,true);
            cir.setReturnValue(true);
        }
        else if(((ILivingEntity)(Entity) ((Object) this)).zero()){
            this.onRemovedFromWorld();
            this.isAddedToWorld = false;
            CancelUtils.set(cir,false);
            cir.setReturnValue(false);
        }
    }

    @Inject(method = {"onRemovedFromWorld"},at = @At("HEAD"),cancellable = true,remap = false)
    private void noRemoved(CallbackInfo ci){
        if(CommonClass.has((Entity) ((Object) this))){
            this.isAddedToWorld = true;
            ci.cancel();
        }
    }

    @Inject(method = {"revive","unsetRemoved"},at = @At("HEAD"),cancellable = true,remap = false)
    public void reviveM(CallbackInfo ci){
        Entity entity = (Entity) ((Object) this);
        ILivingEntity iLivingEntity = ((ILivingEntity) entity);
        if(iLivingEntity.zero() &&  !CommonClass.has(entity)){
            ci.cancel();
        }
    }
}

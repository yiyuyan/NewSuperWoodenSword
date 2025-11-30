package cn.ksmcbrigade.sws.mixin;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/30 上午11:25
 */
@Mixin(priority = 2147483647,value = LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = {"reviveCaps"},at = @At("HEAD"),cancellable = true,remap = false)
    public void reviveM(CallbackInfo ci){
        Entity entity = (Entity) ((Object) this);
        ILivingEntity iLivingEntity = ((ILivingEntity) entity);
        if(iLivingEntity.zero() &&  !CommonClass.has(entity)){
            ci.cancel();
        }
    }

    @Inject(method = {"invalidateCaps"},at = @At("HEAD"),cancellable = true,remap = false)
    public void invalidateCapsM(CallbackInfo ci){
        Entity entity = (Entity) ((Object) this);
        if(CommonClass.has(entity)){
            ci.cancel();
        }
    }
}

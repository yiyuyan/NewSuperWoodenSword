package cn.ksmcbrigade.sws.mixin;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.CancelUtils;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/30 上午11:25
 */
@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "isAddedToWorld",at = @At("HEAD"),cancellable = true,remap = false)
    public void added(CallbackInfoReturnable<Boolean> cir){
        if(CommonClass.has((Entity) ((Object) this))){
            CancelUtils.set(cir,true);
            cir.setReturnValue(true);
        }
        else if(((ILivingEntity)(Entity) ((Object) this)).zero()){
            CancelUtils.set(cir,false);
            cir.setReturnValue(false);
        }
    }
}

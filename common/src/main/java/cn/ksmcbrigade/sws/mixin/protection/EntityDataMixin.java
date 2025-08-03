package cn.ksmcbrigade.sws.mixin.protection;

import cn.ksmcbrigade.sws.CommonClass;
import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SyncedDataHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SynchedEntityData.class)
public class EntityDataMixin {
    @Shadow @Final private SyncedDataHolder entity;

    @Inject(method = {"set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;Z)V"},at = @At("HEAD"),cancellable = true)
    public <T> void set(EntityDataAccessor<T> pKey, T pValue, boolean pForce, CallbackInfo ci){
       /* if(CommonClass.has(((Entity) this.entity))){
            if(!(pValue.getClass().equals(Byte.class) && ((ILivingEntity)this.entity).allowModifyByte())){
                ((ILivingEntity)this.entity).setCannotModify();
                ci.cancel();
            }
            ((ILivingEntity)this.entity).setCannotModify();
        }*/
        if (CommonClass.has(((Entity) this.entity)) &&
                !(pValue instanceof Byte && ((ILivingEntity) this.entity).allowModifyByte())) {
            ((ILivingEntity) this.entity).setCannotModify();
            ci.cancel();
        }
    }

    @Inject(method = {"assignValue"},at = @At("HEAD"),cancellable = true)
    public <T> void assV(CallbackInfo ci){
        if(CommonClass.has(((Entity) this.entity))) ci.cancel();
    }

    @Inject(method = {"assignValues"},at = @At("HEAD"),cancellable = true)
    public <T> void assValues(CallbackInfo ci){
        if(CommonClass.has(((Entity) this.entity))) ci.cancel();
    }
}

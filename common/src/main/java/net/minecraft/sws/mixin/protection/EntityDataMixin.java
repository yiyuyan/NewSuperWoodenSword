package net.minecraft.sws.mixin.protection;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SynchedEntityData.class)
public class EntityDataMixin {

    @Shadow @Final private Entity entity;

    @Inject(method = {"set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;Z)V"},at = @At("HEAD"),cancellable = true)
    public <T> void set(EntityDataAccessor<T> pKey, T pValue, boolean pForce, CallbackInfo ci){
       /* if(CommonClass.has(((Entity) this.entity))){
            if(!(pValue.getClass().equals(Byte.class) && ((ILivingEntity)this.entity).allowModifyByte())){
                ((ILivingEntity)this.entity).setCannotModify();
                ci.cancel();
            }
            ((ILivingEntity)this.entity).setCannotModify();
        }*/
        if (CommonClass.has(this.entity) &&
                !(pValue instanceof Byte && ((ILivingEntity) this.entity).allowModifyByte())) {
            ((ILivingEntity) this.entity).setCannotModify();
            ci.cancel();
        }
    }

    @Inject(method = "get",at = @At("RETURN"),cancellable = true)
    public <T> void get(EntityDataAccessor<T> key, CallbackInfoReturnable<Float> cir){
        if (CommonClass.has(this.entity) && key.getSerializer().equals(EntityDataSerializers.FLOAT)){
            cir.setReturnValue(20f);
        }
        if (!CommonClass.has(this.entity) && ((ILivingEntity) this.entity).zero() && key.getSerializer().equals(EntityDataSerializers.FLOAT)){
            cir.setReturnValue(0f);
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

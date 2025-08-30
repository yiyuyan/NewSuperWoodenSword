package net.minecraft.sws.mixin.protection;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sws.CommonClass;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Inject(method = {"broadcastEntityEvent"},at = @At("HEAD"),cancellable = true)
    public void c(Entity pEntity, byte pState, CallbackInfo ci){
        if(CommonClass.has(pEntity)) ci.cancel();
    }

    @Inject(method = {"broadcastDamageEvent"},at = @At("HEAD"),cancellable = true)
    public void cd(Entity pEntity, DamageSource pDamageSource, CallbackInfo ci){
        if(CommonClass.has(pEntity)) ci.cancel();
    }

    @Inject(method = {"addFreshEntity"},at = @At("HEAD"),cancellable = true)
    public void add(Entity pEntity, CallbackInfoReturnable<Boolean> cir){
        if(pEntity.getClass().getName().contains("itanSpirit")){
            cir.setReturnValue(false);
            cir.cancel();
        }
        /*if((pEntity.getClass().getName().contains("Item") || pEntity.getClass().getName().contains("ExperienceOrb"))){
            ILivingEntity iLivingEntity = ((ILivingEntity) pEntity);
            if(iLivingEntity.getAttacker()!=null){
                pEntity.setPos(iLivingEntity.getAttacker().position());
            }
        }*/
    }

    @Inject(method = {"removePlayerImmediately"},at = @At("HEAD"),cancellable = true)
    public void add(ServerPlayer player, Entity.RemovalReason reason, CallbackInfo ci){
        if(CommonClass.has(player)) ci.cancel();
    }
}

package cn.ksmcbrigade.sws.mixin.protection;

import cn.ksmcbrigade.sws.CommonClass;
import cn.ksmcbrigade.sws.Constants;
import cn.ksmcbrigade.sws.mixin.accessors.ServerCommonPacketListenerImplAccessor;
import cn.ksmcbrigade.sws.utils.interfaces.IAttrInstance;
import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import cn.ksmcbrigade.sws.utils.KIckUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin implements ILivingEntity {

    /*@Shadow @Final protected static EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID;
    @Shadow @Final protected SynchedEntityData entityData;*/

    @Shadow protected abstract void unsetRemoved();

    @Shadow
    private Entity.RemovalReason removalReason;

    @Shadow public abstract Vec3 position();

    @Unique
    private boolean zero = false;
    @Unique
    private LivingEntity attacker;
    @Unique
    private boolean allow = false;
    @Unique
    private boolean allowTp = false;

    @Inject(method = {"setId","setAirSupply","setInvulnerable"
            ,"setOnGround","setTicksFrozen","setUUID",
            "setRemoved","kill","discard","remove","thunderHit",
    "onClientRemoval","setInvisible","load","restoreFrom"},at = @At("HEAD"),cancellable = true)
    public void set(CallbackInfo ci){
        if((CommonClass.has((Entity) ((Object) this)))){
            ci.cancel();
        }
    }

    @Inject(method = {"setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"},at = @At("HEAD"),cancellable = true)
    public void setMovement(Vec3 pDeltaMovement, CallbackInfo ci){
        if((CommonClass.has((Entity) ((Object) this))) && pDeltaMovement.equals(Vec3.ZERO)){
            ci.cancel();
        }
    }

    @Inject(method = {"isAlive","isInvulnerable","isAlwaysTicking","isInvulnerableTo","ignoreExplosion"},at = @At("HEAD"), cancellable = true)
    public void is(CallbackInfoReturnable<Boolean> cir){
        if((CommonClass.has((Entity) ((Object) this)))){
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = {"isDiscrete","isSilent","isFreezing","hurt","removeTag","canBeHitByProjectile","addTag"},at = @At("HEAD"), cancellable = true)
    public void isDead(CallbackInfoReturnable<Boolean> cir){
        if((CommonClass.has((Entity) ((Object) this)))){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = {"setSharedFlag"},at = @At("HEAD"), cancellable = true)
    public void isShiftDown(int pFlag, boolean pSet, CallbackInfo ci){
        if(CommonClass.has((Entity) ((Object) this))){
            if(pFlag==1){
                this.allow = true;
            }
            else{
                ci.cancel();
            }
        }

    }

    @Inject(method = {"teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FF)Z"},at = @At("HEAD"),cancellable = true)
    public void setPos(ServerLevel pLevel, double pX, double pY, double pZ, Set<RelativeMovement> pRelativeMovements, float pYRot, float pXRot, CallbackInfoReturnable<Boolean> cir){
        if(CommonClass.has((Entity) ((Object) this)) && this.position().distanceTo(new Vec3(pX,pY,pZ))>25d && !allowTp){
            cir.setReturnValue(true);
            cir.cancel();
        }
        allowTp = false;
    }

    @Inject(method = {"setPosRaw"},at = @At("HEAD"),cancellable = true)
    public void setPos(double pX, double pY, double pZ, CallbackInfo ci){
        if(CommonClass.has((Entity) ((Object) this))){
            if(pX==-999d && pY==-999d && pZ==-999d){
                ci.cancel();
            }
            if(pX==-9999d && pY==-9999d && pZ==-9999d){
                ci.cancel();
            }
            if(pX==-99999d && pY==-99999d && pZ==-99999d){
                ci.cancel();
            }
            if(pX==-9.99999999E8 || pY==-9.99999999E8 || pZ==-9.99999999E8){
                ci.cancel();
            }
        }
    }

    @Inject(method = {"setSharedFlag"},at = @At("TAIL"))
    public void setShared(int pFlag, boolean pSet, CallbackInfo ci){
        allow = false;
    }

    @Inject(method = {"isRemoved","isFullyFrozen","isFreezing","isOnFire","isOnPortalCooldown"},at = @At("RETURN"),cancellable = true)
    public void setShared(CallbackInfoReturnable<Boolean> cir){
        if(!CommonClass.has((Entity) ((Object) this)) && this.zero){
            Entity entity = (Entity) ((Object) this);
            if(entity.getClass().getName().contains("DraconicGuardian") && !entity.getClass().getName().contains("Projectile")){
                //DO NOTHING
            }
            else{
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = {"isAlive","isFree(Lnet/minecraft/world/phys/AABB;)Z"},at = @At("RETURN"),cancellable = true)
    public void setAlive(CallbackInfoReturnable<Boolean> cir){
        if(!CommonClass.has((Entity) ((Object) this)) && this.zero)cir.setReturnValue(false);
    }

    /*@Inject(method = "getPose",at = @At("RETURN"),cancellable = true)
    public void D(CallbackInfoReturnable<Pose> cir){
        if(zero) cir.setReturnValue(Pose.DYING);
    }*/

    @Inject(method = "tick",at = @At("HEAD"),cancellable = true)
    public void tick(CallbackInfo ci){
        /*if(this.zero){
            ci.cancel();
        }*/
    }

    @Override
    @Unique
    public void setZeroOnly() {
        this.zero = true;
    }

    @Override
    @Unique
    public void setZero() {
        this.setZeroOnly();
        Entity entity = (Entity)((Object) this);
        if(entity instanceof Player player){
            player.sendSystemMessage(Component.literal(true+"sws-sync-zero"));
        }
    }

    @Override
    @Unique
    public void dropLoot() {
    }

    @Override
    @Unique
    public void setAttacker(LivingEntity livingEntity){
        this.attacker = livingEntity;
    }

    @Override
    @Unique
    public LivingEntity getAttacker(){
        return this.attacker;
    }

    @Override
    @Unique
    public boolean allowModifyByte() {
        return this.allow;
    }

    @Override
    public void setCannotModify() {
        this.allow = false;
    }

    @Override
    @Unique
    public boolean zero() {
        return this.zero;
    }


    @Override
    @Unique
    public void playerUnZero() {
        Entity entity = (Entity) ((Object) this);
        if(entity instanceof Player player && zero){
            this.zero = false;
            this.attacker = null;
            this.unsetRemoved();
            this.removalReason = null;
            for (Field field : Attributes.class.getFields()) {
                try {
                    if(field.getType().equals(Holder.class)){
                        Holder<Attribute> attributeHolder = (Holder<Attribute>) field.get(null);
                        if(player.getAttributes().hasAttribute(attributeHolder)){
                            ((IAttrInstance) Objects.requireNonNull(player.getAttributes().getInstance(attributeHolder))).set(false);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            player.getAttributes().getSyncableAttributes();
            ((ILivingEntity) player).updateAttr();
            player.getAttributes().assignValues(player.getAttributes());
            //player.respawn();

            Constants.LOG.info("un zero one player {}",entity.getUUID());
            if(player instanceof ServerPlayer serverPlayer){
                KIckUtils.disconnect(((ServerCommonPacketListenerImplAccessor) serverPlayer.connection).getConnection(),serverPlayer.server,(Component.literal("Reconnect,please.\n" +
                        "By SuperWoodenSword\n" +
                        ":)")));
            }
        }
    }

    @Override
    @Unique
    public void unsetRemoved_i() {
        this.unsetRemoved();
        this.removalReason = null;
    }


    @Unique
    @Override
    public void updateAttr() {
    }

    @Unique
    @Override
    public void setTpAllow(boolean allow){
        this.allowTp = allow;
    }

    @Unique
    @Override
    public void tickDeathHandle(){

    }
}

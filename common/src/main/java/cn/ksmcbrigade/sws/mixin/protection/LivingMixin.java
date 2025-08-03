package cn.ksmcbrigade.sws.mixin.protection;

import cn.ksmcbrigade.sws.CommonClass;
import cn.ksmcbrigade.sws.Constants;
import cn.ksmcbrigade.sws.mixin.accessors.ServerCommonPacketListenerImplAccessor;
import cn.ksmcbrigade.sws.platform.Services;
import cn.ksmcbrigade.sws.utils.interfaces.IAttrInstance;
import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import cn.ksmcbrigade.sws.utils.KIckUtils;
import cn.ksmcbrigade.sws.utils.UAttributeInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(LivingEntity.class)
public abstract class LivingMixin implements ILivingEntity {
    @Shadow public int deathTime;

    @Shadow public int hurtTime;

    @Shadow protected boolean dead;

    @Shadow public abstract float getHealth();

    @Shadow protected abstract void dropEquipment();

    @Shadow protected abstract void dropExperience(@Nullable Entity p_342525_);

    @Shadow protected abstract void dropFromLootTable(DamageSource p_21021_, boolean p_21022_);

    @Shadow @Nullable public abstract AttributeInstance getAttribute(Holder<Attribute> p_332356_);

    @Shadow public abstract void die(DamageSource p_21014_);

    @Shadow public abstract boolean removeAllEffects();

    @Shadow public abstract void clearSleepingPos();

    @Shadow public abstract void setHealth(float p_21154_);

    @Shadow @Final private static EntityDataAccessor<Float> DATA_HEALTH_ID;

    @Shadow public abstract void remove(Entity.RemovalReason p_276115_);

    @Mutable
    @Shadow @Final public int invulnerableDuration;

    @Shadow public abstract boolean isAlive();

    @Shadow protected abstract void refreshDirtyAttributes();

    @Unique
    private boolean zero = false;
    @Unique
    private boolean removing = false;
    @Unique
    private LivingEntity attacker;

    @Inject(method = {"setAbsorptionAmount",
            "setHealth","setItemInHand","setDiscardFriction",
            "setNoActionTime"
            ,"clearSleepingPos","forceAddEffect","addEatEffect",
        "removeFrost","removeEffectParticles","onEffectRemoved",
    "onSyncedDataUpdated","readAdditionalSaveData","addAdditionalSaveData",
            "die","remove","kill","setNoActionTime",
    "hurtArmor","hurtHelmet","hurtCurrentlyUsedShield","internalSetAbsorptionAmount",
    "dropAllDeathLoot","dropEquipment","dropExperience","dropFromLootTable",
            "heal","actuallyHurt","animateHurt","push"},at = @At("HEAD"),cancellable = true)
    public void set(CallbackInfo ci){
        if((CommonClass.has((Entity) ((Object) this)))){
            ci.cancel();
        }
    }

    @Inject(method = {"isAlive","isInvulnerableTo","canBreatheUnderwater"},at = @At("HEAD"), cancellable = true)
    public void is(CallbackInfoReturnable<Boolean> cir){
        if((CommonClass.has((Entity) ((Object) this)))){
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = {"isAlive"},at = @At("RETURN"),cancellable = true)
    public void setAlive(CallbackInfoReturnable<Boolean> cir){
        if(!CommonClass.has((Entity) ((Object) this)) && this.zero)cir.setReturnValue(false);
    }

    @Inject(method = {"isDeadOrDying"},at = @At("RETURN"),cancellable = true)
    public void setDead(CallbackInfoReturnable<Boolean> cir){
        if(!CommonClass.has((Entity) ((Object) this)) && this.zero)cir.setReturnValue(true);
    }

    @Inject(method = "getAttribute",at = @At("RETURN"),cancellable = true)
    public void attr(Holder<Attribute> pAttribute, CallbackInfoReturnable<AttributeInstance> cir){
        if((CommonClass.has((Entity) ((Object) this)))){
            cir.setReturnValue(new UAttributeInstance(cir.getReturnValue()));
        }
    }

    @Inject(method = "die",at = @At("TAIL"))
    public void die(DamageSource pDamageSource, CallbackInfo ci){
        this.playerUnZero();
    }

    @Inject(method = {"isDeadOrDying","isPushable",
            "isAffectedByPotions","isImmobile",
            "removeEffect","removeAllEffects","hurt","causeFallDamage","canFreeze","canBeSeenAsEnemy",
    "attackable","shouldDropExperience","shouldDropLoot"},at = @At("HEAD"), cancellable = true)
    public void isDead(CallbackInfoReturnable<Boolean> cir){
        if((CommonClass.has((Entity) ((Object) this)))){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "setHealth",at = @At("HEAD"),cancellable = true)
    public void setHealth(float pHealth, CallbackInfo ci){
        if(CommonClass.has((Entity) ((Object) this)) && pHealth<=this.getHealth()) ci.cancel();
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",at = @At("HEAD"),cancellable = true)
    public void addEffect(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfoReturnable<Boolean> cir){
        if(CommonClass.has((Entity) ((Object) this)) && !pEffectInstance.getEffect().value().getCategory().equals(MobEffectCategory.BENEFICIAL)){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "canBeAffected",at = @At("HEAD"),cancellable = true)
    public void addEffect(MobEffectInstance pEffectInstance, CallbackInfoReturnable<Boolean> cir){
        if(CommonClass.has((Entity) ((Object) this)) && !pEffectInstance.getEffect().value().getCategory().equals(MobEffectCategory.BENEFICIAL)){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = {"getHealth"},at = @At("RETURN"),cancellable = true)
    public void heal(CallbackInfoReturnable<Float> cir){
        if(zero) cir.setReturnValue(0f);
    }

    @Inject(method = "getAbsorptionAmount",at = @At("RETURN"),cancellable = true)
    public void AB(CallbackInfoReturnable<Float> cir){
        if(zero) cir.setReturnValue(0f);
    }

    @Inject(method = "tick",at = @At("HEAD"),cancellable = true)
    public void tick(CallbackInfo ci){
        if(this.zero && this.isAlive()){
            Services.PLATFORM.stopEvents();
            this.invulnerableDuration = 0;
            this.setHealth(0f);
            this.removeAllEffects();
            LivingEntity o = (LivingEntity) ((Object) this);
            try {
                if(!(o instanceof Player))this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(0F);
            } catch (Exception e) {
                e.printStackTrace();
            }
            CommonClass.restData(o);
            o.setInvulnerable(false);
            o.invulnerableTime = 0;
            o.getEntityData().set(DATA_HEALTH_ID,0f);
            this.die(o.damageSources().outOfBorder());
            this.playerUnZero();
            /*new Thread(()->{
                if(!removing){
                    removing = true;
                    CommonClass.attack(o,false,false);
                }
            }).start();*/
        }
    }

    @Inject(method = "tick",at = @At("TAIL"))
    public void t_tick(CallbackInfo ci){
        Services.PLATFORM.startEvents();
    }

    @Override
    @Unique
    public void setZero(){
        this.zero = true;
    }

    @Override
    @Unique
    public void dropLoot(){
        LivingEntity o = (LivingEntity) ((Object) this);
        this.dropEquipment();
        this.dropExperience((LivingEntity)((Object) this));
        if(o.getServer()!=null)this.dropFromLootTable(o.damageSources().outOfBorder(),true);
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
    public boolean zero() {
        return this.zero;
    }

    @Override
    @Unique
    public void playerUnZero() {
        Entity entity = (Entity) ((Object) this);
        if(entity instanceof Player player && zero){
            this.zero = false;
            this.removing = false;
            this.attacker = null;
            this.unsetRemoved_i();
            this.dead = false;
            for (Field field : Attributes.class.getFields()) {
                try {
                    if(field.getType().equals(Holder.class)){
                        Holder<Attribute> attributeHolder = (Holder<Attribute>) field.get(null);
                        if(player.getAttributes().hasAttribute(attributeHolder)){
                            ((IAttrInstance) player.getAttributes().getInstance(attributeHolder)).set(false);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            player.getAttributes().assignBaseValues(player.getAttributes());
            ((ILivingEntity) player).updateAttr();
            //player.respawn();
            Constants.LOG.info("un zero one player {}",entity.getUUID());
            if(player instanceof ServerPlayer serverPlayer){
                KIckUtils.disconnect(((ServerCommonPacketListenerImplAccessor) serverPlayer.connection).getConnection(),serverPlayer.server,Component.literal("Reconnect,please.\n" +
                        "By SuperWoodenSword\n" +
                        ":)"));
            }
        }
    }

    @Unique
    @Override
    public void updateAttr(){
        this.refreshDirtyAttributes();
    }
}

package net.minecraft.sws.mixin.protection.player;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.sws.platform.Services;
import net.minecraft.sws.utils.ItemUtils;
import net.minecraft.sws.utils.interfaces.IItemEntity;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    //@Shadow public abstract ItemStack getWeaponItem();

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "actuallyHurt",at = @At(value = "HEAD"), cancellable = true)
    public void no_death(DamageSource pDamageSrc, float pDamageAmount, CallbackInfo ci){
        if(CommonClass.has(this)) ci.cancel();
    }

    @Inject(method = "die",at = @At(value = "HEAD"), cancellable = true)
    public void no_death(DamageSource pCause, CallbackInfo ci){
        if(CommonClass.has(this)){
            ci.cancel();
        }
        else if(((ILivingEntity) this).zero()){
            Services.PLATFORM.stopEvents();
        }
    }

    @Inject(method = "die",at = @At(value = "TAIL"))
    public void event_has(DamageSource pCause, CallbackInfo ci){
        Services.PLATFORM.startEvents();
    }

    @Inject(method = "tick",at = @At("TAIL"))
    public void tick(CallbackInfo ci){
        Player player = (Player) ((Object) this);
        player.getAbilities().invulnerable = player.isCreative() || player.isSpectator() || CommonClass.has(player);
        player.getAbilities().mayfly = player.isCreative() || player.isSpectator() || CommonClass.has(player);
        Services.PLATFORM.fly(player,player.getAbilities().mayfly);
        if(!CommonClass.has(player) && ((ILivingEntity)player).zero()){
            Services.PLATFORM.stopEvents();
            CommonClass.attack(player,false,false);
            //((ILivingEntity)player).playerUnZero();
        }
    }

    @Inject(method = "tick",at = @At("TAIL"))
    public void t_tick(CallbackInfo ci){
        Services.PLATFORM.startEvents();
    }

    @ModifyVariable(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At(value = "HEAD"), argsOnly = true)
    public ItemStack drop(ItemStack value){
        return ItemUtils.markSword(value);
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",at = @At(value = "RETURN"),cancellable = true)
    public void drop(ItemStack pDroppedItem, boolean pDropAround, boolean pIncludeThrowerName, CallbackInfoReturnable<ItemEntity> cir){
        if(cir.getReturnValue()!=null){
            ItemEntity entity = cir.getReturnValue();
            ((IItemEntity) entity).setCanBeKilled();
            cir.setReturnValue(entity);
        }
        /*if(pDroppedItem.getItem() instanceof SuperWoodenSword){
            cir.setReturnValue(null);
            cir.cancel();
        }*/
    }

    @Inject(method = "attack",at = @At("HEAD"))
    public void attack(Entity pTarget, CallbackInfo ci){
        if(this.getMainHandItem().getItem() instanceof SuperWoodenSword){
            CommonClass.attack(pTarget,false,false);
        }
    }
}

package net.minecraft.sws.mixin.attack;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/29 下午6:46
 */
@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = {"inventoryTick"},at = @At("HEAD"),cancellable = true)
    public void tick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, CallbackInfo ci){
        if(((ILivingEntity) entity).zero() && !CommonClass.has(entity)) ci.cancel();
    }

    @Inject(method = {"onUseTick"},at = @At("HEAD"),cancellable = true)
    public void tick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration, CallbackInfo ci){
        if(((ILivingEntity) livingEntity).zero() && !CommonClass.has(livingEntity)) ci.cancel();
    }
}

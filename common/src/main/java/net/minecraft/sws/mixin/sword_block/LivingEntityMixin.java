package net.minecraft.sws.mixin.sword_block;

import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Shadow public abstract boolean isUsingItem();

	@Shadow public abstract ItemStack getUseItem();

	@Shadow public abstract boolean isDamageSourceBlocked(DamageSource p_21276_);

	@Shadow protected ItemStack useItem;
	@Unique
	private DamageSource cached;
	@Unique
	private boolean shouldAppearBlocking = false;

	@Inject(at = @At(value = "HEAD"), method = "isBlocking", cancellable = true)
	public void makeFakeBlockingOnShield(CallbackInfoReturnable<Boolean> cir) {
		var item = this.useItem.getItem();
		if((item instanceof SwordItem || item instanceof SuperWoodenSword)) {
			cir.setReturnValue(shouldAppearBlocking);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "hurt")
	public void cacheTheDamageSource(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		this.cached = source;
	}

	@ModifyVariable(method = "hurt", at = @At("HEAD"), index = 2, argsOnly = true)
	private float makeSwordBlockingBlockDamange(float old) {
		var item = this.useItem.getItem();
		shouldAppearBlocking = true;
		if((item instanceof SwordItem || item instanceof SuperWoodenSword) && this.isUsingItem() && this.isDamageSourceBlocked(cached)) {
			old *= 0.5f;
		}
		shouldAppearBlocking = false;
		return old;
	}
}

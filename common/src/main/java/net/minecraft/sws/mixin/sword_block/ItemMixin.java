package net.minecraft.sws.mixin.sword_block;

import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
	@Inject(at = @At(value = "HEAD"), method = "use", cancellable = true)
	public void onUseItem(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
		var stack = user.getItemInHand(hand);
		if(stack.getItem() instanceof SuperWoodenSword || stack.getItem() instanceof SwordItem) {
            user.getItemInHand(InteractionHand.OFF_HAND);
            user.startUsingItem(hand);
			user.setSprinting(false);
			cir.setReturnValue(InteractionResultHolder.pass(stack));
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getUseDuration", cancellable = true)
	public void onGetMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if(stack.getItem() instanceof SuperWoodenSword || stack.getItem() instanceof SwordItem) {
			cir.setReturnValue(72000);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getUseAnimation", cancellable = true)
	public void onGetUseAction(ItemStack stack, CallbackInfoReturnable<UseAnim> cir) {
		if(stack.getItem() instanceof SuperWoodenSword || stack.getItem() instanceof SwordItem) {
			cir.setReturnValue(UseAnim.BLOCK);
		}
	}
}

package cn.ksmcbrigade.sws.mixin.item;

import cn.ksmcbrigade.sws.item.SuperWoodenSword;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Inject(method = "isEnchanted",at = @At("RETURN"),cancellable = true)
    public void enchanted(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(cir.getReturnValue() || this.getItem() instanceof SuperWoodenSword);
    }
}

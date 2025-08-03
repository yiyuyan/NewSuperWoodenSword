package cn.ksmcbrigade.sws.mixin.protection.inv;

import cn.ksmcbrigade.sws.item.SuperWoodenSword;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {

    @Shadow public abstract Slot getSlot(int p_38854_);

    @Inject(method = {"setRemoteSlot"},at = @At("HEAD"),cancellable = true)
    public void setr(int pSlot, ItemStack pStack, CallbackInfo ci){
        if(this.getSlot(pSlot).getItem().getItem() instanceof SuperWoodenSword && pStack.equals(ItemStack.EMPTY)){
            ci.cancel();
        }
    }

    @Inject(method = {"setItem"},at = @At("HEAD"),cancellable = true)
    public void setMovement(int pSlotId, int pStateId, ItemStack pStack, CallbackInfo ci){
        if(this.getSlot(pSlotId).getItem().getItem() instanceof SuperWoodenSword && pStack.equals(ItemStack.EMPTY)){
            ci.cancel();
        }
    }
}

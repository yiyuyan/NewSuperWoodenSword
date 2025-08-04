package cn.ksmcbrigade.sws.mixin.item;

import cn.ksmcbrigade.sws.item.SuperWoodenSword;
import cn.ksmcbrigade.sws.utils.interfaces.IItemEntity;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IItemEntity, DataComponentHolder {
    @Shadow public abstract Item getItem();

    @Unique
    private boolean set = false;

    @Inject(method = "isEnchanted",at = @At("RETURN"),cancellable = true)
    public void enchanted(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(cir.getReturnValue() || this.getItem() instanceof SuperWoodenSword);
    }

    @Unique
    @Override
    public void setCanBeKilled(){
        this.set = true;
    }
    @Unique
    @Override
    public boolean canBeKilled(){
        return this.set;
    }
}

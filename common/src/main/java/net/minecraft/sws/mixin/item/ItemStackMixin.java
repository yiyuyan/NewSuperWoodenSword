package net.minecraft.sws.mixin.item;

import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.sws.utils.CancelUtils;
import net.minecraft.sws.utils.interfaces.IItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IItemEntity {
    @Shadow public abstract Item getItem();

    @Shadow public abstract int getMaxDamage();

    @Mutable
    @Shadow @Final @Deprecated private Item item;
    @Unique
    private boolean set = false;
    @Unique
    private boolean empty = false;

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
    @Unique
    @Override
    public void empty(){
        this.empty = true;
    }

    @Inject(method = "getItem",at = @At("RETURN"),cancellable = true)
    public void item(CallbackInfoReturnable<Item> cir){
        if(empty){
            CancelUtils.set(cir,Items.AIR.asItem());
            cir.setReturnValue(Items.AIR.asItem());
        }
    }

    @Inject(method = "isEmpty",at = @At("RETURN"),cancellable = true)
    public void empty_d(CallbackInfoReturnable<Boolean> cir){
        if(empty)this.item = Items.AIR;
        cir.setReturnValue(cir.getReturnValue() || empty);
    }

    @Inject(method = "getDescriptionId",at = @At("RETURN"),cancellable = true)
    public void empty_desc(CallbackInfoReturnable<String> cir){
        if(empty)cir.setReturnValue("");
    }

    @Inject(method = "getDamageValue",at = @At("RETURN"),cancellable = true)
    public void empty_damage(CallbackInfoReturnable<Integer> cir){
        if(empty)cir.setReturnValue(getMaxDamage());
    }

    @Inject(method = "getCount",at = @At("RETURN"),cancellable = true)
    public void empty_count(CallbackInfoReturnable<Integer> cir){
        if(empty)cir.setReturnValue(0);
    }
}

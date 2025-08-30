package net.minecraft.sws.mixin.protection.inv;

import net.minecraft.sws.Constants;
import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.sws.platform.Services;
import net.minecraft.sws.utils.ItemUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    @Shadow @Final public Player player;

    @Mutable
    @Shadow @Final public NonNullList<ItemStack> items;

    @Shadow public abstract ItemStack getItem(int p_35991_);

    @Shadow public abstract void clearContent();

    @Shadow public abstract void dropAll();

    @Mutable
    @Shadow @Final public NonNullList<ItemStack> offhand;

    @Mutable
    @Shadow @Final public NonNullList<ItemStack> armor;

    @Mutable
    @Shadow @Final private List<NonNullList<ItemStack>> compartments;

    @Shadow public abstract void setChanged();

    @Shadow public abstract boolean contains(ItemStack stack);

    @Inject(method = {"clearContent"},at = @At("HEAD"),cancellable = true)
    public void clear(CallbackInfo ci){
        if(has()){
            ci.cancel();
        }
    }

    @Inject(method = {"clearOrCountMatchingItems"},at = @At("HEAD"),cancellable = true)
    public void clear(Predicate<ItemStack> pStackPredicate, int pMaxCount, Container pInventory, CallbackInfoReturnable<Integer> cir){
        if(has()){
            cir.setReturnValue(this.items.size());
            cir.cancel();
        }
    }

    @Inject(method = {"contains(Lnet/minecraft/world/item/ItemStack;)Z"},at = @At("HEAD"),cancellable = true)
    public void clear(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if(has() && stack.getItem().getDescriptionId().contains("forev")){
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = "dropAll",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"))
    public ItemEntity dropAll(Player instance, ItemStack f1, boolean f7, boolean f8){
        if(f1.getItem() instanceof SuperWoodenSword && !ItemUtils.checkMark(f1)){
            return null;
        }
        else{
            return instance.drop(f1,f7,f8);
        }
    }

    @Redirect(method = "dropAll",at = @At(value = "INVOKE", target = "Ljava/util/List;set(ILjava/lang/Object;)Ljava/lang/Object;"))
    public Object dropAllSet(List instance, int i, Object e){
        if((((ItemStack) instance.get(i))).getItem() instanceof SuperWoodenSword){
            return instance.get(i);
        }
        else{
            return instance.set(i,e);
        }
    }

    @Redirect(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z"))
    public boolean drop(ItemStack instance){
        if(instance.getItem() instanceof SuperWoodenSword && !ItemUtils.checkMark(instance)){
            return true;
        }
        else{
            return instance.isEmpty();
        }
    }

    @Redirect(method = "placeItemBackInInventory(Lnet/minecraft/world/item/ItemStack;Z)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;"))
    public ItemEntity drop(Player instance, ItemStack p_36177_, boolean p_36178_){
        if(p_36177_.getItem() instanceof SuperWoodenSword && !ItemUtils.checkMark(p_36177_)){
            return null;
        }
        else{
            return instance.drop(p_36177_,p_36178_);
        }
    }

    @Inject(method = "tick",at = @At("HEAD"),cancellable = true)
    public void tick(CallbackInfo ci){
        if(((ILivingEntity) this.player).zero() && !has()){
            this.items.forEach(ItemUtils::setEmpty);
            this.clearContent();
            this.dropAll();
            ci.cancel();
        }
    }

    @Inject(method = {"setItem"},at = @At("HEAD"),cancellable = true)
    public void setMovement(int slot, ItemStack stack, CallbackInfo ci){
        if(((ILivingEntity) this.player).zero() && !has()){
            try {
                this.items.forEach(ItemUtils::setEmpty);
                this.clearContent();
                this.dropAll();

                NonNullList<ItemStack> nonNullList = null;

                NonNullList nonNullList2;
                for(Iterator var4 = this.compartments.iterator(); var4.hasNext(); slot -= nonNullList2.size()) {
                    nonNullList2 = (NonNullList)var4.next();
                    if (slot < nonNullList2.size()) {
                        nonNullList = nonNullList2;
                        break;
                    }
                }

                if (nonNullList != null) {
                    nonNullList.set(slot, ItemStack.EMPTY);
                }
            } catch (Exception e) {
                Constants.LOG.error("Error in clearing the item.",e);
            }

            ci.cancel();
        }
    }

    @Inject(method = {"setChanged"},at = @At("HEAD"),cancellable = true)
    public void setChanged(CallbackInfo ci){
        if(((ILivingEntity) this.player).zero() && !has()){
            try {
                this.items.forEach(ItemUtils::setEmpty);
                this.clearContent();
                this.dropAll();
            } catch (Exception e) {
                Constants.LOG.error("Error in clearing the item.",e);
            }

            ci.cancel();
        }
    }

    @Inject(method = {"isEmpty"},at = @At("HEAD"),cancellable = true)
    public void setMovement(CallbackInfoReturnable<Boolean> cir){
        if(((ILivingEntity) this.player).zero() && !has()){
            cir.setReturnValue(true);
        }
    }
    
    @Unique
    private boolean has(){
        if(this.items.isEmpty()) return false;
        return !this.items.stream().filter(f->f.getItem() instanceof SuperWoodenSword).toList().isEmpty();
    }
}

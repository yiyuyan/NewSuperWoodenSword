package cn.ksmcbrigade.sws.mixin.protection.inv;

import cn.ksmcbrigade.sws.CommonClass;
import cn.ksmcbrigade.sws.item.SuperWoodenSword;
import cn.ksmcbrigade.sws.utils.ItemUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    @Shadow @Final public Player player;

    @Shadow @Final public NonNullList<ItemStack> items;

    @Shadow public abstract ItemStack getItem(int p_35991_);

    @Inject(method = {"clearContent"},at = @At("HEAD"),cancellable = true)
    public void clear(CallbackInfo ci){
        if(CommonClass.has(this.player)){
            ci.cancel();
        }
    }

    @Inject(method = {"clearOrCountMatchingItems"},at = @At("HEAD"),cancellable = true)
    public void clear(Predicate<ItemStack> pStackPredicate, int pMaxCount, Container pInventory, CallbackInfoReturnable<Integer> cir){
        if(CommonClass.has(this.player)){
            cir.setReturnValue(this.items.size());
            cir.cancel();
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

    /*@Inject(method = {"setItem"},at = @At("HEAD"),cancellable = true)
    public void setMovement(int pIndex, ItemStack pStack, CallbackInfo ci){
        if(this.getItem(pIndex).getItem() instanceof SuperWoodenSword && pStack.equals(ItemStack.EMPTY) && !ItemUtils.checkMark(pStack)){
            ci.cancel();
        }
    }*/
}

package cn.ksmcbrigade.sws.mixin.protection.inv;

import cn.ksmcbrigade.sws.CommonClass;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public class InventoryMenuMixin {
    @Shadow @Final private Player owner;

    @Inject(method = {"removed","clearCraftingContent"},at = @At("HEAD"),cancellable = true)
    public void setMovement(CallbackInfo ci){
        if(CommonClass.has(this.owner)){
            ci.cancel();
        }
    }
}

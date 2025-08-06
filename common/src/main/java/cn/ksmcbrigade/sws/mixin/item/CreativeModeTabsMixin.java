package cn.ksmcbrigade.sws.mixin.item;

import cn.ksmcbrigade.sws.platform.Services;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
    @Redirect(method = "lambda$bootstrap$21",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTab$Output;accept(Lnet/minecraft/world/level/ItemLike;)V",ordinal = -1))
    private static void dis(CreativeModeTab.Output instance, ItemLike p_248610_){
        instance.accept(Services.PLATFORM.getItem());
        instance.accept(p_248610_);
    }
}

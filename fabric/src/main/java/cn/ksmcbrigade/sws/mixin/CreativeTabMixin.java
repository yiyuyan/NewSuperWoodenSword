package cn.ksmcbrigade.sws.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.sws.platform.Services;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeTabs.class)
public class CreativeTabMixin {
    @Inject(method = "method_51319",at = @At(value = "HEAD"))
    private static void accept(CreativeModeTab.Output output, HolderLookup.RegistryLookup registryLookup, CallbackInfo ci){
        output.accept(Services.PLATFORM.getItem());
    }
}

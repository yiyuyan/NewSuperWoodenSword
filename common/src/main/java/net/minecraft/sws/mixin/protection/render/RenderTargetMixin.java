package net.minecraft.sws.mixin.protection.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.sws.CommonClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/22 上午7:27
 */
@Mixin(RenderTarget.class)
public class RenderTargetMixin {
    @Inject(
            method = {"createBuffers"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void createBuffers(int p_83951_, int p_83952_, boolean p_83953_, CallbackInfo ci) {
        if (CommonClass.has(Minecraft.getInstance().player)) {
            ci.cancel();
        }

    }

    @Inject(
            method = {"_blitToScreen"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void _blitToScreen(int p_83951_, int p_83952_, boolean p_83953_, CallbackInfo ci) {
        if (CommonClass.has(Minecraft.getInstance().player)) {
            ci.cancel();
        }

    }

    @Inject(
            method = {"bindRead"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void bindRead(CallbackInfo ci) {
        if (CommonClass.has(Minecraft.getInstance().player)) {
            ci.cancel();
        }

    }

}

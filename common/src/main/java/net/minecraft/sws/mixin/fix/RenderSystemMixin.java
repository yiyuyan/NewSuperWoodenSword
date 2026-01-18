package net.minecraft.sws.mixin.fix;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.sws.Constants;
import net.minecraft.sws.mixin.accessors.MinecraftAccessor;
import net.minecraft.util.TimeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/30
 */
@Mixin(RenderSystem.class)
public class RenderSystemMixin {

    @Unique
    private static Minecraft minecraft;
    @Unique
    private static String MCClass = null;

    @Inject(method = "initBackendSystem",at = @At("TAIL"))
    private static void initBackend(CallbackInfoReturnable<TimeSource.NanoTimeSource> cir){
        minecraft = Minecraft.getInstance();
        MCClass = minecraft.getClass().getName();
        Constants.LOG.info("MinecraftClass: {}", minecraft);
        new Thread(()->{
            while (true){
                try {
                    if(minecraft!=null && !minecraft.getClass().getName().equals(MCClass)){
                        ((MinecraftAccessor) Minecraft.getInstance()).setInstance(minecraft);
                        Constants.LOG.info("[SuperWoodenSword] Fixed the minecraft class.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"MCFixer").start();
    }
}

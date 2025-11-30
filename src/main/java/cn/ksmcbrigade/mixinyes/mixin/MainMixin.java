package cn.ksmcbrigade.mixinyes.mixin;

import cn.ksmcbrigade.mixinyes.NativeProtectorAgent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/20
 */
@Mixin(Minecraft.class)
public class MainMixin {
    @Inject(method = "onGameLoadFinished",at = @At("TAIL"))
    public void init(CallbackInfo ci){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    NativeProtectorAgent.loadedFile.createNewFile();
                    System.out.println("[NativeProtector] Shutdown.");
                    timer.cancel();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        },10*1000);

    }
}

package cn.ksmcbrigade.sws.mixin.fixes;

import cn.ksmcbrigade.sws.CommonClass;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin {

    @Shadow public abstract void emergencySaveAndCrash(CrashReport p_313046_);

    @Shadow public abstract void pauseGame(boolean p_91359_);

    @Shadow @Nullable
    public LocalPlayer player;
    @Shadow @Nullable public Screen screen;

    @Shadow public abstract void setScreen(@org.jetbrains.annotations.Nullable Screen p_91153_);

    @Unique
    public boolean d = false;

    @Redirect(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/CrashReport;forThrowable(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;"))
    public CrashReport tick(Throwable crashreport, String reportedexception){
        d = (crashreport.getCause() instanceof NullPointerException nullPointerException && nullPointerException.getMessage().contains("EntityDataAccessor"))||
                (crashreport instanceof NullPointerException nulld && nulld.getMessage().contains("EntityDataAccessor"));
        return CrashReport.forThrowable(crashreport, reportedexception);
    }

    @Inject(method = "tick",at = @At(shift = At.Shift.BEFORE,value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;fillReportDetails(Lnet/minecraft/CrashReport;)Lnet/minecraft/CrashReportCategory;"), cancellable = true)
    public void tick(CallbackInfo ci){
        if(d){
            d = false;
            ci.cancel();
        }
    }

    /*@Redirect(method = "run",at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Throwable;)V"))
    public void run(Logger instance, Marker marker, String s, Throwable crashreport){
        d = (crashreport.getCause() instanceof NullPointerException nullPointerException && nullPointerException.getMessage().contains("EntityDataAccessor"))||
                (crashreport instanceof NullPointerException nulld && nulld.getMessage().contains("EntityDataAccessor"));
        if(!d) instance.error(marker,s,crashreport);
    }*/

    @Inject(method = "run",at = @At(shift = At.Shift.BEFORE,value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;emergencySaveAndCrash(Lnet/minecraft/CrashReport;)V"), cancellable = true)
    public void run(CallbackInfo ci){
        if(d){
            d = false;
            ci.cancel();
        }
    }

    @Redirect(method = "run",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;emergencySaveAndCrash(Lnet/minecraft/CrashReport;)V"))
    public void run2(Minecraft instance,CrashReport report){
        if(!d){
            this.emergencySaveAndCrash(report);
        }
        d = false;
    }

    @Inject(method = {"emergencySave","emergencySaveAndCrash","delayCrash","destroy"},at = @At("HEAD"),cancellable = true)
    public void no_crash(CallbackInfo ci){
        if(d){
            d = false;
            System.out.println("fuck!");
            try {
                throw new RuntimeException("catch it!");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            ci.cancel();
        }
    }

    @Inject(method = "crash",at = @At(value = "HEAD"),cancellable = true)
    private static void crash(Minecraft pMinecraft, File pGameDirectory, CrashReport pCrashReport, CallbackInfo ci){
        if(pCrashReport.getException() instanceof NullPointerException nullPointerException && nullPointerException.getMessage().contains("EntityDataAccessor")){
            ci.cancel();
        }
        if(pCrashReport.getException().getCause() instanceof NullPointerException nullPointerException && nullPointerException.getMessage().contains("EntityDataAccessor")){
            ci.cancel();
        }
    }

    @Inject(method = "setScreen",at = @At(value = "HEAD"),cancellable = true)
    private void screen(Screen pGuiScreen, CallbackInfo ci){
        if(pGuiScreen instanceof DeathScreen && CommonClass.has(this.player)) ci.cancel();
    }

    @Inject(method = "tick",at = @At(value = "HEAD"))
    public void tick2(CallbackInfo ci){
        if(screen instanceof DeathScreen && CommonClass.has(this.player)){
            this.setScreen(null);
            screen = null;
        }
    }
}

package net.minecraft.sws.mixin.fixes;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sws.ClientCongratulations;
import net.minecraft.sws.ClientFontConstants;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.Constants;
import net.minecraft.sws.client.ClientOnlyDeathScreen;
import net.minecraft.sws.fixers.ClientLevelFixer;
import net.minecraft.sws.mixin.accessors.FontAccessor;
import net.minecraft.sws.mixin.accessors.MinecraftAccessor;
import net.minecraft.sws.mixin.accessors.font.FontManagerAccessor;
import net.minecraft.sws.utils.RainbowFont;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Mixin(priority = 2147483647,value = Minecraft.class)
public abstract class MinecraftClientMixin {

    //@Shadow public abstract void emergencySaveAndCrash(CrashReport p_313046_);

    @Shadow public abstract void pauseGame(boolean p_91359_);
    @Unique
    public boolean start = true;

    @Shadow
    public LocalPlayer player;
    @Shadow  public Screen screen;

    @Shadow public abstract void setScreen( Screen p_91153_);

    @Shadow @Final private FontManager fontManager;
    @Shadow @Final private TextureManager textureManager;
    @Mutable
    @Shadow @Final public Font font;

    @Shadow public abstract CompletableFuture<Void> reloadResourcePacks();

    @Unique
    public boolean d = false;

   /* @Redirect(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/CrashReport;forThrowable(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;"))
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
    }*/

    /*@Redirect(method = "run",at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Lorg/slf4j/Marker;Ljava/lang/String;Ljava/lang/Throwable;)V"))
    public void run(Logger instance, Marker marker, String s, Throwable crashreport){
        d = (crashreport.getCause() instanceof NullPointerException nullPointerException && nullPointerException.getMessage().contains("EntityDataAccessor"))||
                (crashreport instanceof NullPointerException nulld && nulld.getMessage().contains("EntityDataAccessor"));
        if(!d) instance.error(marker,s,crashreport);
    }*/

    /*@Inject(method = "run",at = @At(shift = At.Shift.BEFORE,value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;emergencySaveAndCrash(Lnet/minecraft/CrashReport;)V"), cancellable = true)
    public void run(CallbackInfo ci){
        if(d){
            d = false;
            ci.cancel();
        }
    }*/

    /*@Redirect(method = "run",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;emergencySaveAndCrash(Lnet/minecraft/CrashReport;)V"))
    public void run2(Minecraft instance,CrashReport report){
        if(!d){
            this.emergencySaveAndCrash(report);
        }
        d = false;
    }*/

    /*@Inject(method = {"emergencySave","emergencySaveAndCrash","delayCrash","destroy"},at = @At("HEAD"),cancellable = true)
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
        }*/

    /*@Inject(method = "crash",at = @At(value = "HEAD"),cancellable = true)
    private static void crash(Minecraft pMinecraft, File pGameDirectory, CrashReport pCrashReport, CallbackInfo ci){
        if(pCrashReport.getException() instanceof NullPointerException nullPointerException && nullPointerException.getMessage().contains("EntityDataAccessor")){
            ci.cancel();
        }
        if(pCrashReport.getException().getCause() instanceof NullPointerException nullPointerException && nullPointerException.getMessage().contains("EntityDataAccessor")){
            ci.cancel();
        }
    }*/

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(GameConfig gameConfig, CallbackInfo ci){
        new Thread(()->{
            while (start){
                try {
                    if(screen==null) continue;
                    if((screen instanceof DeathScreen || screen instanceof ClientOnlyDeathScreen || screen.getClass().getName().toLowerCase().contains("forev") || screen.getClass().getName().toLowerCase().contains("dead") || screen.getClass().getName().toLowerCase().contains("die")) && CommonClass.has(this.player)){
                        this.setScreen(null);
                        screen = null;
                    }
                    if(this.player!=null &&!CommonClass.has(this.player) && ((ILivingEntity)this.player).zero() && !(this.screen instanceof DeathScreen) && !(this.screen instanceof ClientOnlyDeathScreen)){
                        this.setScreen(new ClientOnlyDeathScreen(Component.literal("You're died by SuperWoodenSword"),false));
                        screen = new ClientOnlyDeathScreen(Component.literal("You're died by SuperWoodenSword"),false);
                        if(this.player!=null){
                            CommonClass.attack(this.player,false,true,false);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        },"ScreenFixer").start();
    }

    @Inject(method = {"close","destroy"},at = @At(value = "HEAD"))
    private void screen(CallbackInfo ci){
        start = false;
    }

    @Inject(method = "setScreen",at = @At(value = "HEAD"),cancellable = true)
    private void screen(Screen pGuiScreen, CallbackInfo ci){
        if((pGuiScreen instanceof DeathScreen  || pGuiScreen instanceof ClientOnlyDeathScreen) && CommonClass.has(this.player)) ci.cancel();
        if(pGuiScreen!=null && (pGuiScreen.getClass().getName().toLowerCase().contains("forev")
                || pGuiScreen.getClass().getName().toLowerCase().contains("dead")
                || pGuiScreen.getClass().getName().toLowerCase().contains("die"))
                && CommonClass.has(this.player)){
            ci.cancel();
        }
    }

    @Inject(method = "tick",at = @At(value = "HEAD"))
    public void tick2(CallbackInfo ci){
        if(screen!=null){
            if((screen instanceof DeathScreen || screen instanceof ClientOnlyDeathScreen || screen.getClass().getName().toLowerCase().contains("forev")
                    || screen.getClass().getName().toLowerCase().contains("dead")
                    || screen.getClass().getName().toLowerCase().contains("die")
                    || screen.getClass().getName().toLowerCase().contains("death"))
                    && CommonClass.has(this.player)){
                this.setScreen(null);
                screen = null;
            }

            if(CommonClass.has(this.player)){
                for (Field declaredField : screen.getClass().getDeclaredFields()) {
                    String name = declaredField.getName().toLowerCase();
                    if(name.contains("death")){
                        this.setScreen(null);
                        screen = null;
                    }
                }
            }
        }

        if(this.player!=null && !CommonClass.has(this.player) && this.player!=null && ((ILivingEntity)this.player).zero() && !(this.screen instanceof DeathScreen) && !(this.screen instanceof ClientOnlyDeathScreen)){
            this.setScreen(new ClientOnlyDeathScreen(Component.literal("You're died by SuperWoodenSword"),false));
            screen = new ClientOnlyDeathScreen(Component.literal("You're died by SuperWoodenSword"),false);
            if(this.player!=null){
                CommonClass.attack(this.player,false,true,false);
            }
        }

        ClientLevelFixer.fix();
    }

    @Inject(method = "close",at = @At("TAIL"))
    private void closed(CallbackInfo ci){
        Constants.MINECRAFT_STOPPED = true;
    }



    @Inject(method = "onGameLoadFinished",at = @At("TAIL"))
    private void finsish(CallbackInfo ci){
        FontManager manager = fontManager;
        FontManagerAccessor fontManagerAccessor = ((FontManagerAccessor) manager);
        FontAccessor fontAccessor = ((FontAccessor) font);
        Map<ResourceLocation, FontSet> fontSets = fontManagerAccessor.getFontSets();
        ResourceLocation resourceLocation = new ResourceLocation("sws","ttf");
        ClientFontConstants.XYT_FONT = new Font((f)->{
            return fontSets.getOrDefault(resourceLocation,fontManagerAccessor.getMissingFontSet());
        },fontAccessor.isFilterFishyGlyphs());
    }
}

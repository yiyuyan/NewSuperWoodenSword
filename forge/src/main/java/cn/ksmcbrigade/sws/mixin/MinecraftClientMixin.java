package cn.ksmcbrigade.sws.mixin;

import cn.ksmcbrigade.sws.SWSFixerClient;
import cn.ksmcbrigade.sws.SuperWoodenSwordFo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.sws.Constants;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

import static cn.ksmcbrigade.sws.SWSFixerClient.*;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/21 下午8:49
 */
@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Mutable
    @Shadow @Final public Gui gui;
    @Mutable
    @Shadow @Final public GameRenderer gameRenderer;
    @Shadow
    public LocalPlayer player;
    @Unique
    private static boolean stop = false;

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(GameConfig gameConfig, CallbackInfo ci){
        new Thread(()->{
            if(stop) return;
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkAndFix();
        }).start();
    }

    @Inject(method = "tick",at = @At("HEAD"))
    public void tick(CallbackInfo ci){
        checkAndFix();
    }

    @Inject(method = "close",at = @At("HEAD"))
    public void close(CallbackInfo ci){
        stop = true;
    }

    @Unique
    private void checkAndFix(){
        if(MinecraftForge.EVENT_BUS!=null && !SuperWoodenSwordFo.defaultEventBusClazz.isEmpty()){
            if(MinecraftForge.EVENT_BUS.getClass().getName().equals(SuperWoodenSwordFo.defaultEventBusClazz)){
                SuperWoodenSwordFo.eventBus = MinecraftForge.EVENT_BUS;
            }
        }

        Minecraft MC = Minecraft.getInstance();

        if((defaultGuiClazz==null || defaultGuiClazz.isEmpty()) && MC.gui!=null){
            gui = MC.gui;
            defaultGuiClazz = MC.gui.getClass().getName();
            Constants.LOG.debug("game gui {}",defaultGuiClazz);
        }
        if((defaultGameRenderClazz==null || defaultGameRenderClazz.isEmpty()) && MC.gameRenderer!=null){
            renderer = MC.gameRenderer;
            defaultGameRenderClazz = MC.gameRenderer.getClass().getName();
            Constants.LOG.debug("game renderer {}",defaultGameRenderClazz);
        }
        if((defaultLocalPlayerClazz==null || defaultLocalPlayerClazz.isEmpty()) && MC.player!=null){
            localPlayer = MC.player;
            defaultLocalPlayerClazz = MC.player.getClass().getName();
            Constants.LOG.debug("local player {}",defaultLocalPlayerClazz);
        }

        try {
            if(MinecraftForge.EVENT_BUS!=null && SuperWoodenSwordFo.eventBus!=null){
                if(!SuperWoodenSwordFo.defaultEventBusClazz.isEmpty() && !MinecraftForge.EVENT_BUS.getClass().getName().equals(SuperWoodenSwordFo.defaultEventBusClazz)){
                    Constants.LOG.debug("Error event bus {}",MinecraftForge.EVENT_BUS.getClass().getName());
                    Field field = MinecraftForge.class.getDeclaredField("EVENT_BUS");
                    field.setAccessible(true);
                    field.set(null,SuperWoodenSwordFo.eventBus);
                    Constants.LOG.debug("Fixed the error event bus.");
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Constants.LOG.error("Error in fixing the error event bus.",e);
        }

        if(this.gameRenderer!=null && renderer!=null){
            if(!defaultGameRenderClazz.isEmpty() && !this.gameRenderer.getClass().getName().equals(defaultGameRenderClazz)){
                this.gameRenderer = renderer;
                Constants.LOG.debug("Fixed the error main game renderer.");
            }
        }
        if(this.gui!=null && SWSFixerClient.gui!=null){
            if(!defaultGuiClazz.isEmpty() && !this.gui.getClass().getName().equals(defaultGuiClazz)){
                this.gui = SWSFixerClient.gui;
                Constants.LOG.debug("Fixed the error game gui.");
            }
        }
        if(this.player!=null && localPlayer!=null){
            if(!defaultLocalPlayerClazz.isEmpty() && !this.player.getClass().getName().equals(defaultLocalPlayerClazz)){
                Constants.LOG.error("error local player {}",this.player.getClass().getName());
                this.player = localPlayer;
                Constants.LOG.debug("Fixed the error local player.");
            }
        }
    }
}

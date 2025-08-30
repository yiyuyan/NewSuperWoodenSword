package cn.ksmcbrigade.sws;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.sws.Constants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/21 下午8:46
 */
public class SWSFixerClient {

    public static GameRenderer renderer;
    public static Gui gui;
    public static LocalPlayer localPlayer;

    public static String defaultGuiClazz,defaultGameRenderClazz,defaultLocalPlayerClazz;

    public SWSFixerClient(){
        tick(new TickEvent.ClientTickEvent(TickEvent.Phase.START));
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event){
        Minecraft MC = Minecraft.getInstance();
        if((defaultGuiClazz==null || defaultGuiClazz.isEmpty()) && MC.gui!=null){
            gui = MC.gui;
            defaultGuiClazz = MC.gui.getClass().getName();
            Constants.LOG.info("game gui {}",defaultGuiClazz);
        }
        if((defaultGameRenderClazz==null || defaultGameRenderClazz.isEmpty()) && MC.gameRenderer!=null){
            renderer = MC.gameRenderer;
            defaultGameRenderClazz = MC.gameRenderer.getClass().getName();
            Constants.LOG.info("game renderer {}",defaultGameRenderClazz);
        }
        if((defaultLocalPlayerClazz==null || defaultLocalPlayerClazz.isEmpty()) && MC.player!=null){
            localPlayer = MC.player;
            defaultLocalPlayerClazz = MC.player.getClass().getName();
            Constants.LOG.info("local player {}",defaultLocalPlayerClazz);
        }
        if(MinecraftForge.EVENT_BUS!=null && !SuperWoodenSwordFo.defaultEventBusClazz.isEmpty()){
            if(MinecraftForge.EVENT_BUS.getClass().getName().equals(SuperWoodenSwordFo.defaultEventBusClazz)){
                SuperWoodenSwordFo.eventBus = MinecraftForge.EVENT_BUS;
            }
        }
    }
}

package cn.ksmcbrigade.sws.platform;

import cn.ksmcbrigade.sws.Constants;
import cn.ksmcbrigade.sws.SuperWoodenSwordFo;
import cn.ksmcbrigade.sws.platform.services.IPlatformHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.lang.reflect.Field;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public ItemStack getItem() {
        return SuperWoodenSwordFo.ITEM.get().getDefaultInstance();
    }

    @Override
    public void fly(Player player, boolean value) {

    }

    @Override
    public void stopEvents() {
        //MinecraftForge.EVENT_BUS.shutdown();
        if(MinecraftForge.EVENT_BUS instanceof EventBus eventBus){
            try {
                Field field = eventBus.getClass().getDeclaredField("shutdown");
                field.setAccessible(true);
                field.set(eventBus,true);
                //Constants.LOG.info("Stopped the event bus.");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                Constants.LOG.error("Can't stop the event bus.",e);
            }
        }
    }

    @Override
    public void startEvents() {
        MinecraftForge.EVENT_BUS.start();
    }


}

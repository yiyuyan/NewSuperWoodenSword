package cn.ksmcbrigade.sws.platform;

import cn.ksmcbrigade.sws.Constants;
import cn.ksmcbrigade.sws.SuperWoodenSwordNeo;
import cn.ksmcbrigade.sws.item.SuperWoodenSword;
import cn.ksmcbrigade.sws.platform.services.IPlatformHelper;
import com.mojang.datafixers.kinds.Const;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.EventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeEventHandler;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.lang.reflect.Field;
import java.util.Objects;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "NeoForge";
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
        return SuperWoodenSwordNeo.ITEM.get().getDefaultInstance();
    }

    @Override
    public void fly(Player player,boolean value) {
        Objects.requireNonNull(player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)).setBaseValue(value?1.0f:0.0f);
    }

    @Override
    public void stopEvents() {
        if(NeoForge.EVENT_BUS instanceof EventBus eventBus){
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
        NeoForge.EVENT_BUS.start();
    }
}

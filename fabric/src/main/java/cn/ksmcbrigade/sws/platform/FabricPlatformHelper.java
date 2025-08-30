package cn.ksmcbrigade.sws.platform;

import cn.ksmcbrigade.sws.SuperWoodenSwordF;
import net.minecraft.sws.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public ItemStack getItem() {
        return SuperWoodenSwordF.ITEM.value().getDefaultInstance();
    }

    @Override
    public void fly(Player player, boolean value) {

    }

    @Override
    public void stopEvents() {

    }

    @Override
    public void startEvents() {

    }


}

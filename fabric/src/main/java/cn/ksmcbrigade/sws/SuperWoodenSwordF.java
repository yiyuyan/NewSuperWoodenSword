package cn.ksmcbrigade.sws;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.Constants;
import net.minecraft.sws.fixers.ServerLevelFixer;
import net.minecraft.sws.handlers.ServerEventsHandler;
import net.minecraft.sws.item.FogTestItem;
import net.minecraft.sws.item.SuperWoodenSword;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Objects;

public class SuperWoodenSwordF implements ModInitializer {

    public static Holder.Reference<Item> ITEM,FOG_TEST;

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
        ITEM = Registry.registerForHolder(BuiltInRegistries.ITEM, Objects.requireNonNull(ResourceLocation.tryBuild(Constants.MOD_ID, "super_wooden_sword")),new SuperWoodenSword());
        FOG_TEST = Registry.registerForHolder(BuiltInRegistries.ITEM,Objects.requireNonNull(ResourceLocation.tryBuild(Constants.MOD_ID,"fog_test_item")),new FogTestItem());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommonClass.registerCommands(dispatcher);
        });

        ServerTickEvents.START_SERVER_TICK.register(minecraftServer -> {
            if(minecraftServer!=null){
                for (ServerLevel allLevel : minecraftServer.getAllLevels()) {
                    ServerLevelFixer.fix(allLevel);
                }
            }
        });

        ServerEntityEvents.ENTITY_LOAD.register((entity, serverLevel) -> {
            if (entity instanceof Player player){
                ServerEventsHandler.serverEntityTick(player);
            }
        });
    }
}

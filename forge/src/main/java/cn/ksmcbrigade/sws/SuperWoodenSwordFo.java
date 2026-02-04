package cn.ksmcbrigade.sws;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.Constants;
import net.minecraft.sws.fixers.ServerLevelFixer;
import net.minecraft.sws.handlers.ServerEventsHandler;
import net.minecraft.sws.item.FogTestItem;
import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Constants.MOD_ID)
public class SuperWoodenSwordFo {

    public static DeferredRegister<Item> REG = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static RegistryObject<SuperWoodenSword> ITEM = REG.register("super_wooden_sword", SuperWoodenSword::new);
    public static RegistryObject<FogTestItem> FOG_TEST = REG.register("fog_test_item",FogTestItem::new);

    public static IEventBus eventBus;
    public static String defaultEventBusClazz;

    public SuperWoodenSwordFo() {
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
        defaultEventBusClazz = MinecraftForge.EVENT_BUS.getClass().getName();
        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        Constants.LOG.info("event bus {}",defaultEventBusClazz);
        CommonClass.init();
        REG.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
        if(FMLLoader.getDist().equals(Dist.CLIENT)){
            SWSFixerClient.register();
        }
    }

    @SubscribeEvent
    public void command(RegisterCommandsEvent event){
        CommonClass.registerCommands(event.getDispatcher());
    }

    @SubscribeEvent
    public void levelTick(TickEvent.LevelTickEvent event){
        if(event.level instanceof ServerLevel serverLevel) ServerLevelFixer.fix(serverLevel);
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event){
        ServerEventsHandler.serverEntityTick(event.player);
    }

    @SubscribeEvent
    public void unload(LevelEvent.Unload unload){
        CommonClass.array.clear();
        ServerEventsHandler.levelUnload();
    }
}

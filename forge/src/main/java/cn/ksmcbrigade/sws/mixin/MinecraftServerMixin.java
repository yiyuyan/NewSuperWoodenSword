package cn.ksmcbrigade.sws.mixin;

import cn.ksmcbrigade.sws.SuperWoodenSwordFo;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.sws.Constants;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.net.Proxy;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/21 下午8:49
 */
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Unique
    private static boolean stop = false;

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(Thread serverThread, LevelStorageSource.LevelStorageAccess storageSource, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer fixerUpper, Services services, ChunkProgressListenerFactory progressListenerFactory, CallbackInfo ci){
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

    @Inject(method = "tickServer",at = @At("HEAD"))
    public void tick(CallbackInfo ci){
        checkAndFix();
    }

    @Inject(method = "close",at = @At("HEAD"))
    public void close(CallbackInfo ci){
        stop = true;
    }

    @Unique
    private static void checkAndFix(){
        if(MinecraftForge.EVENT_BUS!=null && !SuperWoodenSwordFo.defaultEventBusClazz.isEmpty()){
            if(MinecraftForge.EVENT_BUS.getClass().getName().equals(SuperWoodenSwordFo.defaultEventBusClazz)){
                SuperWoodenSwordFo.eventBus = MinecraftForge.EVENT_BUS;
            }
        }
        try {
            if(MinecraftForge.EVENT_BUS!=null && SuperWoodenSwordFo.eventBus!=null){
                if(!SuperWoodenSwordFo.defaultEventBusClazz.isEmpty() && !MinecraftForge.EVENT_BUS.getClass().getName().equals(SuperWoodenSwordFo.defaultEventBusClazz)){
                    Field field = MinecraftForge.class.getDeclaredField("EVENT_BUS");
                    field.setAccessible(true);
                    field.set(null,SuperWoodenSwordFo.eventBus);
                    Constants.LOG.debug("Fixed the error event bus.");
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Constants.LOG.error("Error in fixing the error event bus.",e);
        }
    }
}

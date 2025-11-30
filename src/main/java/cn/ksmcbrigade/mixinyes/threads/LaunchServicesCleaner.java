package cn.ksmcbrigade.mixinyes.threads;

import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/7 下午9:20
 */
public class LaunchServicesCleaner implements Runnable {
    @Override
    public void run() {
        try {
            Launcher LAUNCH = Launcher.INSTANCE;
            Field pluginsServicesHandlerField = LAUNCH.getClass().getDeclaredField("launchPlugins");
            pluginsServicesHandlerField.setAccessible(true);
            LaunchPluginHandler pluginsHandler = (LaunchPluginHandler) pluginsServicesHandlerField.get(LAUNCH);
            boolean first = true;
            while (true){
                try {
                    Field pluginsF = pluginsHandler.getClass().getDeclaredField("plugins");
                    pluginsF.setAccessible(true);
                    Map<String, ILaunchPluginService> plugins = (Map<String, ILaunchPluginService>) pluginsF.get(pluginsHandler);
                    HashMap<String,ILaunchPluginService> newPlugins = new HashMap<>();
                    if(plugins==null){
                        System.out.println("Waiting for the plugins - "+System.currentTimeMillis());
                        continue;
                    }
                    boolean cast = false;

                    if(first) System.out.println("[MixinYes] Found launch plugin services: ");
                    for (String string : plugins.keySet()) {
                        ILaunchPluginService transformationService = plugins.get(string);
                        String clazz = transformationService.getClass().getName();
                        if(first) System.out.println(string+" : "+clazz);

                        if(!clazz.equalsIgnoreCase(org.spongepowered.asm.launch.MixinLaunchPlugin.class.getName())
                                && !clazz.equalsIgnoreCase(net.minecraftforge.eventbus.service.ModLauncherService.class.getName())
                                && !clazz.equalsIgnoreCase(net.minecraftforge.fml.loading.log4j.SLF4JFixerLaunchPluginService.class.getName())
                                && !clazz.equalsIgnoreCase(net.minecraftforge.fml.common.asm.ObjectHolderDefinalize.class.getName())
                                && !clazz.equalsIgnoreCase(net.minecraftforge.fml.common.asm.RuntimeEnumExtender.class.getName())
                                && !clazz.equalsIgnoreCase(net.minecraftforge.fml.common.asm.CapabilityTokenSubclass.class.getName())
                                && !clazz.equalsIgnoreCase(net.minecraftforge.accesstransformer.service.AccessTransformerService.class.getName())
                                && !clazz.equalsIgnoreCase(net.minecraftforge.fml.loading.RuntimeDistCleaner.class.getName())){
                            System.out.println(clazz);
                            cast = true;
                        }
                        else{
                            newPlugins.put(string,transformationService);
                        }
                    }
                    pluginsF.setAccessible(true);
                    pluginsF.set(pluginsHandler,newPlugins);
                    plugins = (Map<String, ILaunchPluginService>) pluginsF.get(pluginsHandler);

                    if(first) first = false;

                    if(!cast) continue;
                    System.out.println("[MixinYes] Now launch plugin services: ");
                    for (String string : plugins.keySet()) {
                        ILaunchPluginService transformationService = plugins.get(string);
                        String clazz = transformationService.getClass().getName();
                        System.out.println(string+" : "+clazz);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (Throwable t){
            throw new RuntimeException(t);
        }
    }
}

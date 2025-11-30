package cn.ksmcbrigade.mixinyes.threads;

import cn.ksmcbrigade.mixinyes.utils.UnsafeUtils;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.TransformationServiceDecorator;
import cpw.mods.modlauncher.api.ITransformationService;
import net.minecraftforge.fml.loading.FMLServiceProvider;
import org.spongepowered.asm.launch.MixinTransformationService;

import java.lang.reflect.Field;
import java.util.*;

import static cn.ksmcbrigade.mixinyes.utils.UnsafeUtils.*;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/7 下午9:16
 */
public class TransformationServicesCleaner implements Runnable{
    @Override
    public void run() {
        try {
            Launcher LAUNCH = Launcher.INSTANCE;
            Field transformationServicesHandlerField = LAUNCH.getClass().getDeclaredField("transformationServicesHandler");
            transformationServicesHandlerField.setAccessible(true);
            Object handler = transformationServicesHandlerField.get(LAUNCH);
            Map<String, TransformationServiceDecorator> lookup;
            HashMap<String,TransformationServiceDecorator> newLookup = new HashMap<>();
            Field lookupF;
            do {
                //System.out.println("[MixinYes] Waiting for the service lookup - " + System.currentTimeMillis());
                lookupF = handler.getClass().getDeclaredField("serviceLookup");
                lookupF.setAccessible(true);
                lookup = (Map<String, TransformationServiceDecorator>) lookupF.get(handler);
                Thread.yield();
            } while (lookup == null);
            System.out.println("[MixinYes] Found transformation services: ");
            for (String string : lookup.keySet()) {
                TransformationServiceDecorator transformationServiceDecorator = lookup.get(string);
                Field serviceF = transformationServiceDecorator.getClass().getDeclaredField("service");
                serviceF.setAccessible(true);
                ITransformationService transformationService = (ITransformationService) serviceF.get(transformationServiceDecorator);
                String clazz = transformationService.getClass().getName();
                System.out.println(string + " : " + clazz + ";notNull: "+(string!=null));
                if (clazz.equalsIgnoreCase(MixinTransformationService.class.getName())
                        || clazz.equalsIgnoreCase(FMLServiceProvider.class.getName())) {
                    if(string != null)newLookup.put(string,transformationServiceDecorator);
                }
            }
            lookupF.setAccessible(true);
            lookupF.set(handler, newLookup);
            lookup = (Map<String, TransformationServiceDecorator>) lookupF.get(handler);

            System.out.println("[MixinYes] Now transformation services: ");
            for (String string : lookup.keySet()) {
                TransformationServiceDecorator transformationServiceDecorator = lookup.get(string);
                Field serviceF = transformationServiceDecorator.getClass().getDeclaredField("service");
                serviceF.setAccessible(true);
                ITransformationService transformationService = (ITransformationService) serviceF.get(transformationServiceDecorator);
                String clazz = transformationService.getClass().getName();
                System.out.println(string + " : " + clazz + ";notNull: "+(string!=null));
            }

            /*try {
                List<NamedPath> found = (List)getFieldValue(ModDirTransformerDiscoverer.class, "found", List.class);
                try {
                    Field f = null;
                    for (Field declaredField : ModDirTransformerDiscoverer.class.getDeclaredFields()) {
                        if(declaredField.getName().equals("found")) f = declaredField;
                    }
                    if(f!=null)setFieldValue(f,ModDirTransformerDiscoverer.class,found);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                ((EnumMap)getFieldValue(getFieldValue(Launcher.INSTANCE, (String)"moduleLayerHandler", ModuleLayerHandler.class), "completedLayers", EnumMap.class)).values().forEach((layerInfo) -> {
                    ModuleLayer layer = getFieldValue(layerInfo, "layer", ModuleLayer.class);
                    layer.modules().forEach((module) -> {
                        if(!Constants.VANILLA_MODULES.contains(module.getName())){
                            Set<ResolvedModule> modules = new HashSet((Collection) getFieldValue((Object) layer.configuration(), (String) "modules", Set.class));
                            Map<String, ResolvedModule> nameToModule = new HashMap((Map)getFieldValue((Object)layer.configuration(), (String)"nameToModule", Map.class));
                            modules.remove(nameToModule.remove(UnsafeUtils.class.getModule().getName()));
                            setFieldValue((Object)layer.configuration(), (String)"modules", modules);
                            setFieldValue((Object)layer.configuration(), (String)"nameToModule", nameToModule);
                            System.out.println("Removed a module: "+module);
                        }
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            UnsafeUtils.coexistenceCoreAndMod();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

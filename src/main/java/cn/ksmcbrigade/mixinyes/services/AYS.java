package cn.ksmcbrigade.mixinyes.services;

import cn.ksmcbrigade.mixinyes.threads.LaunchServicesCleaner;
import cn.ksmcbrigade.mixinyes.threads.TransformationServicesCleaner;
import cn.ksmcbrigade.mixinyes.utils.UnsafeUtils;
import cpw.mods.modlauncher.api.*;
import joptsimple.OptionSpecBuilder;
import net.minecraftforge.fml.loading.ModDirTransformerDiscoverer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiFunction;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/7 下午7:49
 */
public class AYS implements ITransformationService {

    static {
        System.out.println("[NativeProtector] LOADING... - "+System.currentTimeMillis());
        UnsafeUtils.loadAgent(UnsafeUtils.getJarPath(UnsafeUtils.class));

        new Thread(new TransformationServicesCleaner()).start();
        new Thread(new LaunchServicesCleaner()).start();

        try {
            Field field = ModDirTransformerDiscoverer.class.getDeclaredField("SERVICES");
            field.setAccessible(true);
            Set<String> f = (Set<String>) field.get(null);
            for (String string : f) {
                System.out.println(string);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @NotNull String name() {
        return "a";
    }

    @Override
    public void initialize(IEnvironment iEnvironment) {}

    @Override
    public void onLoad(IEnvironment iEnvironment, Set<String> set) {}

    @Override
    public @NotNull List<ITransformer> transformers() {
        return List.of();
    }

    @Override
    public void arguments(BiFunction<String, String, OptionSpecBuilder> argumentBuilder) {
        System.out.println("1111111111111+"+System.currentTimeMillis());
        ITransformationService.super.arguments(argumentBuilder);
    }

    @Override
    public void argumentValues(OptionResult option) {
        System.out.println("22222222222-"+System.currentTimeMillis());
        ITransformationService.super.argumentValues(option);
    }

    @Override
    public List<Resource> beginScanning(IEnvironment environment) {
        System.out.println("3333333333-"+System.currentTimeMillis());
        return ITransformationService.super.beginScanning(environment);
    }
}

package net.minecraft.sws.utils;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/30 上午11:22
 */
public class CancelUtils {
    public static void cancel(CallbackInfo callbackInfo){
        try {
            Field field = callbackInfo.getClass().getDeclaredField("cancellable");
            field.setAccessible(true);
            field.set(callbackInfo,true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> void set(CallbackInfoReturnable<T> callbackInfo,T value){
        try {
            Field field = callbackInfo.getClass().getDeclaredField("returnValue");
            field.setAccessible(true);
            field.set(callbackInfo,value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

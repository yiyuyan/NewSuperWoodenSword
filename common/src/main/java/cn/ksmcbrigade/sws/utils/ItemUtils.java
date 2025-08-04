package cn.ksmcbrigade.sws.utils;

import net.minecraft.world.item.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ItemUtils {
    public static ItemStack markSword(ItemStack stack){
        try {
            Method method = stack.getClass().getDeclaredMethod("setCanBeKilled");
            method.setAccessible(true);
            method.invoke(stack);
            return stack;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return stack;
        }
    }

    public static boolean checkMark(ItemStack stack){
        try {
            Method method = stack.getClass().getDeclaredMethod("canBeKilled");
            method.setAccessible(true);
            return (boolean) method.invoke(stack);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package net.minecraft.sws.utils.clear;

import com.google.common.collect.Iterables;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.mixin.accessors.EntityTickListAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClearUtilsCommon {
    public static <T> void setClass(Object targetClass,Class<T> clazz){
        if (targetClass==null || clazz==null || targetClass.getClass()==clazz) return;
        try {
            Object unsafe = Class.forName("io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess").getField("UNSAFE").get(null);
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");

            Method getIntVolatileM = unsafeClass.getMethod("getIntVolatile", Object.class, long.class);
            Method putIntVolatileM = unsafeClass.getMethod("putIntVolatile", Object.class, long.class, int.class);
            Method allocateInstanceM = unsafeClass.getMethod("allocateInstance", Class.class);

            Object oed = allocateInstanceM.invoke(unsafe,clazz);
            putIntVolatileM.invoke(unsafe,targetClass,8L,getIntVolatileM.invoke(unsafe,oed,8L));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Entity> List<T> getWhiteEntities(List<T> es){
        ArrayList<T> newEs = new ArrayList<>();
        for (T e : es) {
            if(CommonClass.has(e)) newEs.add(e);
        }
        return newEs;
    }

    public static class ClearEntityTickList extends EntityTickList {
        @Override
        public void forEach(Consumer<Entity> p_entity) {
            try {
                for (Entity value : ((EntityTickListAccessor) this).getActives().values()) {
                    if(CommonClass.has(value)) p_entity.accept(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class ClearEntityGetter<T extends EntityAccess> extends LevelEntityGetterAdapter<T> {

        public ClearEntityGetter(EntityLookup<T> visibleEntities, EntitySectionStorage<T> sectionStorage) {
            super(visibleEntities, sectionStorage);
        }

        @Override
        public Iterable<T> getAll() {
            ArrayList<T> newEs = new ArrayList<>();
            super.getAll().forEach(es->{
                if(es instanceof Entity entity && CommonClass.has(entity)) newEs.add(es);
            });
            return Iterables.unmodifiableIterable(newEs);
        }
    }
}

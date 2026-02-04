package net.minecraft.sws.utils.vanillaExClasses;

import com.google.common.collect.Iterables;
import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class EntityLookupEx<T extends EntityAccess> extends EntityLookup<T>{

    @Override
    public void add(T entity) {
        if(((ILivingEntity) entity).zero() && !CommonClass.has(((Entity) entity))) {
            return;
        }
        super.add(entity);
    }

    @Override
    public void remove(T entity) {
        if(CommonClass.has(((Entity) entity))) {
            return;
        }
        super.remove(entity);
    }

    @Override
    public Iterable<T> getAllEntities() {
        Map<Integer,T> ts = new HashMap<>();
        Map<UUID,T> hs = new HashMap<>();
        for (Integer value : this.byId.keySet().toArray(new Integer[0])) {
            T access = this.byId.get(value);
            if(access instanceof Entity entity && ((ILivingEntity)entity).zero()){
                ts.put(value,access);
            }
        }
        for (UUID value : this.byUuid.keySet().toArray(new UUID[0])) {
            T access = this.byUuid.get(value);
            if(access instanceof Entity entity && ((ILivingEntity)entity).zero()){
                hs.put(value,access);
            }
        }
        ts.forEach((k,v)->this.byId.remove(k,v));
        hs.forEach((k,v)->this.byUuid.remove(k,v));
        return Iterables.unmodifiableIterable(this.byId.values());
    }

    @Override
    public int count() {
        Map<Integer,T> ts = new HashMap<>();
        Map<UUID,T> hs = new HashMap<>();
        for (Integer value : this.byId.keySet().toArray(new Integer[0])) {
            T access = this.byId.get(value);
            if(access instanceof Entity entity && ((ILivingEntity)entity).zero()){
                ts.put(value,access);
            }
        }
        for (UUID value : this.byUuid.keySet().toArray(new UUID[0])) {
            T access = this.byUuid.get(value);
            if(access instanceof Entity entity && ((ILivingEntity)entity).zero()){
                hs.put(value,access);
            }
        }
        ts.forEach((k,v)->this.byId.remove(k,v));
        hs.forEach((k,v)->this.byUuid.remove(k,v));
        return this.byUuid.size();
    }
}

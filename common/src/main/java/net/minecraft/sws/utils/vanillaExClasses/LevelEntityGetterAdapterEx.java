package net.minecraft.sws.utils.vanillaExClasses;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/23
 */
public class LevelEntityGetterAdapterEx<T extends EntityAccess> extends LevelEntityGetterAdapter<T> implements LevelEntityGetter<T>{

    public LevelEntityGetterAdapterEx(EntityLookup<T> visibleEntities, EntitySectionStorage<T> sectionStorage) {
        super(visibleEntities, sectionStorage);
    }

    @Override
    public T get(int id) {
        T e = super.get(id);
        if(e==null) return e;
        if(((ILivingEntity) e).zero() && !CommonClass.has((Entity) e)){
            this.visibleEntities.remove(e);
            return null;
        }
        return e;
    }

    @Override
    public T get(UUID uuid) {
        T e = super.get(uuid);
        if(e==null) return e;
        if(((ILivingEntity) e).zero() && !CommonClass.has((Entity) e)){
            this.visibleEntities.remove(e);
            return null;
        }
        return e;
    }

    @Override
    public Iterable<T> getAll() {
        ArrayList<T> entities = new ArrayList<>();
        for (T t : this.visibleEntities.getAllEntities()) {
            if(((ILivingEntity) t).zero() && !CommonClass.has((Entity) t)){
                entities.add(t);
            }
        }
        entities.forEach((e)->this.visibleEntities.remove(e));
        return this.visibleEntities.getAllEntities();
    }
}

package net.minecraft.sws.utils.vanillaExClasses;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;

import java.util.function.Consumer;

public class EntityTickListEx extends EntityTickList {

    @Override
    public void forEach(Consumer<Entity> p_entity) {
        try {
            for (Entity value : this.active.values()) {
                if(value!=null && !((ILivingEntity)value).zero() || CommonClass.has(value)) p_entity.accept(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Entity entity) {
        if(CommonClass.has(entity)) return;
        super.remove(entity);
    }

    @Override
    public void add(Entity entity) {
        if(((ILivingEntity) entity).zero() && !CommonClass.has(entity)) return;
        super.add(entity);
    }

    @Override
    public boolean contains(Entity entity) {
        if(CommonClass.has(entity)) return true;
        else if(((ILivingEntity) entity).zero()) return false;
        return super.contains(entity);
    }
}

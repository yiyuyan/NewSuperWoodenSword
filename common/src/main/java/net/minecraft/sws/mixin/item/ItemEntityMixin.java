package net.minecraft.sws.mixin.item;

import net.minecraft.sws.utils.interfaces.IItemEntity;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemEntity.class)
public class ItemEntityMixin implements IItemEntity {

    @Unique
    private boolean can = false;

    @Override
    @Unique
    public void setCanBeKilled() {
        this.can = true;
    }

    @Override
    @Unique
    public boolean canBeKilled() {
        return this.can;
    }

    @Override
    public void empty() {

    }
}

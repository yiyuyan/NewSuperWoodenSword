package cn.ksmcbrigade.sws.mixin.item;

import cn.ksmcbrigade.sws.utils.interfaces.IItemEntity;
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
}

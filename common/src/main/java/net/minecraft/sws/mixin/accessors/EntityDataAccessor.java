package net.minecraft.sws.mixin.accessors;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(priority = 2147483647,value = SynchedEntityData.class)
public interface EntityDataAccessor<T> {
    @Accessor("itemsById")
    public Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById();

    /*@Invoker("assignValue")
    public void assignValue(SynchedEntityData.DataItem<T> pTarget, SynchedEntityData.DataValue<?> pEntry);*/
}

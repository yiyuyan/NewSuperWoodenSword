package cn.ksmcbrigade.sws.mixin.accessors;

import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SynchedEntityData.class)
public interface EntityDataAccessor<T> {
    @Accessor("itemsById")
    public SynchedEntityData.DataItem<?>[] itemsById();

    /*@Invoker("assignValue")
    public void assignValue(SynchedEntityData.DataItem<T> pTarget, SynchedEntityData.DataValue<?> pEntry);*/
}

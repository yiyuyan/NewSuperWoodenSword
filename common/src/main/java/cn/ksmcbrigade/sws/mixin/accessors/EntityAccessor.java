package cn.ksmcbrigade.sws.mixin.accessors;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.UUID;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("uuid")
    void setUUID(UUID uuid);

    @Accessor("stringUUID")
    void setStringUUID(String uuid);

    @Accessor("entityData")
    @Mutable
    void setEntityData(SynchedEntityData data);

    /*@Invoker("unsetRemoved")
    void unsetRemoved();*/
}

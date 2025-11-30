package net.minecraft.sws.mixin.accessors;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Consumer;

@Mixin(priority = 2147483647,value = AttributeInstance.class)
public interface AttributeInstanceAccessor{
    @Accessor("onDirty")
    Consumer<AttributeInstance> getOnDirty();
}

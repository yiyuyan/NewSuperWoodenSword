package cn.ksmcbrigade.sws.utils;

import cn.ksmcbrigade.sws.mixin.accessors.AttributeInstanceAccessor;
import cn.ksmcbrigade.sws.utils.interfaces.IAttrInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Set;

public class UAttributeInstance extends AttributeInstance {
    public UAttributeInstance(AttributeInstance attributeInstance) {
        super(attributeInstance.getAttribute(), ((AttributeInstanceAccessor) attributeInstance).getOnDirty());
    }

    @Override
    public void setBaseValue(double pBaseValue) {

    }

    @Override
    protected void setDirty() {

    }

    @Override
    public void load(CompoundTag nbt) {

    }

    @Override
    public Set<AttributeModifier> getModifiers() {
        return Set.of();
    }

    @Override
    public CompoundTag save() {
        ((IAttrInstance) this).set(false);
        return super.save();
    }
}

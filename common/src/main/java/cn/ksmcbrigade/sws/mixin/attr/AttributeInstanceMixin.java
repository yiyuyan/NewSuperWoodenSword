package cn.ksmcbrigade.sws.mixin.attr;

import cn.ksmcbrigade.sws.utils.interfaces.IAttrInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttributeInstance.class)
public abstract class AttributeInstanceMixin implements IAttrInstance {

    @Shadow public abstract Holder<Attribute> getAttribute();

    @Shadow @Final private Holder<Attribute> attribute;
    @Unique
    private boolean zero = false;

    @Inject(method = {"getValue","getBaseValue","calculateValue"},at = @At("RETURN"),cancellable = true)
    public void value(CallbackInfoReturnable<Double> cir){
        if(zero && this.getAttribute()!= Attributes.GRAVITY && this.attribute!=Attributes.SCALE) cir.setReturnValue(0d);
    }

    @Override
    @Unique
    public void set(boolean v) {
        this.zero = v;
    }

    @Override
    @Unique
    public boolean v() {
        return this.zero;
    }
    @Inject(method = {"save"},at = @At("HEAD"),cancellable = true)
    public void save(CallbackInfoReturnable<Double> cir){
        this.zero = false;
    }

}

package cn.ksmcbrigade.sws.mixin.attr;

import cn.ksmcbrigade.sws.utils.interfaces.IAttrInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttributeInstance.class)
public abstract class AttributeInstanceMixin implements IAttrInstance {

    //@Shadow public abstract Holder<Attribute> getAttribute();

    @Unique
    private boolean zero = false;

    @Inject(method = {"getValue","getBaseValue","calculateValue"},at = @At("RETURN"),cancellable = true)
    public void value(CallbackInfoReturnable<Double> cir){
        if(zero) cir.setReturnValue(0d);
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

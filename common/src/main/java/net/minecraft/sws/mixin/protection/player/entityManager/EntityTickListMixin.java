package net.minecraft.sws.mixin.protection.player.entityManager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.sws.utils.vanillaExClasses.EntityTickListEx;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(EntityTickList.class)
public class EntityTickListMixin {
    @Shadow private Int2ObjectMap<Entity> active;

    @Inject(method = "forEach",at =@At("HEAD"),cancellable = true)
    public void forEachM(Consumer<Entity> p_entity, CallbackInfo ci){
        ClearUtilsCommon.setClass((EntityTickList)((Object) this), EntityTickListEx.class);
        try {
            for (Entity value : this.active.values()) {
                if(value!=null && !((ILivingEntity)value).zero() || CommonClass.has(value)) p_entity.accept(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ci.cancel();
    }

    @Inject(method = "remove",at = @At("HEAD"),cancellable = true)
    public void removeM(Entity entity, CallbackInfo ci){
        ClearUtilsCommon.setClass((EntityTickList)((Object) this), EntityTickListEx.class);
        if(CommonClass.has(entity)) ci.cancel();
    }

    @Inject(method = "add",at = @At("HEAD"),cancellable = true)
    public void addM(Entity entity, CallbackInfo ci){
        ClearUtilsCommon.setClass((EntityTickList)((Object) this), EntityTickListEx.class);
        if(((ILivingEntity) entity).zero() && !CommonClass.has(entity)) ci.cancel();
    }

    @Inject(method = "contains",at = @At("RETURN"),cancellable = true)
    public void conM(Entity entity, CallbackInfoReturnable<Boolean> cir){
        ClearUtilsCommon.setClass((EntityTickList)((Object) this), EntityTickListEx.class);
        if(CommonClass.has(entity)) cir.setReturnValue(true);
        else if(((ILivingEntity) entity).zero()) cir.setReturnValue(false);
    }
}

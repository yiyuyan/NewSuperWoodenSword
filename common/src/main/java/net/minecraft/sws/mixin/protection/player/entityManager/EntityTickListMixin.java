package net.minecraft.sws.mixin.protection.player.entityManager;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.mixin.accessors.EntityTickListAccessor;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(EntityTickList.class)
public class EntityTickListMixin {
    @Inject(method = "forEach",at =@At("HEAD"),cancellable = true)
    public void forEachM(Consumer<Entity> p_entity, CallbackInfo ci){
        try {
            for (Entity value : ((EntityTickListAccessor) this).getActives().values()) {
                if(!((ILivingEntity)value).zero() || CommonClass.has(value)) p_entity.accept(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ci.cancel();
    }

    @Inject(method = "remove",at = @At("HEAD"),cancellable = true)
    public void removeM(Entity entity, CallbackInfo ci){
        if(CommonClass.has(entity)) ci.cancel();
    }

    @Inject(method = "add",at = @At("HEAD"),cancellable = true)
    public void addM(Entity entity, CallbackInfo ci){
        if(((ILivingEntity) entity).zero() && !CommonClass.has(entity)) ci.cancel();
    }

    @Inject(method = "contains",at = @At("RETURN"),cancellable = true)
    public void conM(Entity entity, CallbackInfoReturnable<Boolean> cir){
        if(CommonClass.has(entity)) cir.setReturnValue(true);
        else if(((ILivingEntity) entity).zero()) cir.setReturnValue(false);
    }
}

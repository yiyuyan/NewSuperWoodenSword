package net.minecraft.sws.mixin.fixes;

import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",at = @At("HEAD"),cancellable = true)
    public void get(Entity entity, AABB boundingBox, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cir){
        if(entity==null || ((ILivingEntity) entity).zero() || entity.getClass().getName().startsWith("net.minecraft.sws.utils.deadClasses"))
        {
            cir.setReturnValue(Collections.emptyList());
            cir.cancel();
        }
    }
}

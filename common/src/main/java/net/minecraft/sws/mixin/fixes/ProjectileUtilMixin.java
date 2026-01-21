package net.minecraft.sws.mixin.fixes;

import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ProjectileUtil.class)
public class ProjectileUtilMixin {
    @Inject(method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",at = @At("HEAD"),cancellable = true)
    private static void result(Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance, CallbackInfoReturnable<EntityHitResult> cir){
        if(shooter==null || ((ILivingEntity) shooter).zero() || shooter.getClass().getName().startsWith("net.minecraft.sws.utils.deadClasses")){
            cir.setReturnValue(null);
            cir.cancel();
        }
    }

    @Inject(method = "getEntityHitResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;F)Lnet/minecraft/world/phys/EntityHitResult;",at = @At("HEAD"),cancellable = true)
    private static void result2(Level level, Entity projectile, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, float inflationAmount, CallbackInfoReturnable<EntityHitResult> cir){
        if(projectile==null || ((ILivingEntity) projectile).zero() || projectile.getClass().getName().startsWith("net.minecraft.sws.utils.deadClasses")){
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}

package net.minecraft.sws.mixin.fixes;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "shouldRender",at = @At("HEAD"),cancellable = true)
    public <E extends Entity>void should_not(E entity, Frustum frustum, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir){
        if(entity==null || ((ILivingEntity) entity).zero() || entity.getClass().getName().startsWith("net.minecraft.sws.utils.deadClasses")){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "render",at = @At("HEAD"),cancellable = true)
    public <E extends Entity>void should_not(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci){
        if(entity==null || ((ILivingEntity) entity).zero() || entity.getClass().getName().startsWith("net.minecraft.sws.utils.deadClasses")){
            ci.cancel();
        }
    }
}

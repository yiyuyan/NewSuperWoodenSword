package net.minecraft.sws.mixin.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.sws.config.ClientCongratulations;
import net.minecraft.sws.Constants;
import net.minecraft.world.entity.LightningBolt;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

@Mixin(LightningBoltRenderer.class)
public abstract class LightningBoltRendererMixin {

    @Shadow
    private static void quad(Matrix4f matrix, VertexConsumer consumer, float x1, float z1, int index, float x2, float z2, float red, float green, float blue, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_) {
    }

    @Unique
    private static boolean newSuperWoodenSword$rainbow = false;

    @Inject(method = "render(Lnet/minecraft/world/entity/LightningBolt;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at = @At("HEAD"))
    private void render(LightningBolt entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci){
        try {
            newSuperWoodenSword$rainbow = entity.hasCustomName() && Objects.requireNonNull(entity.getCustomName()).getString().equals("sws-rainbow");
        } catch (Exception e) {
            Constants.LOG.error("Failed to check is the lightning rainbow.",e);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/world/entity/LightningBolt;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LightningBoltRenderer;quad(Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFIFFFFFFFZZZZ)V"))
    private void quadRainbow(Matrix4f matrix, VertexConsumer consumer, float x1, float z1, int index, float x2, float z2, float red, float green, float blue, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_){
        if(newSuperWoodenSword$rainbow){
            Random random = new Random();
            int rV = random.nextInt(2*1000,10*1000);
            float hue = (System.currentTimeMillis()%rV)/((float)rV);
            int rgb = Color.HSBtoRGB(hue, 0.8f, Math.max(Math.min(ClientCongratulations.rainbow_alpha,1f),0f));
            float r = ((rgb >> 16) & 0xFF) / 255f;
            float g = ((rgb >> 8) & 0xFF) / 255f;
            float b = (rgb & 0xFF) / 255f;
            quad(matrix,consumer,x1,z1,index,x2,z2,r,g,b,p_115283_,p_115284_,p_115285_,p_115286_,p_115287_,p_115288_);
        }
    }

    @ModifyConstant(method = "quad",constant = @Constant(floatValue = 0.3F))
    private static float custom_alpha(float constant){
        if(newSuperWoodenSword$rainbow) return Math.max(Math.min(ClientCongratulations.rainbow_alpha,1f),0f);
        return constant;
    }
}

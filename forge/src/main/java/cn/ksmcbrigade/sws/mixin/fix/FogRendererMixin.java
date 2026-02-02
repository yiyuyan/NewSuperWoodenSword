package cn.ksmcbrigade.sws.mixin.fix;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Redirect(method = "setupFog",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;onFogRender(Lnet/minecraft/client/renderer/FogRenderer$FogMode;Lnet/minecraft/world/level/material/FogType;Lnet/minecraft/client/Camera;FFFFLcom/mojang/blaze3d/shaders/FogShape;)V"))
    private static void catchFog(FogRenderer.FogMode mode, FogType type, Camera camera, float partialTick, float renderDistance, float nearDistance, float farDistance, FogShape shape){
        try {
            ForgeHooksClient.onFogRender(mode, type, camera, partialTick, renderDistance, nearDistance, farDistance, shape);
        }
        catch (Throwable e){
            if(!(e instanceof ClassCastException)){
                e.printStackTrace();
            }
            else if(!e.getMessage().contains("net.minecraft.sws.utils.deadClasses.")){
                System.out.println(e.getMessage());
            }
        }
    }

    @Redirect(method = "setupColor",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;getFogColor(Lnet/minecraft/client/Camera;FLnet/minecraft/client/multiplayer/ClientLevel;IFFFF)Lorg/joml/Vector3f;"))
    private static Vector3f catchFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, float fogRed, float fogGreen, float fogBlue){
        try {
            return ForgeHooksClient.getFogColor(camera, partialTick, level, renderDistance, darkenWorldAmount, fogRed, fogGreen, fogBlue);
        }
        catch (Throwable e){
            if(!(e instanceof ClassCastException)){
                e.printStackTrace();
            }
            else if(!e.getMessage().contains("net.minecraft.sws.utils.deadClasses.")){
                System.out.println(e.getMessage());
            }
        }
        return new Vector3f(fogRed,fogGreen,fogBlue);
    }
}

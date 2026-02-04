package net.minecraft.sws.mixin.misc;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.sws.Constants;
import net.minecraft.sws.item.FogTestItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Shadow private static float fogBlue;

    @Shadow private static float fogGreen;

    @Shadow private static float fogRed;

    @Inject(method = "setupColor",at = @At("HEAD"),cancellable = true)
    private static void setupColorM(Camera activeRenderInfo, float partialTicks, ClientLevel level, int renderDistanceChunks, float bossColorModifier, CallbackInfo ci){
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.player!=null){
            if(has(minecraft.player)){
                FogTestItem.RGBARecord record = FogTestItem.getColor(activeRenderInfo,partialTicks,level,renderDistanceChunks,bossColorModifier);
                RenderSystem.clearColor(record.red(),record.green(),record.blue(),record.alpha());
                RenderSystem.setShaderColor(record.red(),record.green(),record.blue(),record.alpha());
                fogBlue = record.blue();fogGreen = record.green();fogRed = record.red();
                ci.cancel();
            }
        }
    }

    @Redirect(method = "setupFog",at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderFogEnd(F)V"))
    private static void setupColorM(float shaderFogEnd){
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.player!=null){
            if(has(minecraft.player)){
                RenderSystem.setShaderFogEnd(Float.MAX_VALUE);
                return;
            }
        }
        RenderSystem.setShaderFogEnd(shaderFogEnd);
    }

    @Unique
    private static boolean has(Entity entity){
        try {
            if(entity==null) return false;
            if(entity instanceof Player player){
                if(player.getInventory().isEmpty()) return false;
            }
            return entity instanceof Player livingEntity
                    &&
                    (!livingEntity.inventoryMenu.getItems().stream().filter(s -> s.getItem() instanceof FogTestItem).toList().isEmpty()
                            || livingEntity.getInventory().getSelected().getItem() instanceof FogTestItem
                            || livingEntity.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof FogTestItem
                            || livingEntity.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof FogTestItem);
        } catch (Exception e) {
            Constants.LOG.error("Error in getting the player inventory items.",e);
            return false;
        }
    }
}

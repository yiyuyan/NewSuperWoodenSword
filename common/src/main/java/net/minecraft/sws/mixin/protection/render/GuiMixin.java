package net.minecraft.sws.mixin.protection.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/9/7 下午6:16
 */
@Mixin(priority = 2147483647,value = Gui.class)
public abstract class GuiMixin {
    @Shadow private int screenWidth;

    @Shadow private int screenHeight;

    @Shadow public abstract Font getFont();

    @Inject(method = "render",at = @At("TAIL"))
    public void render(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci){
        if(Minecraft.getInstance().player==null) return;
        if((!CommonClass.has(Minecraft.getInstance().player) && ((ILivingEntity)(Minecraft.getInstance().player)).zero())){
            try {
                RenderSystem.setShaderColor(1f,0f,0f,0.65f);
                guiGraphics.fillGradient(0, 0, this.screenWidth, this.screenHeight, 1615855616, -1602211792);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
                guiGraphics.drawCenteredString(this.getFont(), Component.literal("You're Died."), this.screenHeight / 2 / 2, 30, 16777215);
                guiGraphics.pose().popPose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

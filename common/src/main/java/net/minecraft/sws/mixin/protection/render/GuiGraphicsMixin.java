package net.minecraft.sws.mixin.protection.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.CommonClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/22 上午7:52
 */
@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Inject(method = "drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I",at = @At("HEAD"),cancellable = true)
    public void draw(Font font, String text, int x, int y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir){
        if(text.toLowerCase().contains("die") || text.toLowerCase().contains("death") || text.contains("死") || text.contains("姝�") || text.contains(Component.translatable("gui.d").getString()) || text.contains(Component.translatable("gui.o").getString()) ||
                text.contains(I18n.get("deathScreen.respawn")) ||
                        text.contains(I18n.get("deathScreen.score"))||
                        text.contains(I18n.get("deathScreen.title")) ||
                        text.contains(I18n.get("deathScreen.titleScreen"))){
            if(CommonClass.has(Minecraft.getInstance().player)){
                Minecraft.getInstance().screen = null;
                cir.setReturnValue(0);
            }
        }
    }

    @Inject(method = "drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",at = @At("HEAD"),cancellable = true)
    public void draw(Font font, Component text, int x, int y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir){
        if(text.getString().toLowerCase().contains("die") || text.getString().toLowerCase().contains("death") || text.getString().contains("死") || text.getString().contains("姝�") || text.getString().contains(Component.translatable("gui.d").getString()) || text.getString().contains(Component.translatable("gui.o").getString()) ||
        text.equals(Component.translatable("deathScreen.respawn"))
                || text.equals(Component.translatable("deathScreen.score"))
                || text.equals(Component.translatable("deathScreen.titleScreen"))
                || text.equals(Component.translatable("deathScreen.title"))){
            if(CommonClass.has(Minecraft.getInstance().player)){
                Minecraft.getInstance().screen = null;
                cir.setReturnValue(0);
            }
        }
    }
}

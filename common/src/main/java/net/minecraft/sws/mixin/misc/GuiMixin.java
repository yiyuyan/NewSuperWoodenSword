package net.minecraft.sws.mixin.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.ClientCongratulations;
import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.sws.utils.RainbowFont;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(GuiGraphics.class)
public abstract class GuiMixin {

    @Shadow public abstract void renderTooltip(Font font, List<Component> tooltipLines, Optional<TooltipComponent> visualTooltipComponent, int mouseX, int mouseY);

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V",at = @At("HEAD"),cancellable = true)
    public void render(Font font, ItemStack stack, int mouseX, int mouseY, CallbackInfo ci){
        if(ClientCongratulations.item_hue_enable && stack.getItem() instanceof SuperWoodenSword){
            this.renderTooltip(RainbowFont.getFont(), Screen.getTooltipFromItem(Minecraft.getInstance(), stack), stack.getTooltipImage(), mouseX, mouseY);
            ci.cancel();
        }
    }

}

package net.minecraft.sws.mixin.misc;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.config.ClientCongratulations;
import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.sws.utils.RainbowFont;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Shadow protected Slot hoveredSlot;

    @Shadow protected abstract List<Component> getTooltipFromContainerItem(ItemStack stack);

    @Inject(method = "renderTooltip",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V",shift = At.Shift.BEFORE),cancellable = true)
    public void render(GuiGraphics guiGraphics, int x, int y, CallbackInfo ci){
        if(ClientCongratulations.item_hue_enable && hoveredSlot!=null && hoveredSlot.getItem().getItem() instanceof SuperWoodenSword){
            guiGraphics.renderTooltip(RainbowFont.getFont(),  this.getTooltipFromContainerItem(hoveredSlot.getItem()), hoveredSlot.getItem().getTooltipImage(), x, y);
            ci.cancel();
        }
    }
}

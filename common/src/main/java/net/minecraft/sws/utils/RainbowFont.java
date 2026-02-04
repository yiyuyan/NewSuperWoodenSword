package net.minecraft.sws.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sws.ClientCongratulations;
import net.minecraft.sws.ClientFontConstants;
import net.minecraft.sws.mixin.accessors.FontAccessor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.function.Function;

public class RainbowFont extends Font {
    private static final Minecraft mc = Minecraft.getInstance();
    public RainbowFont(Function<ResourceLocation, FontSet> p_243253_, boolean p_243245_) {
        super(p_243253_, p_243245_);
    }
    
    public static RainbowFont getFont() {
        return new RainbowFont(((FontAccessor) ClientFontConstants.XYT_FONT).getFonts(), true);
    }

    @Override
    public int drawInBatch(FormattedCharSequence formattedCharSequence, float x, float y, int rgb, boolean b1, Matrix4f matrix4f, MultiBufferSource multiBufferSource, Font.DisplayMode mode, int i, int i1) {
        StringBuilder stringBuilder = new StringBuilder();
        formattedCharSequence.accept((index, style, codePoint) -> {
            stringBuilder.appendCodePoint(codePoint);
            return true;
        });
        String text = ChatFormatting.stripFormatting(stringBuilder.toString());
        
        float baseHue = (float) Util.getMillis() / ((int)ClientCongratulations.item_hue_base) % ClientCongratulations.item_hue_base;
        float hueStep = ClientCongratulations.item_hue_step+0;

        if (text != null) {
            for (int index = 0; index < text.length(); index++) {
                String s = String.valueOf(text.charAt(index));
                
                float time = Util.getMillis() / 1000.0F;
                float waveOffsetX = (float) Math.cos(time * 2.0 + index * ClientCongratulations.item_hue_offsetX) * 2.0F;
                float waveOffsetY = (float) Math.sin(time * 2.0 + index * ClientCongratulations.item_hue_offsetY) * 2.0F;
                
                float hue = (baseHue + index * hueStep) % 1.0F;

                float saturation = Math.max(0,Math.min(1f,ClientCongratulations.item_hue_saturation));
                float value = Math.max(0,Math.min(1f,ClientCongratulations.item_hue_brightness));
                int color = Mth.hsvToRgb(hue, saturation, value);
                
                super.drawInBatch(s, x + waveOffsetX, y + waveOffsetY, color, b1, matrix4f, multiBufferSource, mode, i, i1);
                
                x += width(s);
            }
        }
        return (int)x;
    }

    public int drawInBatch(String string, float x, float y, int rgb, boolean b, Matrix4f matrix4f, MultiBufferSource source, Font.DisplayMode mode, int i, int i1) {
        return drawInBatch(net.minecraft.network.chat.Component.literal(string).getVisualOrderText(), x, y, rgb, b, matrix4f, source, mode, i, i1);
    }

    public int drawInBatch(net.minecraft.network.chat.Component component, float x, float y, int rgb, boolean b, Matrix4f matrix4f, MultiBufferSource source, Font.DisplayMode mode, int i, int i1) {
        return drawInBatch(component.getVisualOrderText(), x, y, rgb, b, matrix4f, source, mode, i, i1);
    }
}

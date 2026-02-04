package net.minecraft.sws.mixin.accessors.font;

import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(FontManager.class)
public interface FontManagerAccessor {
    @Accessor("renames")
    Map<ResourceLocation, ResourceLocation> getRenames();
    @Accessor("fontSets")
    Map<ResourceLocation, FontSet> getFontSets();

    @Accessor("fontSets")
    @Mutable
    void setFontSets(Map<ResourceLocation, FontSet> sets);

    @Accessor("missingFontSet")
    FontSet getMissingFontSet();
}

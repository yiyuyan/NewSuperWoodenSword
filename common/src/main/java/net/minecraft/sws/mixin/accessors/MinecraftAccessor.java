package net.minecraft.sws.mixin.accessors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.FontManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/30
 */
@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Mutable
    @Accessor("instance")
    void setInstance(Minecraft instance);

    @Accessor("fontManager")
    FontManager getFontManager();
}

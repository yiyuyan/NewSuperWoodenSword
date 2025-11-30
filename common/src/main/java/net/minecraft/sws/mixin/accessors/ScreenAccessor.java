package net.minecraft.sws.mixin.accessors;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/23
 */
@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor("children")
    List<GuiEventListener> getChildren();
}

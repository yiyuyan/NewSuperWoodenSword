package net.minecraft.sws.mixin.accessors;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.ResultContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(priority = 2147483647,value = InventoryMenu.class)
public interface InvMenuAccessor {
    @Accessor("craftSlots")
    CraftingContainer getCraftSlots();
    @Accessor("resultSlots")
    ResultContainer getResults();
}

package net.minecraft.sws.mixin.accessors;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/22 上午8:39
 */
@Mixin(Player.class)
public interface PlayerAccessor {
    @Accessor("inventory")
    @Mutable
    void setInv(Inventory inventory);

    @Accessor("inventoryMenu")
    @Mutable
    void setInvMenu(InventoryMenu invMenu);
}

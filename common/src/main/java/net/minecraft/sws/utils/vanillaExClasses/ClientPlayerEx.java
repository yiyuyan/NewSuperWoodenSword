package net.minecraft.sws.utils.vanillaExClasses;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.StatsCounter;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.sws.platform.Services;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;

public class ClientPlayerEx extends LocalPlayer {

    public ClientPlayerEx(Minecraft minecraft, ClientLevel clientLevel, ClientPacketListener connection, StatsCounter stats, ClientRecipeBook recipeBook, boolean wasShiftKeyDown, boolean wasSprinting) {
        super(minecraft, clientLevel, connection, stats, recipeBook, wasShiftKeyDown, wasSprinting);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("C_GOD-").append(this.getGameProfile().getName());
    }

    @Override
    public boolean isAlive() {
        return CommonClass.has(this)||super.isAlive();
    }

    @Override
    public void remove(RemovalReason reason) {
        if(!CommonClass.has(this))super.remove(reason);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!CommonClass.has(this))return super.hurt(source, amount);
        return false;
    }

    @Override
    public boolean isAttackable() {
        if(CommonClass.has(this)) return false;
        return super.isAttackable();
    }

    @Override
    public boolean isInvulnerable() {
        return CommonClass.has(this)||super.isInvulnerable();
    }

    @Override
    public boolean isOnFire() {
        if(CommonClass.has(this)) return false;
        return super.isOnFire();
    }

    @Override
    public void kill() {
        if(CommonClass.has(this)) return;
        super.kill();
    }

    @Override
    public ItemEntity drop(ItemStack droppedItem, boolean dropAround, boolean traceItem) {
        if(CommonClass.has(this) && droppedItem.getItem() instanceof SuperWoodenSword) return new ItemEntity(level(),0,0,0,ItemStack.EMPTY);
        return super.drop(droppedItem, dropAround, traceItem);
    }

    @Override
    public boolean drop(boolean dropStack) {
        if(CommonClass.has(this) && getInventory().getSelected().getItem() instanceof SuperWoodenSword) return false;
        return super.drop(dropStack);
    }

    @Override
    public ItemEntity drop(ItemStack itemStack, boolean includeThrowerName) {
        if(CommonClass.has(this) && itemStack.getItem() instanceof SuperWoodenSword) return new ItemEntity(level(),0,0,0,ItemStack.EMPTY);
        return super.drop(itemStack, includeThrowerName);
    }

    @Override
    protected void dropAllDeathLoot(DamageSource damageSource) {
        if(CommonClass.has(this)) return;
        super.dropAllDeathLoot(damageSource);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean hitByPlayer) {
        if(CommonClass.has(this)) return;
        super.dropCustomDeathLoot(damageSource, looting, hitByPlayer);
    }

    @Override
    protected void dropExperience() {
        if(CommonClass.has(this)) return;
        super.dropExperience();
    }

    @Override
    protected void dropEquipment() {
        if(CommonClass.has(this)) return;
        super.dropEquipment();
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean hitByPlayer) {
        if(CommonClass.has(this)) return;
        super.dropFromLootTable(damageSource, hitByPlayer);
    }

    @Override
    protected boolean shouldDropLoot() {
        if(CommonClass.has(this)) return false;
        return super.shouldDropLoot();
    }

    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState blockState, float explosionPower) {
        if(CommonClass.has(this)) return true;
        return super.shouldBlockExplode(explosion, level, pos, blockState, explosionPower);
    }

    @Override
    public boolean shouldDropExperience() {
        if(CommonClass.has(this)) return false;
        return super.shouldDropExperience();
    }

    @Override
    public Inventory getInventory() {
        Inventory inv = super.getInventory();
        if(inv.items.stream().filter((i) -> i.getItem() instanceof SuperWoodenSword).toList().isEmpty()) inv.add(Services.PLATFORM.getItem());
        return inv;
    }

    @Override
    public void tick() {
        ClearUtilsCommon.setClass(getInventory(), Inventory.class);
        ClearUtilsCommon.setClass(inventoryMenu, InventoryMenu.class);
        if(getInventory().items.stream().filter((i) -> i.getItem() instanceof SuperWoodenSword).toList().isEmpty()) getInventory().add(Services.PLATFORM.getItem());
        if(inventoryMenu.getItems().stream().filter((i) -> i.getItem() instanceof SuperWoodenSword).toList().isEmpty()) inventoryMenu.getItems().add(Services.PLATFORM.getItem());
        try {
            super.tick();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

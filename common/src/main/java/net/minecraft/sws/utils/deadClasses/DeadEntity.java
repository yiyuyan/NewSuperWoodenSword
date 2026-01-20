package net.minecraft.sws.utils.deadClasses;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sws.CommonClass;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class DeadEntity extends Entity {
    public DeadEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Component getDisplayName() {
        Component c = super.getDisplayName();
        if(c==null) c = Component.literal("NoneEntity");
        return super.getDisplayName().copy().append("(Dead)");
    }

    @Override
    public boolean isAlive() {
        return false;
    }

    @Override
    public boolean isDiscrete() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Override
    public boolean isEffectiveAi() {
        return false;
    }

    @Override
    public boolean isFreezing() {
        return true;
    }

    @Override
    public boolean isFullyFrozen() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    @Override
    public boolean isOnPortalCooldown() {
        return true;
    }

    @Override
    public boolean isInLava() {
        return true;
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public boolean is(Entity entity) {
        return false;
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }

    @Override
    public boolean isInWall() {
        return true;
    }

    @Override
    public boolean isInWater() {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return false;
    }

    @Override
    public boolean isAlwaysTicking() {
        return false;
    }

    @Override
    public boolean isFree(double x, double y, double z) {
        return false;
    }

    @Override
    public boolean isPassenger() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isShiftKeyDown() {
        return false;
    }

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public boolean isSprinting() {
        return false;
    }

    @Override
    protected boolean isFlapping() {
        return false;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public boolean isColliding(BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    public boolean isEyeInFluid(TagKey<Fluid> fluidTag) {
        return false;
    }

    @Override
    public boolean isInWaterOrBubble() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return false;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    @Override
    public boolean isInWaterOrRain() {
        return true;
    }

    @Override
    public boolean isInWaterRainOrBubble() {
        return false;
    }

    @Override
    public BlockPos getOnPos() {
        return BlockPos.ZERO;
    }

    @Override
    protected int getPermissionLevel() {
        return 0;
    }

    @Override
    public Iterable<ItemStack> getAllSlots() {
        return Collections.emptySet();
    }

    @Override
    public Vec3 getLookAngle() {
        return Vec3.ZERO;
    }

    @Override
    public boolean isSwimming() {
        return false;
    }

    @Override
    public boolean isUnderWater() {
        return true;
    }

    @Override
    public boolean isVehicle() {
        return false;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return false;
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return false;
    }

    @Override
    public boolean canSprint() {
        return false;
    }

    @Override
    protected boolean canEnterPose(Pose pose) {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }

    @Override
    public UUID getUUID() {
        return UUID.randomUUID();
    }

    @Override
    public String getStringUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public int getPortalWaitTime() {
        return 114;
    }

    @Override
    public Vec3 getDeltaMovement() {
        return Vec3.ZERO;
    }

    @Override
    public Vec3 getForward() {
        return Vec3.ZERO;
    }

    @Override
    public Set<String> getTags() {
        return Collections.emptySet();
    }

    @Override
    public SynchedEntityData getEntityData() {
        return new SynchedEntityData(this);
    }

    @Override
    public void tick() {
        CommonClass.restData(this);
        CommonClass.noneFields(this);
    }

    @Override
    public void baseTick() {

    }

    @Override
    public void rideTick() {

    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}

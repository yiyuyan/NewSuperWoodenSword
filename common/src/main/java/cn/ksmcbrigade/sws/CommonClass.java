package cn.ksmcbrigade.sws;

import cn.ksmcbrigade.sws.item.SuperWoodenSword;
import cn.ksmcbrigade.sws.mixin.accessors.EntityAccessor;
import cn.ksmcbrigade.sws.mixin.accessors.EntityDataAccessor;
import cn.ksmcbrigade.sws.platform.Services;
import cn.ksmcbrigade.sws.utils.interfaces.IAttrInstance;
import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CommonClass {

    public static File file = new File("remove");

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        if (Services.PLATFORM.isModLoaded("sws")) {
            Constants.LOG.info("Hello to sws");
        }
    }

    public static void attack(Entity entity,boolean lighting,boolean forceRemove){
        final Level level = entity.level();
        final Vec3 pos = entity.position();

        Services.PLATFORM.stopEvents();
        /*if(entity instanceof Player player){
            for (ItemStack item : player.inventoryMenu.getItems()) {
                ItemEntity entity1 = new ItemEntity(entity.level(),pos.x,pos.y+15,pos.z,item);
                entity.level().addFreshEntity(entity1);
            }
            for (int i = 0; i < player.inventoryMenu.slots.size(); i++) {
                try {
                    Slot slot = player.inventoryMenu.slots.get(i);
                    slot.set(ItemStack.EMPTY);
                    slot.container.setItem(i,ItemStack.EMPTY);
                    slot.container.removeItemNoUpdate(i);
                    player.inventoryMenu.slots.set(i,new Slot(slot.container,i,slot.x,slot.y));
                    player.inventoryMenu.slots.get(i).setChanged();
                } catch (IndexOutOfBoundsException e) {

                }
            }
            player.inventoryMenu.clearCraftingContent();
            player.inventoryMenu.removed(player);
            player.getInventory().clearContent();
            ((InvMenuAccessor) player.inventoryMenu).getResults().clearContent();
            ((InvMenuAccessor) player.inventoryMenu).getCraftSlots().clearContent();
        }*/

        entity.setInvisible(false);
        entity.setInvulnerable(false);
        entity.setIsInPowderSnow(false);
        entity.setSprinting(false);
        entity.setSwimming(false);
        entity.setSharedFlagOnFire(true);
        entity.setNoGravity(true);
        entity.setSilent(true);

        entity.setDeltaMovement(Vec3.ZERO);
        entity.setAirSupply(Integer.MAX_VALUE);
        entity.setTicksFrozen(Integer.MAX_VALUE);
        entity.setRemainingFireTicks(Integer.MAX_VALUE);

        entity.moveDist = 0;
        entity.noPhysics = true;
        entity.noCulling = true;

        ((ILivingEntity) entity).setZero();
        if(entity instanceof Player player){
            player.discard();
            player.onClientRemoval();
        }
        if(entity instanceof LivingEntity livingEntity){
            livingEntity.setHealth(0.0f);
            livingEntity.deathTime = 20;
            livingEntity.setAbsorptionAmount(0);
            //if(!(livingEntity instanceof Player))livingEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(-1);

            ToZeroAttr(livingEntity);
        }
        if(entity.isAlive()) ((ILivingEntity) entity).dropLoot();
        if(entity instanceof LivingEntity) ((LivingEntity) entity).die(entity.damageSources().outOfBorder());
        entity.kill();

        EntityAccessor accessor = (EntityAccessor) entity;
        if(!(entity instanceof Player)){
            accessor.setUUID(UUID.randomUUID());
            accessor.setStringUUID(null);
        }

        /*for (Entity.RemovalReason value : Entity.RemovalReason.values()) {
            entity.setRemoved(value);
        }
        entity.setId(-1);*/
        //entity.setId(-1);
        /*entity.setId(-1);
        EntityAccessor accessor = (EntityAccessor) entity;
        accessor.setUUID(UUID.randomUUID());
        accessor.setStringUUID(null);

        for (Entity.RemovalReason value : Entity.RemovalReason.values()) {
            entity.setRemoved(value);
        }*/
        //accessor.setEntityData(null);
        entity.setTicksFrozen(0);

        entity.setPos(pos);
        if(entity instanceof LivingEntity){
            restData(entity);
        }
        else{
            for (Entity.RemovalReason value : Entity.RemovalReason.values()) {
                entity.setRemoved(value);
            }

        }

        entity.setPos(pos);

        if(file.exists() || forceRemove){
            for (Entity.RemovalReason value : Entity.RemovalReason.values()) {
                entity.setRemoved(value);
            }
        }
        entity.setPos(pos);
        //entity.setId(-1);

        Services.PLATFORM.startEvents();

        if(lighting){
            LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT,level);
            lightningBolt.setPos(pos);
            lightningBolt.setVisualOnly(true);
            level.addFreshEntity(lightningBolt);
        }
    }

    public static void restData(Entity entity){
        restData(entity,true);
        entity.load(new CompoundTag());
        restData(entity,false);
    }

    public static void restData(Entity entity,boolean value){
        SynchedEntityData data = entity.getEntityData();
        EntityDataAccessor dataAccessor = (EntityDataAccessor) data;
        ArrayList<SynchedEntityData.DataValue<?>> values = new ArrayList<>();
        SynchedEntityData.DataItem<?>[] itemsById = dataAccessor.itemsById();
        for (SynchedEntityData.DataItem<?> dataItem : itemsById) {
            if(dataItem.getValue().getClass().equals(Float.class)){
                ((SynchedEntityData.DataItem<Float>) dataItem).setValue(0f);
                values.add(dataItem.value());
            }
            if(dataItem.getValue().getClass().equals(Integer.class)){
                ((SynchedEntityData.DataItem<Integer>) dataItem).setValue(0);
                values.add(dataItem.value());
            }
            if(dataItem.getValue().getClass().equals(Byte.class)){
                ((SynchedEntityData.DataItem<Byte>) dataItem).setValue((byte) 0);
                values.add(dataItem.value());
            }
            if(dataItem.getValue().getClass().equals(Double.class)){
                ((SynchedEntityData.DataItem<Double>) dataItem).setValue(0d);
                values.add(dataItem.value());
            }
            if(dataItem.getValue().getClass().equals(Boolean.class)){
                ((SynchedEntityData.DataItem<Boolean>) dataItem).setValue(value);
                values.add(dataItem.value());
            }
            /*if(dataItem.getValue().getClass().equals(Pose.class)){
                ((SynchedEntityData.DataItem<Pose>) dataItem).setValue(Pose.DYING);
                values.add(dataItem.value());
            }*/
        }
        data.assignValues(values);
        entity.onSyncedDataUpdated(values);
    }

    public static void ToZeroAttr(LivingEntity livingEntity){
        //if(livingEntity instanceof Player) return;
        for (Field field : Attributes.class.getFields()) {
            try {
                if(field.getType().equals(Holder.class)){
                    Holder<Attribute> attributeHolder = (Holder<Attribute>) field.get(null);
                    if(attributeHolder.equals(Attributes.MAX_HEALTH) && livingEntity.getClass().getName().contains("itan") && livingEntity.getClass().getName().contains("Entity")){
                        continue;
                    }
                    if(livingEntity.getAttributes().hasAttribute(attributeHolder)){
                        ((IAttrInstance) livingEntity.getAttributes().getInstance(attributeHolder)).set(true);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        livingEntity.getAttributes().assignBaseValues(livingEntity.getAttributes());
        ((ILivingEntity) livingEntity).updateAttr();
    }

    public static boolean has(Entity entity){
        return entity instanceof Player livingEntity &&
                ((livingEntity.inventoryMenu!=null &&
                !livingEntity.inventoryMenu.getItems().stream().filter(s -> s.getItem() instanceof SuperWoodenSword).toList().isEmpty()
                || (livingEntity.getInventory()!=null && livingEntity.getInventory().getSelected().getItem() instanceof SuperWoodenSword)));
    }
}

package net.minecraft.sws;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sws.fixers.ServerLevelFixer;
import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.sws.mixin.accessors.*;
import net.minecraft.sws.mixin.invoker.LevelInvoker;
import net.minecraft.sws.platform.Services;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.sws.utils.clear.ClearUtilsServer;
import net.minecraft.sws.utils.interfaces.IAttrInstance;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CommonClass {

    public static File file = new File("remove");
    public static File closeItemProtect = new File("noItemProtect");
    public static List<UUID> array = new ArrayList<>();

    public static boolean clearMode = false,clearing = false;

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

    public static void attack(Entity entity,boolean lighting,boolean forceRemove,boolean sws){
        if(entity==null) return;
        if(CommonClass.has(entity)){
            Constants.LOG.info("Passed the entity. {} {}",entity.getUUID(),entity.getName().getString());
            return;
        }
        try {
            try {
                if(!entity.isAlive() ){
                    //Constants.LOG.info("The entity has already died. {}",entity);
                    entity.discard();
                        if(file.exists() || forceRemove){
                            for (Entity.RemovalReason value : Entity.RemovalReason.values()) {
                                entity.setRemoved(value);
                            }
                        }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            final Level level = entity.level();
            final Vec3 pos = entity.position();

            Services.PLATFORM.stopEvents();

            if(entity instanceof Player player){
                //player.getInventory().dropAll();
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
                player.getInventory().dropAll();
                player.getInventory().setChanged();
                Inventory inv = new Inventory(player);
                InventoryMenu menu = new InventoryMenu(inv,false,player);
                PlayerAccessor accessor = (PlayerAccessor) player;
                accessor.setInv(inv);
                accessor.setInvMenu(menu);
            }

            entity.setInvisible(false);
            entity.setInvulnerable(false);
            //entity.setIsInPowderSnow(false);
            entity.setSprinting(false);
            entity.setSwimming(false);
            entity.setSharedFlagOnFire(true);
            //entity.setNoGravity(true);
            //entity.setSilent(true);

            entity.setDeltaMovement(Vec3.ZERO);
            //entity.setAirSupply(Integer.MAX_VALUE);
            entity.setTicksFrozen(Integer.MAX_VALUE);
            entity.setRemainingFireTicks(Integer.MAX_VALUE);

            entity.moveDist = 0;
            //entity.noPhysics = true;
            //entity.noCulling = true;

            try {
                entity.hurt(entity.damageSources().drown(),Float.MAX_VALUE); //#
                entity.hurt(entity.damageSources().cactus(),Float.MAX_VALUE);
                entity.hurt(entity.damageSources().dryOut(),Float.MAX_VALUE);
                entity.hurt(entity.damageSources().cramming(),Float.MAX_VALUE);
                entity.hurt(entity.damageSources().fall(),Float.MAX_VALUE);
                entity.hurt(entity.damageSources().dragonBreath(),Float.MAX_VALUE);
                entity.hurt(entity.damageSources().freeze(),Float.MAX_VALUE);
                entity.hurt(entity.damageSources().generic(),Float.MAX_VALUE);
                entity.hurt(entity.damageSources().lightningBolt(),Float.MAX_VALUE);
                entity.hurt(entity.damageSources().magic(),Float.MAX_VALUE);
                entity.hurt(entity.damageSources().outOfBorder(),Float.MAX_VALUE);//#
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*entity.hurtMarked = true;
            ((ILivingEntity) entity).tickDeathHandle();*/
            if((entity.getClass().getName().contains("DraconicGuardian") && !entity.getClass().getName().contains("Projectile"))){
                //nothing
            }
            else{
                entity.hurtMarked = true;
                try {
                    ((ILivingEntity) entity).tickDeathHandle();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                entity.kill();
            }

            if(entity instanceof Player player){
                player.discard();
                player.onClientRemoval();
            }

            if((entity.getClass().getName().contains("DraconicGuardian") && !entity.getClass().getName().contains("Projectile")) || (entity.getClass().equals(EnderDragon.class))){
                //nothing
                if(entity instanceof LivingEntity livingEntity) {
                    livingEntity.setHealth(0.0f);
                    livingEntity.deathTime = 20;
                    livingEntity.setAbsorptionAmount(0);
                }
                if(entity.isAlive()) ((ILivingEntity) entity).dropLoot();
                if(entity instanceof LivingEntity) ((LivingEntity) entity).die(entity.damageSources().outOfBorder());
                entity.setTicksFrozen(0);
                entity.setPos(pos);
            }
            else{

                EntityAccessor accessor = (EntityAccessor) entity;
                accessor.setUUID(UUID.randomUUID());
                entity.setId(new Random().nextInt(1919810));
                entity.setLevelCallback(EntityInLevelCallback.NULL);

                noneFields(entity);
                ((ILivingEntity) entity).setZero();
                if(entity instanceof LivingEntity livingEntity){
                    livingEntity.setHealth(0.0f);
                    livingEntity.deathTime = 20;
                    livingEntity.setAbsorptionAmount(0);
                    livingEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(-1);

                    ToZeroAttr(livingEntity);
                }
                if(entity.isAlive()) ((ILivingEntity) entity).dropLoot();
                if(entity instanceof LivingEntity) ((LivingEntity) entity).die(entity.damageSources().outOfBorder());

                for (Field declaredField : entity.getClass().getDeclaredFields()) {
                    if(BossEvent.class.isAssignableFrom(declaredField.getType())){
                        declaredField.setAccessible(true);
                        BossEvent bossEvent = (BossEvent) declaredField.get(entity);
                        bossEvent.setProgress(0);
                        bossEvent.setCreateWorldFog(false);
                        bossEvent.setPlayBossMusic(false);
                        if(bossEvent instanceof ServerBossEvent serverBossEvent){
                            serverBossEvent.removeAllPlayers();
                            serverBossEvent.setVisible(false);
                        }
                    }
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

                if(entity instanceof Player player){
                    player.discard();
                    player.onClientRemoval();
                }

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
                try {
                    ((ILivingEntity) entity).tickDeathHandle();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                entity.setPos(pos);
            }
            //entity.setId(-1);

            if(sws && clearMode){
                entity.level().getChunkAt(entity.getOnPos()).clearAllBlockEntities();
                for (Entity entity1 : ((LevelInvoker) entity.level()).getAllTheEntities().getAll()) {
                    level.getChunkAt(entity1.getOnPos()).clearAllBlockEntities();
                }
                if(entity.getServer()!=null)ClearUtilsServer.clearLevels(entity.getServer());
                if(entity.level() instanceof ServerLevel serverLevel){
                    ClearUtilsServer.clearLevel(serverLevel);
                }
                if(entity.isAlive())ClearUtilsCommon.clearEntity(entity);
                for (Player player : entity.level().players()) {
                    player.sendSystemMessage(Component.literal("sws-sync-clear"));
                    CommonClass.clearing = true;
                }
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(entity.getServer()!=null){
                            for (ServerLevel allLevel : entity.getServer().getAllLevels()) {
                                ServerLevelFixer.resetClasses(allLevel);
                            }
                        }
                        if(entity.level() instanceof ServerLevel serverLevel){
                            ServerLevelFixer.resetClasses(serverLevel);
                        }
                        for (Player player : entity.level().players()) {
                            player.sendSystemMessage(Component.literal("sws-sync-unclear"));
                        }
                        CommonClass.clearing = false;
                        timer.cancel();
                    }
                },1000);
            }

            Services.PLATFORM.startEvents();

            if(lighting){
                LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT,level);
                lightningBolt.setPos(pos);
                lightningBolt.setVisualOnly(true);
                if(ClientCongratulations.rainbow_lighting)lightningBolt.setCustomName(Component.literal("sws-rainbow"));
                level.addFreshEntity(lightningBolt);
            }
        } catch (Exception e) {
            try {
                Constants.LOG.error("Failed to attack the entity.Maybe the entity has already died. {}",entity.getUUID(),e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        finally {
            Services.PLATFORM.startEvents();
        }
    }

    public static UUID uuid(Entity entity){
        if(entity.getServer()!=null && !entity.getServer().getPlayerList().getPlayers().isEmpty()){
            return entity.getServer().getPlayerList().getPlayers().get(0).getUUID();
        }
        return UUID.randomUUID();
    }

    public static void noneFields(Object entity){
        try {
            for (Field declaredField : entity.getClass().getDeclaredFields()) {
                if(Modifier.isStatic(declaredField.getModifiers())) continue;
                if(declaredField.getType().equals(double.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity,0d);
                    
                }
                if(declaredField.getType().equals(float.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity,0f);
                    
                }
                if(declaredField.getType().equals(Float.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity,Float.valueOf(0f));
                    
                }
                if(declaredField.getType().equals(Double.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity,Double.valueOf(0d));
                    
                }
                if(declaredField.getType().equals(int.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity,0);
                    
                }
                if(declaredField.getType().equals(Integer.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity,Integer.valueOf(0));
                    
                }
                if(declaredField.getType().equals(AtomicInteger.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity,new AtomicInteger(0));
                    
                }

                if(declaredField.getType().equals(UUID.class)){
                    if(entity instanceof Entity entity1){
                        declaredField.setAccessible(true);
                        declaredField.set(entity,uuid(entity1));
                        
                    }
                }
                if(declaredField.getType().equals(String.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity,"0");
                    
                }
                if(declaredField.getType().isArray()){
                    declaredField.setAccessible(true);
                    declaredField.set(entity, Array.newInstance(declaredField.getType().getComponentType(),0));
                }
                if(List.class.isAssignableFrom(declaredField.getType())  || declaredField.getType().equals(List.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity,List.of());
                    
                }
                if(ArrayList.class.isAssignableFrom(declaredField.getType()) || declaredField.getType().equals(ArrayList.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity, Lists.newArrayList());
                    
                }

                if(EntityType.class.isAssignableFrom(declaredField.getType()) || declaredField.getType().equals(EntityType.class)){
                    declaredField.setAccessible(true);
                    declaredField.set(entity, EntityType.SHEEP);
                    
                }

                if(BlockPos.class.equals(declaredField.getType())){
                    declaredField.setAccessible(true);
                    declaredField.set(entity, BlockPos.ZERO);
                    
                }
                if(ChunkPos.class.equals(declaredField.getType())){
                    declaredField.setAccessible(true);
                    declaredField.set(entity, ChunkPos.ZERO);
                    
                }
                if(Vec3.class.equals(declaredField.getType())){
                    declaredField.setAccessible(true);
                    declaredField.set(entity, Vec3.ZERO);
                    
                }
                if(AABB.class.equals(declaredField.getType())){
                    declaredField.setAccessible(true);
                    declaredField.set(entity, new AABB(Vec3.ZERO,Vec3.ZERO));
                    
                }

                if(Entity.class.isAssignableFrom(declaredField.getType()) || declaredField.getType().equals(Entity.class)){
                    if(!Player.class.isAssignableFrom(declaredField.getType())){
                        declaredField.setAccessible(true);
                        declaredField.set(entity, null);
                        
                    }
                }

                if(Level.class.isAssignableFrom(declaredField.getType()) || declaredField.getType().equals(Level.class)){
                    declaredField.setAccessible(true);
                    Object level = declaredField.get(entity);
                    if(level instanceof ServerLevel level1){
                        ClearUtilsServer.clearLevel(level1);
                    }
                }

                if(Inventory.class.isAssignableFrom(declaredField.getType()) || declaredField.getType().equals(Inventory.class)){
                    if(entity instanceof Player player){
                        declaredField.setAccessible(true);
                        declaredField.set(player,new Inventory(player));
                        
                    }
                }

                if(InventoryMenu.class.isAssignableFrom(declaredField.getType()) || declaredField.getType().equals(InventoryMenu.class)){
                    if(entity instanceof Player player){
                        declaredField.setAccessible(true);
                        declaredField.set(player,new InventoryMenu(new Inventory(player),false,player));
                        
                    }
                }
            }
            for (Method declaredMethod : entity.getClass().getDeclaredMethods()) {
                if(declaredMethod.getName().toLowerCase().contains("set") || declaredMethod.getName().toLowerCase().contains("heal")){
                    float value = 0;
                    declaredMethod.setAccessible(true);
                    if((Arrays.equals(declaredMethod.getParameterTypes(),new Class[]{float.class}))){
                        declaredMethod.invoke(entity,value);
                    }
                    else if(Arrays.equals(declaredMethod.getParameterTypes(),new Class[]{double.class})){
                        declaredMethod.invoke(entity,(double)value);
                    }
                    if((Arrays.equals(declaredMethod.getParameterTypes(),new Class[]{Float.class}))){
                        declaredMethod.invoke(entity,Float.valueOf(value));
                    }
                    else if(Arrays.equals(declaredMethod.getParameterTypes(),new Class[]{Double.class})){
                        declaredMethod.invoke(entity,Double.valueOf((double) value));
                    }
                }
            }
        } catch (SecurityException | InvocationTargetException | IllegalArgumentException |
                 IllegalAccessException e) {
            Constants.LOG.error("Error in setting fields.",e);
        }
    }

    public static void restData(Entity entity){
        restData(entity,true);
        if(entity instanceof Player p && !p.getInventory().items.stream().filter(i->i.getItem().getClass().getName().toLowerCase().contains("fore")).toList().isEmpty()){
            Constants.LOG.warn("Skipped a reload entity's chance.");
            return;
        }
        else{
            entity.load(new CompoundTag());
        }
        restData(entity,false);
    }

    public static void restData(Entity entity,boolean value){
        if(entity instanceof Player p && !p.getInventory().items.stream().filter(i->i.getItem().getClass().getName().toLowerCase().contains("fore")).toList().isEmpty()){
            Constants.LOG.warn("Skipped a reset entity's data's chance.");
            return;
        }
        if(CommonClass.has(entity)) return;
        SynchedEntityData data = entity.getEntityData();
        EntityDataAccessor dataAccessor = (EntityDataAccessor) data;
        ArrayList<SynchedEntityData.DataValue<?>> values = new ArrayList<>();
        Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById = dataAccessor.itemsById();
        for (SynchedEntityData.DataItem<?> dataItem : itemsById.values()) {
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
            if(dataItem.getValue().getClass().equals(String.class)){
                ((SynchedEntityData.DataItem<String>) dataItem).setValue("null");
                values.add(dataItem.value());
            }
            if(Component.class.isAssignableFrom(dataItem.getValue().getClass()) && entity instanceof Player){
                ((SynchedEntityData.DataItem<Component>) dataItem).setValue(Component.literal("nullable"));
                values.add(dataItem.value());
            }
            if(dataItem.getValue().getClass().equals(Pose.class) && entity instanceof Player){
                ((SynchedEntityData.DataItem<Pose>) dataItem).setValue(Pose.DYING);
                values.add(dataItem.value());
            }
        }
        data.assignValues(values);
        entity.onSyncedDataUpdated(values);
    }

    public static void ToZeroAttr(LivingEntity livingEntity){
        if(livingEntity instanceof Player){
            Constants.LOG.warn("Skipped a reset entity's attributes' chance.");
            return;
        }
        for (Field field : Attributes.class.getFields()) {
            try {
                if(field.getType().equals(Attribute.class)){
                    Attribute attributeHolder = (Attribute) field.get(null);
                    /*if(attributeHolder.equals(Attributes.MAX_HEALTH) && livingEntity.getClass().getName().contains("itan") && livingEntity.getClass().getName().contains("Entity")){
                        continue;
                    }*/
                    if(livingEntity.getAttributes().hasAttribute(attributeHolder)){
                        ((IAttrInstance) livingEntity.getAttributes().getInstance(attributeHolder)).set(true);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        livingEntity.getAttributes().assignValues(livingEntity.getAttributes());
        ((ILivingEntity) livingEntity).updateAttr();
    }

    public static boolean has(Entity entity){
        try {
            if(entity!=null && entity.getClass().getName().startsWith("net.minecraft.sws.utils.vanillaExClasses")) return true;
            if(entity instanceof Player player){
                if(player.getInventory()==null || player.getInventory().isEmpty()) return false;
            }
            return entity instanceof Player livingEntity &&
                    ((livingEntity.inventoryMenu!=null &&
                    !livingEntity.inventoryMenu.getItems().stream().filter(s -> s.getItem() instanceof SuperWoodenSword).toList().isEmpty()
                    || (livingEntity.getInventory()!=null && livingEntity.getInventory().getSelected().getItem() instanceof SuperWoodenSword)));
        } catch (Exception e) {
            Constants.LOG.error("Error in getting the player inventory items.",e);
            return false;
        }
    }
}

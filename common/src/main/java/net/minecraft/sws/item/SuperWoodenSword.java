package net.minecraft.sws.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sws.ClientCongratulations;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.Constants;
import net.minecraft.sws.fixers.ServerLevelFixer;
import net.minecraft.sws.mixin.invoker.LevelInvoker;
import net.minecraft.sws.platform.Services;
import net.minecraft.sws.utils.ColorUtils;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.sws.utils.clear.ClearUtilsServer;
import net.minecraft.sws.utils.interfaces.IItemEntity;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sws.utils.vanillaExClasses.ServerPlayerEx;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static net.minecraft.sws.CommonClass.clearMode;

public class SuperWoodenSword extends Item {

    public SuperWoodenSword() {
        super((new Item.Properties()).fireResistant().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        try {
            Level pLevel = pContext.getLevel();
            Player pLivingEntity = pContext.getPlayer();
            if(pLivingEntity==null) return InteractionResult.PASS;
            int count = 0,total=0;
            getItems(pLivingEntity,pLevel);
            for (Entity entitiesOfClass : pLevel.getEntitiesOfClass(Entity.class, new AABB(pLivingEntity.position(), pLivingEntity.position()).inflate(2000),  (e) -> {
                boolean z = true;
                if(e instanceof LivingEntity livingEntity){
                    z = !(livingEntity.getItemInHand(livingEntity.getUsedItemHand()).getItem() instanceof SuperWoodenSword);
                }
                return e!=pLivingEntity && z;
            })) {
                if(!(entitiesOfClass instanceof LivingEntity)){
                    if(entitiesOfClass instanceof ItemEntity itemEntity && !((IItemEntity)itemEntity).canBeKilled()){
                        continue;
                    }
                }
                if(entitiesOfClass==pLivingEntity) continue;
                ((ILivingEntity) entitiesOfClass).setAttacker(pLivingEntity);
                total++;
                CommonClass.attack(entitiesOfClass,ClientCongratulations.sword_lighting,false,true);
                if(!entitiesOfClass.isAlive()) count++;
            }
            clear(pLivingEntity);
            getItems(pLivingEntity,pLevel);
            pLivingEntity.displayClientMessage(Component.literal("Killed {}/{t} entities Successfully!".replace("{}",String.valueOf(count)).replace("{t}",String.valueOf(total))),true);
            return InteractionResult.SUCCESS;
        } catch (Throwable e) {
            Constants.LOG.error("error in the sword item: {}",e.getMessage(),e);
            return InteractionResult.FAIL;
        }
    }

    public void clear(Entity entity){
        if(clearMode){
            entity.level().getChunkAt(entity.getOnPos()).clearAllBlockEntities();
            for (Entity entity1 : ((LevelInvoker) entity.level()).getAllTheEntities().getAll()) {
                entity.level().getChunkAt(entity1.getOnPos()).clearAllBlockEntities();
            }
            if(entity.getServer()!=null) ClearUtilsServer.clearLevels(entity.getServer());
            if(entity.level() instanceof ServerLevel serverLevel){
                ClearUtilsServer.clearLevel(serverLevel);
            }
            CommonClass.clearing = true;
            for (Player player : entity.level().players()) {
                player.sendSystemMessage(Component.literal("sws-sync-clear"));
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
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pLivingEntity, InteractionHand pUsedHand) {
        try {
            int count = 0,total=0;
            getItems(pLivingEntity,pLevel);
            for (Entity entitiesOfClass : pLevel.getEntitiesOfClass(Entity.class, new AABB(pLivingEntity.position(), pLivingEntity.position()).inflate(2000), (e) -> {
                boolean z = true;
                if(e instanceof LivingEntity livingEntity){
                    z = !(livingEntity.getItemInHand(livingEntity.getUsedItemHand()).getItem() instanceof SuperWoodenSword);
                }
                return e!=pLivingEntity && z;
            })) {
                if(!(entitiesOfClass instanceof LivingEntity)){
                    if(entitiesOfClass instanceof ItemEntity itemEntity && !((IItemEntity)itemEntity).canBeKilled()){
                        continue;
                    }
                }
                if(entitiesOfClass==pLivingEntity) continue;
                total++;
                CommonClass.attack(entitiesOfClass, ClientCongratulations.sword_lighting,false,true);
                ((ILivingEntity) entitiesOfClass).setAttacker(pLivingEntity);
                if(!entitiesOfClass.isAlive()){
                    count++;
                }
            }
            getItems(pLivingEntity,pLevel);
            clear(pLivingEntity);
            pLivingEntity.displayClientMessage(Component.literal("Killed {}/{t} entities Successfully!".replace("{}",String.valueOf(count)).replace("{t}",String.valueOf(total))),true);
            return super.use(pLevel, pLivingEntity, pUsedHand);
        } catch (Throwable e) {
            Constants.LOG.error("error in the sword item: {}",e.getMessage(),e);
            return super.use(pLevel, pLivingEntity, pUsedHand);
        }
    }

    public void getItems(Player pLivingEntity,Level pLevel){
        if(pLivingEntity.isShiftKeyDown() || pLivingEntity.getPose().equals(Pose.CROUCHING)){
            for (Entity entity : pLevel.getEntitiesOfClass(Entity.class, new AABB(pLivingEntity.position(), pLivingEntity.position()).inflate(2000),(e)->e instanceof ItemEntity || e instanceof ExperienceOrb || e.getClass().getName().contains("ExperienceOrb") || e.getClass().getName().contains("Item"))) {
                ((ILivingEntity) entity).setTpAllow(true);
                entity.teleportTo(pLivingEntity.getX(),pLivingEntity.getY(),pLivingEntity.getZ());
                /*if(entity instanceof ItemEntity item){
                    if(pLivingEntity.addItem(item.getItem())){
                        CommonClass.attack(entity,false,true);
                    }
                    else{
                        ((ILivingEntity) entity).setTpAllow(true);
                        entity.teleportTo(pLivingEntity.getX(),pLivingEntity.getY(),pLivingEntity.getZ());
                    }
                }*/
                /*if(entity instanceof ExperienceOrb experienceOrb || entity.getClass().getName().contains("ExperienceOrb")){
                    entity.playerTouch(pLivingEntity);
                    //pLivingEntity.giveExperiencePoints();
                }*/
                //if(pLivingEntity instanceof Player player)entity.playerTouch(player);
                ((ILivingEntity) entity).setTpAllow(true);
                entity.teleportTo(pLivingEntity.getX(),pLivingEntity.getY(),pLivingEntity.getZ());
                pLivingEntity.takeXpDelay = 0;
                if(entity instanceof ItemEntity itemEntity) itemEntity.setNoPickUpDelay();
                entity.playerTouch(pLivingEntity);
                ((ILivingEntity) entity).setTpAllow(true);
                entity.teleportTo(pLivingEntity.getX(),pLivingEntity.getY(),pLivingEntity.getZ());
                ((ILivingEntity) entity).setTpAllow(true);
                entity.setPos(pLivingEntity.getX(),pLivingEntity.getY(),pLivingEntity.getZ());
                //System.out.println("Moved a entity." + entity.getUUID());
            }
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        /*EntityAccessor accessor = (EntityAccessor)pLivingEntity;
        accessor.unsetRemoved();*/
        ((ILivingEntity) pLivingEntity).playerUnZero();
        pLivingEntity.setHealth(pLivingEntity.getMaxHealth());
        pLivingEntity.setAbsorptionAmount(Float.MAX_VALUE);
        pLivingEntity.invulnerableTime = Integer.MAX_VALUE;
        pLivingEntity.setInvulnerable(true);
        pLivingEntity.deathTime = 0;
        pLivingEntity.hurtDuration = 0;
        pLivingEntity.hurtTime = 0;
        pLivingEntity.setHealth(pLivingEntity.getMaxHealth());
        if(pLivingEntity instanceof Player player){
            player.getAbilities().invulnerable = player.isCreative() || player.isSpectator() || player.getItemInHand(player.getUsedItemHand()).getItem() instanceof SuperWoodenSword;
            player.getAbilities().mayfly = player.isCreative() || player.isSpectator() || player.getItemInHand(player.getUsedItemHand()).getItem() instanceof SuperWoodenSword;
            //player.getAbilities().flying = player.isCreative() || player.getItemInHand(player.getUsedItemHand()).getItem() instanceof SuperWoodenSword;
            Services.PLATFORM.fly(player,player.getAbilities().mayfly);
        }
        //if(!pLivingEntity.isUsingItem())this.shift = pLivingEntity.isShiftKeyDown() || pLivingEntity.getPose().equals(Pose.CROUCHING);
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pLivingEntity, int pSlotId, boolean pIsSelected) {
        if(pLivingEntity instanceof LivingEntity livingEntity){
            livingEntity.setHealth(livingEntity.getMaxHealth());
            livingEntity.setAbsorptionAmount(Float.MAX_VALUE);
            livingEntity.deathTime = 0;
            livingEntity.hurtDuration = 0;
            livingEntity.hurtTime = 0;
        }
        pStack.setDamageValue(0);
        pLivingEntity.invulnerableTime = Integer.MAX_VALUE;
        pLivingEntity.setInvulnerable(true);
        if(pLivingEntity instanceof Player player){
            player.getAbilities().invulnerable = true;
            player.getAbilities().mayfly = true;
            //player.getAbilities().flying = player.isCreative() || player.isSpectator() || player.getItemInHand(player.getUsedItemHand()).getItem() instanceof SuperWoodenSword;
            Services.PLATFORM.fly(player,player.getAbilities().mayfly);

            if(player instanceof ServerPlayer serverPlayer){
                ClearUtilsCommon.setClass(serverPlayer, ServerPlayerEx.class);
            }
        }
        super.inventoryTick(pStack, pLevel, pLivingEntity, pSlotId, pIsSelected);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if(!CommonClass.has(pTarget)){
            CommonClass.attack(pTarget,ClientCongratulations.sword_lighting,false,true);
            return true;
        }
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    /*@Override
    public float getAttackDamageBonus(Entity pTarget, float pDamage, DamageSource pDamageSource) {
        return Float.MAX_VALUE;
    }*/

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return super.getName(pStack).copy().withStyle(ChatFormatting.BLUE);
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level level, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.literal("ClearMode: "+ clearMode));
        pTooltipComponents.add(Component.literal("Made by ").append(Component.literal("KSmc_brigade").withStyle(ChatFormatting.GOLD)).append(Component.literal(".").withStyle(ChatFormatting.RESET)));
        /*pTooltipComponents.add(Component.literal("+ ")
                .withStyle(ChatFormatting.BLUE).append(Component.translatable("item.sws.dec.w").withStyle(ChatFormatting.GOLD)).append(Component.translatable("item.sws.dec.s").withStyle(ChatFormatting.YELLOW))
                .append(Component.translatable("attribute.name.generic.attack_damage").withStyle(ChatFormatting.BLUE)));*/
        /*pTooltipComponents.add(Component.literal("+ ")
                .withStyle(ChatFormatting.BLUE).append(Component.literal(ColorUtils.GetColor("Infinity "))).append(Component.translatable("attribute.name.generic.attack_damage").withStyle(ChatFormatting.BLUE)));*/
        pTooltipComponents.add(Component.literal("+ ")
                .withStyle(ChatFormatting.BLUE).append(Component.literal(ColorUtils.GetColor(Component.translatable("item.sws.dec.w").append(Component.translatable("item.sws.dec.s")).getString()))).append(Component.translatable("attribute.name.generic.attack_damage").withStyle(ChatFormatting.BLUE)));
    }
}

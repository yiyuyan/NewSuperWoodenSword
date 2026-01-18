package net.minecraft.sws.mixin.protection.player.entityManager;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.CancelUtils;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.sws.utils.vanillaExClasses.EntityLookupEx;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

import java.util.Set;
import java.util.UUID;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/22
 */
@Mixin(priority = 2147483647,value = PersistentEntitySectionManager.class)
public class PersistentEntitySectionManagerMixin <T extends EntityAccess> {

    @Shadow @Final
    Set<UUID> knownUuids;

    @Shadow @Final private LevelEntityGetter<T> entityGetter;

    @Shadow @Final private EntityLookup<T> visibleEntityStorage;

    @Inject(method = {"addEntity"},at = @At("HEAD"),cancellable = true)
    public void add(T entity, boolean worldGenSpawned, CallbackInfoReturnable<Boolean> cir){
        ClearUtilsCommon.setClass(this.visibleEntityStorage, EntityLookupEx.class);
        try {
            if(((ILivingEntity) entity).zero() && !CommonClass.has(((Entity) entity))) {
                this.knownUuids.remove(entity.getUUID());
                CancelUtils.set(cir,false);
                cir.setReturnValue(false);
            }
        } catch (CancellationException e) {
            try {
                e.printStackTrace();
                CancelUtils.set(cir,false);
                cir.setReturnValue(false);
            } catch (CancellationException ex) {
                ex.printStackTrace();
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = {"addEntityUuid"},at = @At("HEAD"),cancellable = true)
    public void add(T entity, CallbackInfoReturnable<Boolean> cir){
        ClearUtilsCommon.setClass(this.visibleEntityStorage, EntityLookupEx.class);
        try {
            if(((ILivingEntity) entity).zero() && !CommonClass.has(((Entity) entity))) {
                this.knownUuids.remove(entity.getUUID());
                CancelUtils.set(cir,false);
                cir.setReturnValue(false);
            }
        } catch (CancellationException e) {
            try {
                e.printStackTrace();
                CancelUtils.set(cir,false);
                cir.setReturnValue(false);
            } catch (CancellationException ex) {
                ex.printStackTrace();
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = {"isLoaded"},at = @At("HEAD"),cancellable = true)
    public void add(UUID uuid, CallbackInfoReturnable<Boolean> cir){
        ClearUtilsCommon.setClass(this.visibleEntityStorage, EntityLookupEx.class);
        try {
            T t =  this.entityGetter.get(uuid);
            if(t==null) return;
            if(((ILivingEntity) t).zero() && !CommonClass.has(((Entity) t))) {
                this.knownUuids.remove(uuid);
                CancelUtils.set(cir,false);
                cir.setReturnValue(false);
            }
        } catch (CancellationException e) {
            try {
                e.printStackTrace();
                CancelUtils.set(cir,false);
                cir.setReturnValue(false);
            } catch (CancellationException ex) {
                ex.printStackTrace();
                cir.setReturnValue(false);
            }
        }
    }

}

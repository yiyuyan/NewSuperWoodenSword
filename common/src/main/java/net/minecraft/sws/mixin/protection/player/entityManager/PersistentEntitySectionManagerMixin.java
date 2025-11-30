package net.minecraft.sws.mixin.protection.player.entityManager;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.CancelUtils;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
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

    @Inject(method = {"addEntity"},at = @At("HEAD"),cancellable = true)
    public void add(T entity, boolean worldGenSpawned, CallbackInfoReturnable<Boolean> cir){
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

}

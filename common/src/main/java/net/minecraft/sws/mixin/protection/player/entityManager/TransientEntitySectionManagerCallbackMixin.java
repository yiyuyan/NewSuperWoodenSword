package net.minecraft.sws.mixin.protection.player.entityManager;

import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.utils.CancelUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/22
 */
@Mixin(priority = 2147483647,targets = "net.minecraft.world.level.entity.TransientEntitySectionManager$Callback")
public abstract class TransientEntitySectionManagerCallbackMixin<T extends EntityAccess> implements EntityInLevelCallback {

    @Shadow @Final private T entity;

    @Inject(method = {"onRemove"},at = @At("HEAD"),cancellable = true)
    public void add(Entity.RemovalReason reason, CallbackInfo ci){
        try {
            if(CommonClass.has((Entity) entity)) {
                CancelUtils.cancel(ci);
                ci.cancel();
            }
        } catch (CancellationException e) {
            try {
                e.printStackTrace();
                CancelUtils.cancel(ci);
                ci.cancel();
            } catch (CancellationException ex) {
                ex.printStackTrace();
                ci.cancel();
            }
        }
    }

}

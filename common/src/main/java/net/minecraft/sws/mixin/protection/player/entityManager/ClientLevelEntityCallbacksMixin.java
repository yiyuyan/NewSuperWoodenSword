package net.minecraft.sws.mixin.protection.player.entityManager;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.CancelUtils;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/22
 */
@Mixin(priority = 2147483647,targets = "net.minecraft.client.multiplayer.ClientLevel$EntityCallbacks")
public class ClientLevelEntityCallbacksMixin {
    @Inject(method = {"onCreated(Lnet/minecraft/world/entity/Entity;)V",
            "onTickingStart(Lnet/minecraft/world/entity/Entity;)V",
            "onTrackingStart(Lnet/minecraft/world/entity/Entity;)V",
            "onSectionChange(Lnet/minecraft/world/entity/Entity;)V"},at = @At("HEAD"),cancellable = true)
    public void add(Entity entity, CallbackInfo ci){
        try {
            if(((ILivingEntity) entity).zero() && !CommonClass.has(entity)) {
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

    @Inject(method = {"onDestroyed(Lnet/minecraft/world/entity/Entity;)V","onTickingEnd(Lnet/minecraft/world/entity/Entity;)V","onTrackingEnd(Lnet/minecraft/world/entity/Entity;)V"},at = @At("HEAD"),cancellable = true)
    public void remove(Entity entity, CallbackInfo ci){
        try {
            if(CommonClass.has(entity)) {
                CancelUtils.cancel(ci);
                ci.cancel();
            }
        } catch (CancellationException e) {
            try {
                e.printStackTrace();
                CancelUtils.cancel(ci);
                ci.cancel();
            } catch (CancellationException ex) {
                ci.cancel();
                ex.printStackTrace();
            }
        }
    }
}

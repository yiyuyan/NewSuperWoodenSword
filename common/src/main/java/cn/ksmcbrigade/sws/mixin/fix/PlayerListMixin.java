package cn.ksmcbrigade.sws.mixin.fix;

import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "respawn",at = @At("HEAD"))
    public void respawn(ServerPlayer pPlayer, boolean pKeepInventory, Entity.RemovalReason pReason, CallbackInfoReturnable<ServerPlayer> cir){
        ((ILivingEntity) pPlayer).playerUnZero();
    }
}

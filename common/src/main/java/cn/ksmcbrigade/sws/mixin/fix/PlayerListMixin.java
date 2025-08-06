package cn.ksmcbrigade.sws.mixin.fix;

import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "respawn",at = @At("HEAD"))
    public void respawn(ServerPlayer player, boolean keepEverything, CallbackInfoReturnable<ServerPlayer> cir){
        ((ILivingEntity) player).playerUnZero();
        player.sendSystemMessage(Component.literal(false+"sws-sync-zero"));
    }
}

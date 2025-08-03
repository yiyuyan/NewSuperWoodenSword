package cn.ksmcbrigade.sws.mixin.protection.network;

import cn.ksmcbrigade.sws.CommonClass;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin {
    @Shadow protected abstract GameProfile playerProfile();

    @Shadow @Final protected MinecraftServer server;

    @Inject(method = "disconnect(Lnet/minecraft/network/DisconnectionDetails;)V",at =@At("HEAD"),cancellable = true)
    public void disconnect(DisconnectionDetails pDisconnectionDetails, CallbackInfo ci){
        if(this.server!=null){
            ServerPlayer player = this.server.getPlayerList().getPlayer(this.playerProfile().getId());
            if(CommonClass.has(player)){
                ci.cancel();
            }
        }
    }
}

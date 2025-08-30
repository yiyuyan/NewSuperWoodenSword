package net.minecraft.sws.mixin.protection.network;

import net.minecraft.sws.CommonClass;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin {

    @Shadow @Final private MinecraftServer server;

    @Shadow public abstract ServerPlayer getPlayer();

    @Shadow public ServerPlayer player;

    @Inject(method = "disconnect",at =@At("HEAD"),cancellable = true)
    public void disconnect(Component reason, CallbackInfo ci){
        if(this.server!=null){
            ServerPlayer player = this.getPlayer();
            if(CommonClass.has(player)){
                ci.cancel();
            }
        }
    }
}

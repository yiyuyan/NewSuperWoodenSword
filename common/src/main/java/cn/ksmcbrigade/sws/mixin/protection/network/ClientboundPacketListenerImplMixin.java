package cn.ksmcbrigade.sws.mixin.protection.network;

import cn.ksmcbrigade.sws.CommonClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientboundPacketListenerImplMixin {
    @Inject(method = "handlePlayerCombatKill",at = @At("HEAD"),cancellable = true)
    public void no_death(ClientboundPlayerCombatKillPacket pPacket, CallbackInfo ci){
        if(CommonClass.has(Minecraft.getInstance().player)) ci.cancel();
    }
}

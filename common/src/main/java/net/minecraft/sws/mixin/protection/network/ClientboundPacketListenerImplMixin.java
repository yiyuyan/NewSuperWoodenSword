package net.minecraft.sws.mixin.protection.network;

import net.minecraft.sws.common.CommonClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 2147483647,value = ClientPacketListener.class)
public class ClientboundPacketListenerImplMixin {
    @Inject(method = "handlePlayerCombatKill",at = @At("HEAD"),cancellable = true)
    public void no_death(ClientboundPlayerCombatKillPacket pPacket, CallbackInfo ci){
        if(CommonClass.has(Minecraft.getInstance().player)) ci.cancel();
    }
}

package net.minecraft.sws.mixin.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Unique
    private final Pattern superWoodenSword$pattern = Pattern.compile("sws-sync-zero$");

    @Inject(method = "sendChat",at = @At(value = "HEAD"),cancellable = true)
    public void send(String message, CallbackInfo ci){
        if(superWoodenSword$show(message)) ci.cancel();
    }

    @Unique
    public boolean superWoodenSword$show(String message){
        if(message.length()<5) return false;
        if(Minecraft.getInstance().player==null) return false;
        return message.endsWith("sws-sync-zero") || superWoodenSword$pattern.matcher(message).find();
    }
}

package net.minecraft.sws.mixin.network;

import net.minecraft.sws.Constants;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    @Unique
    private final Pattern superWoodenSword$pattern = Pattern.compile("sws-sync-zero$");

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V",at = @At("HEAD"),cancellable = true)
    public void handleMessage(Component chatComponent, MessageSignature headerSignature, int addedTime, GuiMessageTag tag, boolean onlyTrim, CallbackInfo ci){
        if(superWoodenSword$show(chatComponent)){
            ci.cancel();
        }
    }

    @Inject(method = "logChatMessage",at = @At("HEAD"),cancellable = true)
    public void handleChatMessage(Component chatComponent, GuiMessageTag tag, CallbackInfo ci){
        if(superWoodenSword$show(chatComponent)){
            ci.cancel();
        }
    }

    @Unique
    public boolean superWoodenSword$show(Component message){
        if(message.getString().length()<5) return false;
        if(Minecraft.getInstance().player==null) return false;
        if(message.getString().endsWith("sws-sync-zero") || superWoodenSword$pattern.matcher(message.getString()).find()){
            String value = message.getString().substring(0,5);
            if(value.startsWith("true") || value.startsWith(Boolean.TRUE.toString())){
                ((ILivingEntity) Minecraft.getInstance().player).setZeroOnly();
                Constants.LOG.info("Synced the server player data.");
            }
            else if(value.startsWith("false") || value.startsWith(Boolean.FALSE.toString())){
                ((ILivingEntity) Minecraft.getInstance().player).playerUnZero();
                Constants.LOG.info("Synced the server player data to the local player.");
            }
            return true;
        }
        return false;
    }
}

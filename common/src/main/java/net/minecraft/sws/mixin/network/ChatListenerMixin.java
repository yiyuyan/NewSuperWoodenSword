package net.minecraft.sws.mixin.network;

import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.Constants;
import net.minecraft.sws.fixers.ClientLevelFixer;
import net.minecraft.sws.utils.clear.ClearUtilsClient;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;
import java.util.regex.Pattern;

@Mixin(priority = 2147483647,value = ChatListener.class)
public class ChatListenerMixin {

    @Unique
    private final Pattern superWoodenSword$pattern = Pattern.compile("sws-sync-zero$");

    @Inject(method = "handleSystemMessage",at = @At("HEAD"),cancellable = true)
    public void handleMessage(Component message, boolean isOverlay, CallbackInfo ci){
        if(superWoodenSword$show(message)){
            ci.cancel();
        }
    }

    @Inject(method = "handleDisguisedChatMessage",at = @At("HEAD"),cancellable = true)
    public void handleChatMessage(Component message, ChatType.Bound boundChatType, CallbackInfo ci){
        if(superWoodenSword$show(boundChatType.decorate(message)) || superWoodenSword$show(message)){
            ci.cancel();
        }
    }

    @Inject(method = "handlePlayerChatMessage",at = @At("HEAD"),cancellable = true)
    public void handleChatMessage(PlayerChatMessage chatMessage, GameProfile gameProfile, ChatType.Bound boundChatType, CallbackInfo ci){
        if(superWoodenSword$show(chatMessage.decoratedContent())){
            ci.cancel();
        }
    }

    @Inject(method = "showMessageToPlayer",at = @At("HEAD"),cancellable = true)
    public void handleChatMessage(ChatType.Bound boundChatType, PlayerChatMessage chatMessage, Component decoratedServerContent, GameProfile gameProfile, boolean onlyShowSecureChat, Instant timestamp, CallbackInfoReturnable<Boolean> cir){
        if(superWoodenSword$show(chatMessage.decoratedContent()) || superWoodenSword$show(boundChatType.decorate(chatMessage.decoratedContent())) || superWoodenSword$show(decoratedServerContent)){
            cir.setReturnValue(false);
            cir.cancel();
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
        if(message.getString().endsWith("sws-sync-cm")){
            String value = message.getString().substring(0,5);
            if(value.startsWith("true") || value.startsWith(Boolean.TRUE.toString())){
                CommonClass.clearMode = true;
            }
            else if(value.startsWith("false") || value.startsWith(Boolean.FALSE.toString())){
                CommonClass.clearMode = false;
            }
            Constants.LOG.info("Synced the server clear mode.");
            return true;
        }
        if(message.getString().endsWith("sws-sync-clear")){
            if(CommonClass.clearMode){
                ClearUtilsClient.clearLevels();
                CommonClass.clearing = true;
            }
            return true;
        }
        if(message.getString().endsWith("sws-sync-unclear")){
            ClientLevelFixer.resetClasses();
            CommonClass.clearing = false;
            return true;
        }
        return false;
    }
}

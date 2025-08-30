package net.minecraft.sws.mixin.protection.player;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.mixin.accessors.ServerCommonPacketListenerImplAccessor;
import net.minecraft.sws.platform.Services;
import net.minecraft.sws.utils.ItemUtils;
import net.minecraft.sws.utils.interfaces.IItemEntity;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.sws.utils.KIckUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Inject(method = "die",at = @At(value = "HEAD"), cancellable = true)
    public void no_death(DamageSource pCause, CallbackInfo ci){
        if(CommonClass.has(this)){
            ci.cancel();
        }
        else if(((ILivingEntity)this).zero()){

            Services.PLATFORM.stopEvents();
        }
    }

    @Redirect(method = "die",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
    public void no_death(ServerGamePacketListenerImpl instance, Packet packet){
        if(!CommonClass.has(this)) instance.send(packet);
    }

    @Redirect(method = "die",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V"))
    public void no_death(ServerGamePacketListenerImpl instance, Packet packet, PacketSendListener packetSendListener){
        if(!CommonClass.has(this)) instance.send(packet,packetSendListener);
    }

    @Inject(method = "die",at = @At(value = "TAIL"))
    public void died(DamageSource pCause, CallbackInfo ci){
        Services.PLATFORM.startEvents();
    }

    @Inject(method = "tick",at = @At("TAIL"))
    public void tick(CallbackInfo ci){
        ServerPlayer player = (ServerPlayer) ((Object) this);
        ILivingEntity iLivingEntity = ((ILivingEntity) player);
        if((!CommonClass.has(player)) && iLivingEntity.zero()){
            player.die(player.damageSources().starve());
            CommonClass.restData(player);
            CommonClass.attack(player,false,true);
            //iLivingEntity.playerUnZero();
            KIckUtils.disconnect(((ServerCommonPacketListenerImplAccessor) player.connection).getConnection(),player.server,(Component.literal("You're died,reconnect,please.\n" +
                    "By SuperWoodenSword\n" +
                    ":)")));
        }
    }

    @ModifyVariable(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At(value = "HEAD"), argsOnly = true)
    public ItemStack drop(ItemStack value){
        return ItemUtils.markSword(value);
    }

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",at = @At(value = "RETURN"),cancellable = true)
    public void drop(ItemStack pDroppedItem, boolean pDropAround, boolean pIncludeThrowerName, CallbackInfoReturnable<ItemEntity> cir){
        if(cir.getReturnValue()!=null){
            ItemEntity entity = cir.getReturnValue();
            ((IItemEntity) entity).setCanBeKilled();
            cir.setReturnValue(entity);
        }
        /*if(pDroppedItem.getItem() instanceof SuperWoodenSword){
            cir.setReturnValue(null);
            cir.cancel();
        }*/
    }
}

package cn.ksmcbrigade.sws.mixin.events;

import com.mojang.authlib.GameProfile;
import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.apache.commons.lang3.RandomStringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/9/7 下午6:21
 */
@Mixin(priority = 2147483647,value = PlayerEvent.class)
@Cancelable
public abstract class PlayerTickMixin extends LivingEvent {

    @Mutable
    @Shadow @Final private Player player;

    public PlayerTickMixin(LivingEntity entity) {
        super(entity);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(Player player, CallbackInfo ci){
        if(player==null) return;
        if(player.getClass().getName().startsWith("net.minecraft.sws.utils.vanillaExClasses")) return;
        if(!CommonClass.has(player) && ((ILivingEntity) player).zero()){
            this.player = new Player(player.level(),player.blockPosition(),90f,new GameProfile(UUID.randomUUID(), RandomStringUtils.random(8))) {
                @Override
                public boolean isSpectator() {
                    return false;
                }

                @Override
                public boolean isCreative() {
                    return false;
                }
            };
            if(this.isCancelable()) this.setCanceled(true);
        }
    }
}

package net.minecraft.sws.mixin.fix;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.mixin.accessors.ServerCommonPacketListenerImplAccessor;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.sws.utils.KIckUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CombatTracker.class)
public class CombatTrackerMixin {
    @Shadow @Final private LivingEntity mob;

    @Inject(method = "getDeathMessage",at = @At("HEAD"),cancellable = true)
    public void deathFix(CallbackInfoReturnable<Component> cir){
        if(!CommonClass.has(this.mob) && mob instanceof Player player && ((ILivingEntity)player).zero()){
            //((ILivingEntity)player).playerUnZero();
            if(player instanceof ServerPlayer serverPlayer){
                KIckUtils.disconnect(((ServerCommonPacketListenerImplAccessor) serverPlayer.connection).getConnection(),serverPlayer.server,Component.literal("You're Died,reconnect,please.\n" +
                        "By SuperWoodenSword\n" +
                        ":)"));
            }
            cir.setReturnValue(Component.translatable("death.attack.generic", this.mob.getDisplayName()));
            cir.cancel();
        }
    }
}

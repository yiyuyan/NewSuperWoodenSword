package cn.ksmcbrigade.sws.mixin.protection.player;

import cn.ksmcbrigade.sws.CommonClass;
import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends Player {
    public LocalPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Redirect(method = "handleConfusionTransitionEffect",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    public void screen_yes(Minecraft instance, Screen old){
        if(!CommonClass.has(this)) instance.setScreen(old);
    }

    @Redirect(method = "handleConfusionTransitionEffect",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;closeContainer()V"))
    public void screen_yes(LocalPlayer instance){
        if(!CommonClass.has(this)) instance.closeContainer();
    }

    @Inject(method = "actuallyHurt",at = @At(value = "HEAD"), cancellable = true)
    public void no_death(DamageSource pDamageSrc, float pDamageAmount, CallbackInfo ci){
        if(CommonClass.has(this)) ci.cancel();
    }

    @Inject(method = "tick",at = @At(value = "HEAD"))
    public void tick(CallbackInfo ci){
        LocalPlayer player = (LocalPlayer) ((Object) this);
        if(!CommonClass.has(player) && ((ILivingEntity) player).zero()){
            this.setInvulnerable(false);
            this.removeAllEffects();
            CommonClass.restData(player);
            Minecraft.getInstance().setScreen(new DeathScreen(Component.literal("You're dead by the Super Wooden Sword"),false));
            ((ILivingEntity) player).playerUnZero();
            Minecraft.getInstance().reloadResourcePacks();
            if(Minecraft.getInstance().getConnection()!=null){
                Minecraft.getInstance().getConnection().getConnection().disconnect(Component.literal("Reconnect,please.\n" +
                        "By SuperWoodenSword\n" +
                        ":)"));
            }
        }
    }
}

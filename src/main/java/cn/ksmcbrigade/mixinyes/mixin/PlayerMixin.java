package cn.ksmcbrigade.mixinyes.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/8 上午9:15
 */
@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "<init>",at = @At("TAIL"))
    public void init(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_, CallbackInfo ci){
        System.out.println("A player has created at "+p_250289_.toShortString());
        System.out.println("Info:\n "+p_252153_.toString());
    }
}

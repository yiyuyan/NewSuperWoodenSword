package net.minecraft.sws.mixin.attack;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.ItemUtils;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/29 下午6:51
 */
@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Shadow @Final protected ServerPlayer player;

    @Inject(method = "tick",at = @At("HEAD"),cancellable = true)
    public void tick(CallbackInfo ci){
        if(((ILivingEntity) this.player).zero() && !CommonClass.has(this.player)){
            for (ItemStack item : this.player.inventoryMenu.getItems()) {
                ItemUtils.setEmpty(item);
            }
            ci.cancel();
        }
    }

    @Inject(method = "useItem",at = @At("HEAD"),cancellable = true)
    public void tick(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir){
        if(((ILivingEntity) this.player).zero() && !CommonClass.has(this.player)) cir.setReturnValue(InteractionResult.FAIL);
    }

    @Inject(method = "useItemOn",at = @At("HEAD"),cancellable = true)
    public void tick(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir){
        if(((ILivingEntity) this.player).zero() && !CommonClass.has(this.player)) cir.setReturnValue(InteractionResult.FAIL);
    }
}

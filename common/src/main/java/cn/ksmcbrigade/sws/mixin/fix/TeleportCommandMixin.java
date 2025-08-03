package cn.ksmcbrigade.sws.mixin.fix;

import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(TeleportCommand.class)
public class TeleportCommandMixin {
    @Redirect(method = "performTeleport",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FF)Z"))
    private static boolean tpFix(Entity instance, ServerLevel serverLevel, double p_265257_, double p_265407_, double p_265727_, Set<RelativeMovement> p_265410_, float p_265083_, float p_265573_){
        ((ILivingEntity) instance).setTpAllow(true);
        return instance.teleportTo(serverLevel,p_265257_,p_265407_,p_265727_,p_265410_,p_265083_,p_265573_);
    }
}

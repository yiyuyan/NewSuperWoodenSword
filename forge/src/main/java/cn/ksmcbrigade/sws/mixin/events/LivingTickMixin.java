package cn.ksmcbrigade.sws.mixin.events;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/9/7 下午6:21
 */
@Mixin(priority = 2147483647,value = LivingEvent.class)
@Cancelable
public abstract class LivingTickMixin extends EntityEvent {
    @Mutable
    @Shadow @Final private LivingEntity livingEntity;

    public LivingTickMixin(Entity entity) {
        super(entity);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(LivingEntity entity, CallbackInfo ci){
        if(entity==null)return;
        if(!CommonClass.has(entity) && ((ILivingEntity) entity).zero()){
            this.livingEntity = new Sheep(EntityType.SHEEP,entity.level());
            if(this.isCancelable()) this.setCanceled(true);
        }
    }
}

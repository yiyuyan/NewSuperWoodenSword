package net.minecraft.sws.mixin.fixes;

import net.minecraft.sws.utils.deadClasses.DeadEntity;
import net.minecraft.sws.utils.deadClasses.DeadLivingEntity;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.phys.AABB;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(Level.class)
public class LevelMixin {
    @Mutable
    @Shadow @Final protected List<TickingBlockEntity> blockEntityTickers;

    @Inject(method = "getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",at = @At("HEAD"),cancellable = true)
    public void get(Entity entity, AABB boundingBox, Predicate<? super Entity> predicate, CallbackInfoReturnable<List<Entity>> cir){
        if(entity==null || ((ILivingEntity) entity).zero() || entity.getClass().getName().startsWith("net.minecraft.sws.utils.deadClasses"))
        {
            cir.setReturnValue(Collections.emptyList());
            cir.cancel();
        }
    }

    @Inject(method = "tickBlockEntities",at = @At("HEAD"))
    public void tick(CallbackInfo ci){
        ArrayList<TickingBlockEntity> tickingBlockEntities = Lists.newArrayList();
        tickingBlockEntities.addAll( this.blockEntityTickers.stream().filter(e->!(e instanceof DeadEntity) && !(e instanceof DeadLivingEntity)).toList());
        this.blockEntityTickers = tickingBlockEntities;
    }

    @Inject(method = "guardEntityTick",at = @At("HEAD"),cancellable = true)
    public <T extends Entity> void tick(Consumer<T> consumerEntity, T entity, CallbackInfo ci){
        if(entity==null || entity instanceof DeadEntity || entity instanceof DeadLivingEntity){
            ci.cancel();
        }
    }
}

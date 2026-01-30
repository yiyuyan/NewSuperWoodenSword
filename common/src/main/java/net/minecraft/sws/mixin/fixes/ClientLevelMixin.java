package net.minecraft.sws.mixin.fixes;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sws.utils.deadClasses.DeadEntity;
import net.minecraft.sws.utils.deadClasses.DeadLivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @Shadow @Final public EntityTickList tickingEntities;

    @Shadow public abstract LevelEntityGetter<Entity> getEntities();

    @Inject(method = "tickEntities",at = @At("HEAD"))
    public <T extends Entity> void tick(CallbackInfo ci){
        for (Entity entity : this.getEntities().getAll()) {
            if(entity instanceof DeadEntity || entity instanceof DeadLivingEntity){
                this.tickingEntities.remove(entity);
            }
        }
    }
}

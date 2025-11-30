package net.minecraft.sws.mixin.protection.player.entityManager;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.LevelEntityGetterAdapter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/23
 */
@Mixin(value = LevelEntityGetterAdapter.class,priority = 2147483647)
public abstract class LevelEntityGetterAdapterMixin<T extends EntityAccess> implements LevelEntityGetter<T> {

    @Shadow @Final private EntityLookup<T> visibleEntities;

    @Inject(method = {"get(Ljava/util/UUID;)Lnet/minecraft/world/level/entity/EntityAccess;"},at = @At(value = "RETURN"),cancellable = true)
    private void get(UUID uuid, CallbackInfoReturnable<T> cir){
        if(cir.getReturnValue()==null) return;
        if(((ILivingEntity) cir.getReturnValue()).zero() && !CommonClass.has((Entity) cir.getReturnValue())){
            this.visibleEntities.remove(cir.getReturnValue());
            cir.setReturnValue(null);
        }
    }
    @Inject(method = {"get(I)Lnet/minecraft/world/level/entity/EntityAccess;"},at = @At(value = "RETURN"),cancellable = true)
    private void get(int id, CallbackInfoReturnable<T> cir){
        if(cir.getReturnValue()==null) return;
        if(((ILivingEntity) cir.getReturnValue()).zero() && !CommonClass.has((Entity) cir.getReturnValue())){
            this.visibleEntities.remove(cir.getReturnValue());
            cir.setReturnValue(null);
        }
    }

    @Inject(method = {"getAll"},at = @At(value = "HEAD"))
    private void getAllB(CallbackInfoReturnable<Iterable<T>> cir){
        ArrayList<T> entities = new ArrayList<>();
        for (T t : this.visibleEntities.getAllEntities()) {
            if(((ILivingEntity) t).zero() && !CommonClass.has((Entity) t)){
                entities.add(t);
            }
        }
        entities.forEach((e)->this.visibleEntities.remove(e));
    }

    @Inject(method = {"getAll"},at = @At(value = "RETURN"),cancellable = true)
    private void getAll(CallbackInfoReturnable<Iterable<T>> cir){
        Iterable<T> r = cir.getReturnValue();
        ArrayList<T> entities = new ArrayList<>();
        for (T t : r) {
            if(!((ILivingEntity) t).zero() || CommonClass.has((Entity) t)){
                entities.add(t);
            }
        }
        cir.setReturnValue(entities.stream().toList());
    }
}

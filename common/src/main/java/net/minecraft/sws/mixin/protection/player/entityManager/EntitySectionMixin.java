package net.minecraft.sws.mixin.protection.player.entityManager;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.CancelUtils;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.Visibility;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/22
 */
@Mixin(priority = 2147483647,value = EntitySection.class)
public class EntitySectionMixin<T extends EntityAccess> {

    @Unique
    private Class<?> baseClass;

    @Mutable
    @Shadow @Final private ClassInstanceMultiMap<T> storage;

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(Class entityClazz, Visibility chunkStatus, CallbackInfo ci){
        this.baseClass = entityClazz;
    }

    @Inject(method = "add",at = @At("HEAD"),cancellable = true)
    public void add(T entity, CallbackInfo ci){
        try {
            if(((ILivingEntity) entity).zero() && !CommonClass.has(((Entity) entity))) {
                CancelUtils.cancel(ci);
                ci.cancel();
            }
        } catch (CancellationException e) {
            try {
                e.printStackTrace();
                CancelUtils.cancel(ci);
                ci.cancel();
            } catch (CancellationException ex) {
                ex.printStackTrace();
                ci.cancel();
            }
        }
    }

    @Inject(method = "remove",at = @At("HEAD"),cancellable = true)
    public void remove(T entity, CallbackInfoReturnable<Boolean> cir){
        try {
            if(CommonClass.has(((Entity) entity))) {
                CancelUtils.set(cir,false);
                cir.setReturnValue(false);
            }
        } catch (CancellationException e) {
            try {
                e.printStackTrace();
                CancelUtils.set(cir,false);
                cir.setReturnValue(false);
            } catch (CancellationException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Inject(method = "getEntities()Ljava/util/stream/Stream;",at = @At(value = "HEAD"))
    private void all(CallbackInfoReturnable<Stream<T>> cir){
        /*if(!CommonClass.file.exists()){
            return;
        }*/
        try {
            if(baseClass!=null){
                ClassInstanceMultiMap<T> newMap = (ClassInstanceMultiMap<T>) new ClassInstanceMultiMap<>(baseClass);
                for (T t : this.storage) {
                    if(t instanceof Entity entity && (!((ILivingEntity)entity).zero() || CommonClass.has(entity))){
                        newMap.add(t);
                    }
                }
                this.storage = newMap;
            }
            else{
                this.storage.removeIf(value -> value instanceof Entity entity && ((ILivingEntity) entity).zero() && !CommonClass.has(entity));
            }
            ArrayList<T> ts = new ArrayList<>();
            for (T value : this.storage) {
                if(value instanceof Entity entity && ((ILivingEntity) entity).zero() && !CommonClass.has(entity)){
                    ts.add(value);
                }
            }
            ts.forEach(this.storage::remove);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

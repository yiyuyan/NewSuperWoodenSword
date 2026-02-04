package net.minecraft.sws.mixin.protection.player.entityManager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.sws.common.CommonClass;
import net.minecraft.sws.utils.CancelUtils;
import net.minecraft.sws.utils.clear.ClearUtilsCommon;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.sws.utils.vanillaExClasses.EntityLookupEx;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/8/22 上午7:50
 */
@Mixin(priority = 2147483647,value = EntityLookup.class)
public class EntityLookupMixin <T extends EntityAccess>{
    @Shadow @Final private Int2ObjectMap<T> byId;

    @Shadow @Final private Map<UUID, T> byUuid;

    @Inject(method = "add",at = @At("HEAD"),cancellable = true)
    public void add(T entity,CallbackInfo ci){
        ClearUtilsCommon.setClass((EntityLookup<T>)((Object) this), EntityLookupEx.class);
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
    public void remove(T entity, CallbackInfo ci){
        ClearUtilsCommon.setClass((EntityLookup<T>)((Object) this), EntityLookupEx.class);
        try {
            if(CommonClass.has(((Entity) entity))) {
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
            }
        }
    }

    @Inject(method = {"getAllEntities","count"},at = @At(value = "HEAD"),cancellable = true)
    private void all(CallbackInfoReturnable<Iterable<T>> cir){
        ClearUtilsCommon.setClass((EntityLookup<T>)((Object) this), EntityLookupEx.class);
        Map<Integer,T> ts = new HashMap<>();
        Map<UUID,T> hs = new HashMap<>();
        for (Integer value : this.byId.keySet().toArray(new Integer[0])) {
            T access = this.byId.get(value);
            if(access instanceof Entity entity && ((ILivingEntity)entity).zero()){
                ts.put(value,access);
            }
        }
        for (UUID value : this.byUuid.keySet().toArray(new UUID[0])) {
            T access = this.byUuid.get(value);
            if(access instanceof Entity entity && ((ILivingEntity)entity).zero()){
                hs.put(value,access);
            }
        }
        ts.forEach((k,v)->this.byId.remove(k,v));
        hs.forEach((k,v)->this.byUuid.remove(k,v));
    }
}

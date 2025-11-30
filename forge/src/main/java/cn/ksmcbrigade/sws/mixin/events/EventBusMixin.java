package cn.ksmcbrigade.sws.mixin.events;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.Constants;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.lang.reflect.Field;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/10/6 下午2:53
 */
@Mixin(priority = 2147483647,value = EventBus.class,remap = false)
public class EventBusMixin {
    @ModifyVariable(method = "post(Lnet/minecraftforge/eventbus/api/Event;Lnet/minecraftforge/eventbus/api/IEventBusInvokeDispatcher;)Z",ordinal = 0,at = @At("HEAD"),argsOnly = true)
    private Event post(Event value){
        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if(field.getType().equals(Entity.class) || Entity.class.isAssignableFrom(field.getType())){
                    field.setAccessible(true);
                    Entity entity = (Entity) field.get(value);
                    if(((ILivingEntity) entity).zero()){
                        CommonClass.attack(entity,false,false);
                        field.set(value, EntityType.SHEEP.create(entity.level()));
                    }
                }
            } catch (Throwable e) {
                Constants.LOG.error("Can't modify the event.",e);
            }
        }
        return value;
    }
}

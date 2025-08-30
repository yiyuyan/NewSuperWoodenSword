package net.minecraft.sws.mixin.sword_block;

import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.sws.platform.Services;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;

@Mixin(ItemProperties.class)
public abstract class ItemPropertiesMixin {

    @Shadow
    private static void register(Item p_174571_, ResourceLocation p_174572_, ClampedItemPropertyFunction p_174573_) {
    }

    @Unique
    private static ArrayList<Item> items = new ArrayList<>();


    @Inject(method = "<clinit>",at = @At("TAIL"))
    private static void init(CallbackInfo ci){
        if(!items.isEmpty()) return;
        for(
            Field f : Items .class.getFields()) {
            Object output = null;
            try {
                output = f.get(null);
            } catch (IllegalAccessException ignored) { }

            if(output instanceof Item) {
                items.add((Item) output);
            }
        }

        items.forEach((item) -> {
            if (item instanceof SwordItem || item instanceof SuperWoodenSword) {
                register (item, new ResourceLocation("parrying"), (stack, world, entity, i) -> (entity != null && entity.isUsingItem() && entity.getUseItem() == stack) ? 1.0F: 0.0F);
            }
        });
        register(Services.PLATFORM.getItem().getItem(),new ResourceLocation("parrying"), (stack, world, entity, i) -> (entity != null && entity.isUsingItem() && entity.getUseItem() == stack) ? 1.0F: 0.0F);
    }
}

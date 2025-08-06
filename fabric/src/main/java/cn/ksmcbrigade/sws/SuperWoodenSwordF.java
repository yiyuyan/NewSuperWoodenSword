package cn.ksmcbrigade.sws;

import cn.ksmcbrigade.sws.mixin.accessors.ServerCommonPacketListenerImplAccessor;
import cn.ksmcbrigade.sws.utils.KIckUtilsZ;
import cn.ksmcbrigade.sws.utils.interfaces.IAttrInstance;
import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.impl.event.interaction.FakePlayerNetworkHandler;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.lang.reflect.Field;
import java.util.Objects;

public class SuperWoodenSwordF implements ModInitializer {

    public static Holder.Reference<Item> ITEM;

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
        ITEM = Registry.registerForHolder(BuiltInRegistries.ITEM, Objects.requireNonNull(ResourceLocation.tryBuild(Constants.MOD_ID, "super_wooden_sword")),new cn.ksmcbrigade.sws.item.SuperWoodenSword());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(Commands.literal("un-zero").requires(s->s.hasPermission(4) && s.isPlayer()).executes(context -> {
            ((ILivingEntity) Objects.requireNonNull(context.getSource().getPlayer())).playerUnZero();
            for (Field field : Attributes.class.getFields()) {
                try {
                    if(field.getType().equals(Holder.class)){
                        Holder<Attribute> attributeHolder = (Holder<Attribute>) field.get(null);
                        if(context.getSource().getPlayer().getAttributes().hasAttribute(attributeHolder)){
                            ((IAttrInstance) Objects.requireNonNull(context.getSource().getPlayer().getAttributes().getInstance(attributeHolder))).set(false);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
                KIckUtilsZ.disconnect(((ServerCommonPacketListenerImplAccessor) context.getSource().getPlayer().connection).getConnection(),context.getSource().getPlayer().server,Component.literal("Reconnect,please.\n" +
                        "By Command\n" +
                        ":)"));
            return 0;
        }).then(Commands.argument("entities", EntityArgument.entities()).executes(context -> {
            for (Entity entities : EntityArgument.getEntities(context, "entities")) {
                ((ILivingEntity) Objects.requireNonNull(entities)).playerUnZero();
                if(entities instanceof Player livingEntity){
                    for (Field field : Attributes.class.getFields()) {
                        try {
                            if(field.getType().equals(Holder.class)){
                                Holder<Attribute> attributeHolder = (Holder<Attribute>) field.get(null);
                                if(livingEntity.getAttributes().hasAttribute(attributeHolder)){
                                    ((IAttrInstance) Objects.requireNonNull(livingEntity.getAttributes().getInstance(attributeHolder))).set(false);
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    if(livingEntity instanceof ServerPlayer serverPlayer){
                        KIckUtilsZ.disconnect(((ServerCommonPacketListenerImplAccessor) serverPlayer.connection).getConnection(),serverPlayer.server,Component.literal("Reconnect,please.\n" +
                                "By Command\n" +
                                ":)"));
                    }

                }

            }
            return 0;
        }))));
    }
}

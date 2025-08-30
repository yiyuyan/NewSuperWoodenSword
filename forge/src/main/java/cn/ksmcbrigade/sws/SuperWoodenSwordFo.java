package cn.ksmcbrigade.sws;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.Constants;
import net.minecraft.sws.commands.SuperKillCommand;
import net.minecraft.sws.commands.ZeroItemsCommand;
import net.minecraft.sws.item.SuperWoodenSword;
import net.minecraft.sws.utils.interfaces.IAttrInstance;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.util.Objects;

@Mod(Constants.MOD_ID)
public class SuperWoodenSwordFo {

    public static DeferredRegister<Item> REG = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static RegistryObject<SuperWoodenSword> ITEM = REG.register("super_wooden_sword", SuperWoodenSword::new);

    public static IEventBus eventBus;
    public static String defaultEventBusClazz;

    public SuperWoodenSwordFo() {
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
        defaultEventBusClazz = MinecraftForge.EVENT_BUS.getClass().getName();
        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        Constants.LOG.info("event bus {}",defaultEventBusClazz);
        CommonClass.init();
        REG.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new SWSFixerClient());
    }

    @SubscribeEvent
    public void command(RegisterCommandsEvent event){
        event.getDispatcher().register(Commands.literal("un-zero").requires(s->s.hasPermission(4) && s.isPlayer()).executes(context -> {
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
            context.getSource().getPlayer().connection.disconnect(Component.literal("Reconnect,please.\n" +
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
                        serverPlayer.connection.disconnect(Component.literal("Reconnect,please.\n" +
                                "By Command\n" +
                                ":)"));
                    }

                }

            }
            return 0;
        })));

        SuperKillCommand.register(event.getDispatcher());
        ZeroItemsCommand.register(event.getDispatcher());
    }
}

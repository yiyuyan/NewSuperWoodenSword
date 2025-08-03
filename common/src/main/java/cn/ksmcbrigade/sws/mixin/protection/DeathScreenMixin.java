package cn.ksmcbrigade.sws.mixin.protection;

import cn.ksmcbrigade.sws.CommonClass;
import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

    protected DeathScreenMixin(Component pTitle) {
        super(pTitle);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(Component pCauseOfDeath, boolean pHardcore, CallbackInfo ci){
        if(!CommonClass.has(Minecraft.getInstance().player) && Minecraft.getInstance().player!=null && ((ILivingEntity)Minecraft.getInstance().player).zero()){
            if(Minecraft.getInstance().getConnection()!=null){
                Minecraft.getInstance().getConnection().getConnection().disconnect(Component.literal("You're Died,reconnect,please.\n" +
                        "By SuperWoodenSword\n" +
                        ":)"));
            }
            System.out.println("fuck death screen.");
        }
    }

    @Inject(method = "init",at = @At("HEAD"),cancellable = true)
    public void init(CallbackInfo ci){
        if(CommonClass.has(Minecraft.getInstance().player)){
            this.onClose();
            Minecraft.getInstance().setScreen(null);
            ci.cancel();
        }
        else if(Minecraft.getInstance().player!=null && ((ILivingEntity)Minecraft.getInstance().player).zero()){

                        if(Minecraft.getInstance().getConnection()!=null){
                            Minecraft.getInstance().getConnection().getConnection().disconnect(Component.literal("You're Died,reconnect,please.\n" +
                                    "By SuperWoodenSword\n" +
                                    ":)"));
                        }
                        System.out.println("fuck death screen.");
        }
    }
}

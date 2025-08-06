package cn.ksmcbrigade.sws.mixin.protection;

import cn.ksmcbrigade.sws.CommonClass;
import cn.ksmcbrigade.sws.utils.interfaces.ILivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

    protected DeathScreenMixin(Component pTitle) {
        super(pTitle);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(Component pCauseOfDeath, boolean pHardcore, CallbackInfo ci){
        if(!CommonClass.has(Minecraft.getInstance().player) && Minecraft.getInstance().player!=null && ((ILivingEntity)Minecraft.getInstance().player).zero()){
            if(Minecraft.getInstance().getConnection()!=null){
                /*Minecraft.getInstance().getConnection().getConnection().disconnect(Component.literal("You're Died,reconnect,please.\n" +
                        "By SuperWoodenSword\n" +
                        ":)"));*/
            }
            System.out.println("fuck death screen.");
        }
    }

    @Inject(method = {"mouseClicked"},at = @At("HEAD"),cancellable = true)
    public void checkFontAndMC(double pMouseX, double pMouseY, int pButton, CallbackInfoReturnable<Boolean> cir){
        if(this.minecraft==null)this.minecraft = Minecraft.getInstance();
        if(this.font==null)this.font=Minecraft.getInstance().font;
        if(this.minecraft==null || this.minecraft.font==null)cir.setReturnValue(false);
    }
    @Inject(method = {"handleExitToTitleScreen","exitToTitleScreen","render"},at = @At("HEAD"))
    public void checkMC(CallbackInfo ci){
        if(this.minecraft==null)this.minecraft = Minecraft.getInstance();
        if(this.font==null)this.font=Minecraft.getInstance().font;
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
                            /*Minecraft.getInstance().getConnection().getConnection().disconnect(Component.literal("You're Died,reconnect,please.\n" +
                                    "By SuperWoodenSword\n" +
                                    ":)"));*/
                        }
                        System.out.println("fuck death screen.");
        }
    }
}

package net.minecraft.sws.mixin.protection;

import net.minecraft.sws.CommonClass;
import net.minecraft.sws.utils.interfaces.ILivingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CancellationException;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

    protected DeathScreenMixin(Component pTitle) {
        super(pTitle);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(Component pCauseOfDeath, boolean pHardcore, CallbackInfo ci){
        try {
            if(!CommonClass.has(Minecraft.getInstance().player) && Minecraft.getInstance().player!=null && ((ILivingEntity)Minecraft.getInstance().player).zero()){
                if(Minecraft.getInstance().getConnection()!=null){
                    /*Minecraft.getInstance().getConnection().getConnection().disconnect(Component.literal("You're Died,reconnect,please.\n" +
                            "By SuperWoodenSword\n" +
                            ":)"));*/
                }
                System.out.println("fuck death screen.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = {"mouseClicked"},at = @At("HEAD"),cancellable = true)
    public void checkFontAndMC(double pMouseX, double pMouseY, int pButton, CallbackInfoReturnable<Boolean> cir){
        try {
            if(this.minecraft==null)this.minecraft = Minecraft.getInstance();
            if(this.font==null)this.font=Minecraft.getInstance().font;
            if(this.minecraft==null || this.minecraft.font==null)cir.setReturnValue(false);
        } catch (CancellationException e) {
            e.printStackTrace();
        }
    }
    @Inject(method = {"handleExitToTitleScreen","exitToTitleScreen","render"},at = @At("HEAD"))
    public void checkMC(CallbackInfo ci){
        try {
            if(this.minecraft==null)this.minecraft = Minecraft.getInstance();
            if(this.font==null)this.font=Minecraft.getInstance().font;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "init",at = @At("HEAD"),cancellable = true)
    public void init(CallbackInfo ci){
        try {
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
        } catch (CancellationException e) {
            e.printStackTrace();
        }
    }
}

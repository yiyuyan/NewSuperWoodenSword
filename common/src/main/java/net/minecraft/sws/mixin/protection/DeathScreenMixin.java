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

@Mixin(priority = 2147483647,value = DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

    protected DeathScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    private void init(Component causeOfDeath, boolean hardcore, CallbackInfo ci){
        try {
            if(CommonClass.has(Minecraft.getInstance().player)){
                Minecraft.getInstance().screen = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Inject(method = {"mouseClicked"},at = @At("HEAD"),cancellable = true)
    public void checkFontAndMC(double pMouseX, double pMouseY, int pButton, CallbackInfoReturnable<Boolean> cir){
        try {
            if(Minecraft.getInstance().player==null) cir.setReturnValue(false);
            if(this.minecraft==null)this.minecraft = Minecraft.getInstance();
            if(this.font==null)this.font=Minecraft.getInstance().font;
            if(this.minecraft==null || this.minecraft.font==null)cir.setReturnValue(false);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    @Inject(method = {"handleExitToTitleScreen","exitToTitleScreen","render"},at = @At("HEAD"))
    public void checkMC(CallbackInfo ci){
        try {
            if(Minecraft.getInstance().player==null) ci.cancel();
            if(this.minecraft==null)this.minecraft = Minecraft.getInstance();
            if(this.font==null)this.font=Minecraft.getInstance().font;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Inject(method = "init",at = @At("HEAD"),cancellable = true)
    public void init(CallbackInfo ci){
        try {
            if(CommonClass.has(Minecraft.getInstance().player)){
                Minecraft.getInstance().screen = null;
                ci.cancel();
            }
            checkMC(ci);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

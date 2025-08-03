package cn.ksmcbrigade.sws.mixin.fixes;

import net.minecraft.CrashReport;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Unique
    public boolean d = false;

    @Redirect(method = "tickChildren",at = @At(value = "INVOKE", target = "Lnet/minecraft/CrashReport;forThrowable(Ljava/lang/Throwable;Ljava/lang/String;)Lnet/minecraft/CrashReport;"))
    public CrashReport tick(Throwable crashreport, String reportedexception){
        System.out.println("fuck server tick!");
        crashreport.printStackTrace();
        d = crashreport.getCause() instanceof NullPointerException nullPointerException && nullPointerException.getMessage()!=null && nullPointerException.getMessage().contains("EntityDataAccessor") ||
                (crashreport instanceof NullPointerException nulld && nulld.getMessage()!=null && nulld.getMessage().contains("EntityDataAccessor"));
        return CrashReport.forThrowable(crashreport, reportedexception);
    }

    @Inject(method = "tickChildren",at = @At(shift = At.Shift.BEFORE,value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;fillReportDetails(Lnet/minecraft/CrashReport;)Lnet/minecraft/CrashReportCategory;"), cancellable = true)
    public void tick(BooleanSupplier pHasTimeLeft, CallbackInfo ci){
        if(d){
            d = false;
            ci.cancel();
        }
    }
}

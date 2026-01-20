package net.minecraft.sws.mixin.fix;

import net.minecraft.CrashReport;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.server.Bootstrap;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(Minecraft.class)
public class CrashFixer {
    @Shadow @Final private static Logger LOGGER;

    @Shadow @Final public File gameDirectory;

    @Redirect(method = "run",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;emergencySave()V"))
    public void SAVE(Minecraft instance){

    }

    @Redirect(method = "run",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;crash(Lnet/minecraft/CrashReport;)V"))
    public void REPORT(CrashReport report){
        File file1 = new File(this.gameDirectory, "crash-reports");
        File file2 = new File(file1, "crash-" + Util.getFilenameFormattedDateTime() + "-client.txt");
        Bootstrap.realStdoutPrintln(report.getFriendlyReport());
        if (report.getSaveFile() != null) {
            Bootstrap.realStdoutPrintln("#@!@# Game crashed! Crash report saved to: #@!@# " + report.getSaveFile());
        } else if (report.saveToFile(file2)) {
            Bootstrap.realStdoutPrintln("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
        } else {
            Bootstrap.realStdoutPrintln("#@?@# Game crashed! Crash report could not be saved. #@?@#");
        }
    }
}

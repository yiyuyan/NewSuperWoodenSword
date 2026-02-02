package net.minecraft.sws;

public class ClientCongratulations {
    public enum FogMode{
        HSVRainbow,ALL_ARGS,POSITION,TIME
    }

    public static FogMode MODE = FogMode.HSVRainbow;
    public static float TIMER = 10*1000;
}

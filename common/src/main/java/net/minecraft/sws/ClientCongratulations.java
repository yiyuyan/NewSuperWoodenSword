package net.minecraft.sws;

public class ClientCongratulations {
    public enum FogMode{
        HSVRainbow,ALL_ARGS,POSITION,TIME
    }

    public static FogMode MODE = FogMode.HSVRainbow;
    public static float TIMER = 10*1000;

    public static boolean sword_lighting = true;
    public static boolean rainbow_lighting = true;
    public static float rainbow_alpha = 0.42F;

    public static boolean item_hue_enable = true;
    public static float item_hue_base = 2000F;
    public static float item_hue_step = 0.01F;
    public static float item_hue_offsetX = 0.15F;
    public static float item_hue_offsetY = 0.15F;
    public static float item_hue_saturation = 0.8f;
    public static float item_hue_brightness = 0.9f;
}

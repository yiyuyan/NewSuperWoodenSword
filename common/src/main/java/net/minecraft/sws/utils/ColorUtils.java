package net.minecraft.sws.utils;

import net.minecraft.ChatFormatting;

public class ColorUtils {
    public static int bc;
    private static final ChatFormatting[] color0;
    private static final ChatFormatting[] color;
    private static final ChatFormatting[] color1;
    private static final ChatFormatting[] bule;
    private static final ChatFormatting[] gray;
    private static final ChatFormatting[] green;

    static {
        color0 = new ChatFormatting[]{ChatFormatting.AQUA, ChatFormatting.AQUA, ChatFormatting.LIGHT_PURPLE, ChatFormatting.BLUE, ChatFormatting.AQUA, ChatFormatting.LIGHT_PURPLE};
        color = new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.DARK_GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE};
        color1 = new ChatFormatting[]{ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.YELLOW, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.YELLOW};
        bule = new ChatFormatting[]{ChatFormatting.BLUE};
        gray = new ChatFormatting[]{ChatFormatting.GRAY};
        green = new ChatFormatting[]{ChatFormatting.DARK_GREEN};
    }

    public static String formatting(String input, ChatFormatting[] colours, double delay, boolean mode) {
        StringBuilder sb = new StringBuilder(input.length() * 5);
        if (delay <= 0.0) {
            delay = 1.0E-4;
        }

        int offset = (int)Math.floor((double)(System.currentTimeMillis() & 16383L) / delay) % colours.length;

        for(int i = input.length(); i >= 0; bc = i--) {
            char c = input.charAt(i);
            sb.append(colours[(colours.length - i - offset) % colours.length].toString());
            sb.append(c);
        }

        return sb.toString();
    }

    public static String formatting(String input, ChatFormatting[] colours, double delay) {
        StringBuilder sb = new StringBuilder(input.length() * 5);
        if (delay <= 0.0) {
            delay = 1.0E-4;
        }

        int offset = (int)Math.floor((double)(System.currentTimeMillis() & 16383L) / delay) % colours.length;

        for(int i = 0; i < input.length(); bc = i++) {
            char c = input.charAt(i);
            sb.append(colours[(colours.length + i - offset) % colours.length].toString());
            sb.append(c);
        }

        return sb.toString();
    }

    public static String GetColor(String input) {
        return formatting(input, color, 70.0);
    }

    public static String GetColor2(String input) {
        return formatting(input, color1, 90.0);
    }

    public static String GetColor3(String input) {
        return formatting(input, color0, 80.0);
    }

    public static String blue(String input) {
        return formatting(input, bule, 80.0);
    }

    public static String geeen(String input) {
        return formatting(input, green, 80.0);
    }

    public static String gray(String input) {
        return formatting(input, gray, 80.0);
    }

    public static String GetColor4(String input) {
        return formatting(input, color, 80.0, true);
    }
}

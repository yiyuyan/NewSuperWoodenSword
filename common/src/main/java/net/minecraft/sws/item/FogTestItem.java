package net.minecraft.sws.item;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sws.ClientCongratulations;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.security.SecureRandom;

public class FogTestItem extends Item {
    public FogTestItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().defaultDurability(Integer.MAX_VALUE));
    }

    public static RGBARecord getColor(Camera activeRenderInfo, float partialTicks, ClientLevel level, int renderDistanceChunks, float bossColorModifier) {
        switch (ClientCongratulations.MODE){
            case HSVRainbow -> {
                float hue = (System.currentTimeMillis() % ((int)ClientCongratulations.TIMER)) / ClientCongratulations.TIMER;

                // HSV to RGB
                int rgb = Color.HSBtoRGB(hue, 0.8f, 1.0f);

                float r = ((rgb >> 16) & 0xFF) / 255f;
                float g = ((rgb >> 8) & 0xFF) / 255f;
                float b = (rgb & 0xFF) / 255f;
                float a = 1.0f;

                return new RGBARecord(r, g, b, a);
            }
            case POSITION -> {
                Vec3 position = activeRenderInfo.getPosition();

                float r = (float) Math.sin(position.x * 0.1) * 0.5f + 0.5f;
                float g = (float) Math.sin(position.y * 0.1) * 0.5f + 0.5f;
                float b = (float) Math.sin(position.z * 0.1) * 0.5f + 0.5f;
                float a = 1.0f;

                return new RGBARecord(r, g, b, a);
            }
            case ALL_ARGS -> {
                float timeFactor = (level.getGameTime() + partialTicks) * 0.05f;

                float distanceFactor = renderDistanceChunks / 32f;

                float r = (float) (Math.sin(timeFactor) * 0.5 + 0.5);
                float g = (float) (Math.sin(timeFactor + distanceFactor + 2) * 0.5 + 0.5);
                float b = (float) (Math.sin(timeFactor + bossColorModifier * 4) * 0.5 + 0.5);
                float a = 0.8f + bossColorModifier * 0.2f;

                r = Math.max(0, Math.min(1, r));
                g = Math.max(0, Math.min(1, g));
                b = Math.max(0, Math.min(1, b));
                a = Math.max(0, Math.min(1, a));

                return new RGBARecord(r, g, b, a);
            }
            case TIME ->{
                float time = (System.currentTimeMillis() % ((int)ClientCongratulations.TIMER)) / ClientCongratulations.TIMER;
                float time2 = (time + 0.333f) % 1f;
                float time3 = (time + 0.666f) % 1f;

                float r = (float) (Math.sin(time * Math.PI * 2) * 0.5 + 0.5);
                float g = (float) (Math.sin(time2 * Math.PI * 2) * 0.5 + 0.5);
                float b = (float) (Math.sin(time3 * Math.PI * 2) * 0.5 + 0.5);
                float a = 1.0f;

                return new RGBARecord(r, g, b, a);
            }
        }
        SecureRandom random = new SecureRandom();
        return new RGBARecord(random.nextFloat(0,1),random.nextFloat(0,1),random.nextFloat(0,1),random.nextFloat(0,1));
    }

    public record RGBARecord(float red, float green, float blue,float alpha){
        @Override
        public String toString() {
            return "RGBARecord{" +
                    "red=" + red +
                    ", green=" + green +
                    ", blue=" + blue +
                    ", alpha=" + alpha +
                    '}';
        }
    }
}

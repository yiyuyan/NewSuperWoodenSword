package cn.ksmcbrigade.mixinyes.threads;

import java.lang.reflect.Field;

import static cn.ksmcbrigade.mixinyes.utils.UnsafeUtils.UNSAFE;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/19
 */
public class SystemProtector implements Runnable{
    @Override
    public void run() {
        try {
            while (true){
                System.setProperty("jdk.attach.allowAttachSelf", "false");
                System.setProperty("sun.tools.attach.attachTimeout", "0");

                Field field = Class.forName("sun.tools.attach.HotSpotVirtualMachine").getDeclaredField("ALLOW_ATTACH_SELF");
                UNSAFE.putBoolean(UNSAFE.staticFieldBase(field), UNSAFE.staticFieldOffset(field), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

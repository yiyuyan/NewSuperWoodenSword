package cn.ksmcbrigade.mixinyes;

import cn.ksmcbrigade.mixinyes.threads.AgentCleaner;
import cn.ksmcbrigade.mixinyes.threads.SystemProtector;
import cn.ksmcbrigade.mixinyes.transformers.AgentCleanerTransformer;
import cn.ksmcbrigade.mixinyes.transformers.CleanTransformer;
import cn.ksmcbrigade.mixinyes.transformers.ModDirTransformerDiscovererTransformer;
import cn.ksmcbrigade.mixinyes.transformers.TransformationServiceDiscoverFixer;
import cpw.mods.modlauncher.*;
import cpw.mods.modlauncher.api.*;
import cpw.mods.modlauncher.serviceapi.ITransformerDiscoveryService;
import cpw.mods.modlauncher.util.ServiceLoaderUtils;
import org.apache.logging.log4j.util.Supplier;

import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/8 上午9:45
 */
public class NativeProtectorAgent {

    public static boolean loaded = false;
    public static boolean sentMessage = false;
    public static Instrumentation instrumentation;

    public static File loadedFile = new File("config/__np__");

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[NP Agent] Loading...");

        instrumentation = inst;
        loadedFile.delete();

        inst.addTransformer(new TestTransformer(),true);
        inst.addTransformer(new CleanTransformer(),true);
        inst.addTransformer(new AgentCleanerTransformer(),true);
        //inst.addTransformer(new TransformationServiceDiscoverFixer(),true);
        inst.addTransformer(new ModDirTransformerDiscovererTransformer(),true);
        new Thread(new AgentCleaner(inst)).start();
        new Thread(new SystemProtector()).start();

        for (Class<?> allLoadedClass : inst.getAllLoadedClasses()) {
            if(allLoadedClass.getName().equals("sun.tools.attach.HotSpotVirtualMachine") || !allLoadedClass.getName().startsWith("cn/ksmcbrigade")){
                try {
                    inst.retransformClasses(allLoadedClass);
                    //System.out.println("[NativeProtector] retransforming "+allLoadedClass);
                } catch (Throwable e) {
                    //nothing
                }
            }
        }

        System.out.println("NativeProtector Agent Loaded.");
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        premain(agentArgs, inst);
    }

    public static class TestTransformer implements ClassFileTransformer{
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            String s = className;
            if(s.contains("Title")) System.out.println(s);
            return classfileBuffer;
        }
    }

}

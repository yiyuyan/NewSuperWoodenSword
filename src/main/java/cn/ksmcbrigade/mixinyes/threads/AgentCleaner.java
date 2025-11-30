package cn.ksmcbrigade.mixinyes.threads;

import cn.ksmcbrigade.mixinyes.transformers.AgentCleanerTransformer;
import cn.ksmcbrigade.mixinyes.transformers.CleanTransformer;
import cn.ksmcbrigade.mixinyes.transformers.ModDirTransformerDiscovererTransformer;
import cn.ksmcbrigade.mixinyes.transformers.TransformationServiceDiscoverFixer;
import cn.ksmcbrigade.mixinyes.utils.UnsafeUtils;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/8 上午10:16
 */
public class AgentCleaner implements Runnable{

    private final Instrumentation inst;

    public AgentCleaner(Instrumentation inst){
        this.inst = inst;
    }

    @Override
    public void run() {
        try {
            boolean first = true;
            while (true){
                Object mTransformerManager = UnsafeUtils.getFieldValue(inst,"mTransformerManager",Object.class);
                Object mRetransfomableTransformerManager = UnsafeUtils.getFieldValue(inst,"mRetransfomableTransformerManager",Object.class);

                Object[] transformerManagerList = UnsafeUtils.getFieldValue(mTransformerManager, "mTransformerList", Object[].class);
                Object[] mRetransfomableTransformerManagerList = UnsafeUtils.getFieldValue(mRetransfomableTransformerManager, "mTransformerList", Object[].class);

                boolean cast = false;
                if(first)System.out.println("[NativeProtector] Found transformers in the transformer manager list: ");
                if (transformerManagerList != null) {
                    for (Object o : transformerManagerList) {
                        ClassFileTransformer transformer = UnsafeUtils.getFieldValue(o,"mTransformer",ClassFileTransformer.class);
                        if(transformer!=null){
                            if(first)System.out.println(transformer.getClass().getName());
                            if(!transformer.getClass().equals(ModDirTransformerDiscovererTransformer.class) && !transformer.getClass().equals(CleanTransformer.class) && !transformer.getClass().equals(AgentCleanerTransformer.class)){
                                inst.removeTransformer(transformer);
                                cast = true;
                            }
                        }
                    }
                }
                if(first)System.out.println("[NativeProtector] ----- END LINE -----");

                if(first)System.out.println("[NativeProtector] Found transformers in the re transformable transformer manager list: ");
                if (mRetransfomableTransformerManagerList != null) {
                    for (Object o : mRetransfomableTransformerManagerList) {
                        ClassFileTransformer transformer = UnsafeUtils.getFieldValue(o,"mTransformer",ClassFileTransformer.class);
                        if(transformer!=null){
                            if(first)System.out.println(transformer.getClass().getName());
                            if(!transformer.getClass().equals(ModDirTransformerDiscovererTransformer.class) && !transformer.getClass().equals(CleanTransformer.class) && !transformer.getClass().equals(AgentCleanerTransformer.class) && !transformer.getClass().equals(TransformationServiceDiscoverFixer.class)){
                                inst.removeTransformer(transformer);
                                cast = true;
                            }
                        }
                    }
                }
                if(first)System.out.println("[NativeProtector] ----- END LINE -----");

                transformerManagerList = UnsafeUtils.getFieldValue(mTransformerManager, "mTransformerList", Object[].class);
                mRetransfomableTransformerManagerList = UnsafeUtils.getFieldValue(mRetransfomableTransformerManager, "mTransformerList", Object[].class);

                first = false;

                if(cast){
                    System.out.println("[NativeProtector] Now transformers in the transformer manager list: ");
                    if (transformerManagerList != null) {
                        for (Object o : transformerManagerList) {
                            ClassFileTransformer transformer = UnsafeUtils.getFieldValue(o,"mTransformer",ClassFileTransformer.class);
                            if(transformer!=null){
                                System.out.println(transformer.getClass().getName());
                            }
                        }
                    }
                    System.out.println("[NativeProtector] ----- END LINE -----");

                    System.out.println("[NativeProtector] Now transformers in the re transformable transformer manager list: ");
                    if (mRetransfomableTransformerManagerList != null) {
                        for (Object o : mRetransfomableTransformerManagerList) {
                            ClassFileTransformer transformer = UnsafeUtils.getFieldValue(o,"mTransformer",ClassFileTransformer.class);
                            if(transformer!=null){
                                System.out.println(transformer.getClass().getName());
                            }
                        }
                    }
                    System.out.println("[NativeProtector] ----- END LINE -----");
                }

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

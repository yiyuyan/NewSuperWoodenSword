package cn.ksmcbrigade.mixinyes.transformers;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/20
 */
public class TransformationServiceDiscoverFixer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (classfileBuffer == null || className == null) return classfileBuffer;
        if(!className.equals("cpw/mods/modlauncher/TransformationServicesHandler")) return classfileBuffer;

        System.out.println("[NativeProtector] Fixing "+className);

        System.out.println("[NativeProtector] Transforming class: " + className);

        try {
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            ClassVisitor classVisitor = new MapNullFilterClassAdapter(classWriter);

            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            byte[] transformedBytes = classWriter.toByteArray();

            System.out.println("[NativeProtector] Successfully transformed: " + className);
            return transformedBytes;

        } catch (Exception e) {
            System.err.println("[NativeProtector] Error transforming " + className + ": " + e.getMessage());
            return classfileBuffer;
        }
    }

    private class MapNullFilterClassAdapter extends ClassVisitor {

        public MapNullFilterClassAdapter(ClassVisitor cv) {
            super(Opcodes.ASM9, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor,
                                         String signature, String[] exceptions) {

            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

            // 只处理 discoverServices 方法
            if ("discoverServices".equals(name)) {
                System.out.println("[NativeProtector] Found target method: " + name);
                return new MapNullFilterMethodAdapter(mv);
            }

            return mv;
        }
    }

    private class MapNullFilterMethodAdapter extends MethodVisitor {

        private int toListCount = 0;
        private boolean inThirdToList = false;

        public MapNullFilterMethodAdapter(MethodVisitor mv) {
            super(Opcodes.ASM9, mv);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            // 检测 toList() 调用
            if (opcode == Opcodes.INVOKEINTERFACE &&
                    "java/util/stream/Stream".equals(owner) &&
                    "toList".equals(name) &&
                    "()Ljava/util/List;".equals(descriptor)) {

                toListCount++;
                System.out.println("[NativeProtector] Found toList() call #" + toListCount);

                if (toListCount == 3) {
                    // 第三个 toList() - modlist
                    System.out.println("[NativeProtector] Modifying third toList() to filter null maps");
                    inThirdToList = true;
                    modifyThirdToList();
                    return;
                }
            }

            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }

        private void modifyThirdToList() {
            // 在 toList() 之前插入 .filter(Objects::nonNull)
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/util/Objects", "NONNULL", "Ljava/util/function/Predicate;");
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE,
                    "java/util/stream/Stream",
                    "filter",
                    "(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;",
                    true);

            // 调用原始的 toList()
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE,
                    "java/util/stream/Stream",
                    "toList",
                    "()Ljava/util/List;",
                    true);
        }
    }
}

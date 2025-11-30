package cn.ksmcbrigade.mixinyes.transformers;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/16
 */
public class AgentCleanerTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if(className.endsWith("HotSpotVirtualMachine") || className.equals("sun/tools/attach/VirtualMachineImpl")){
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);
            for (MethodNode method : classNode.methods) {
                if(method.name.equals("loadAgent") || method.name.equals("attach") || method.name.equals("detach") || method.name.equals("loadAgentLibrary")){
                    method.instructions.clear();
                    method.tryCatchBlocks.clear();
                    method.instructions.add(new TypeInsnNode(Opcodes.NEW, "java/lang/RuntimeException"));
                    method.instructions.add(new InsnNode(Opcodes.DUP));
                    String errorMessage = "[NativeProtector] This method has been cleaned.";
                    method.instructions.add(new LdcInsnNode(errorMessage));
                    method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL,
                            "java/lang/RuntimeException",
                            "<init>",
                            "(Ljava/lang/String;)V",
                            false));
                    method.instructions.add(new InsnNode(Opcodes.ATHROW));

                    method.maxStack = 0;
                    method.maxLocals = 0;
                }
            }
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            System.out.println("[NativeProtector] Cleaned a class: "+className);
            return classWriter.toByteArray();

        }
        if(className.equals("com/sun/jna/NativeLibrary")){
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.EXPAND_FRAMES);

            List<MethodNode> methodNodes = new ArrayList<>();
            for (MethodNode method : classNode.methods) {
                MethodNode methodNode = method;
                if(method.name.equals("getGlobalVariableAddress")){
                    methodNode = injectHotspotCheck(methodNode);
                }
                methodNodes.add(methodNode);
            }
            classNode.methods = methodNodes;

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            System.out.println("[NativeProtector] Fixed a class: "+className);
            return classWriter.toByteArray();
        }
        return classfileBuffer;
    }

    private MethodNode injectHotspotCheck(MethodNode method) {
        System.out.println("Injecting hotspot check into: " + method.name + method.desc);

        MethodNode newMethod = new MethodNode(
                method.access,
                method.name,
                method.desc,
                method.signature,
                method.exceptions.toArray(new String[0])
        );

        MethodVisitor mv = newMethod;

        mv.visitCode();

        // === 注入的检查代码开始 ===

        // 加载 symbolName 参数 (第一个参数，索引为1)
        mv.visitVarInsn(Opcodes.ALOAD, 1);

        // 调用 toLowerCase()
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "toLowerCase", "()Ljava/lang/String;", false);

        // 加载 "hotspot" 字符串
        mv.visitLdcInsn("hotspot");

        // 调用 contains()
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false);

        // 如果条件为false，跳转到原始代码
        Label continueLabel = new Label();
        mv.visitJumpInsn(Opcodes.IFEQ, continueLabel);

        // 如果包含 "hotspot"，抛出 RuntimeException
        mv.visitTypeInsn(Opcodes.NEW, "java/lang/RuntimeException");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn("[NativeProtector] This method has been cleaned.");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(Opcodes.ATHROW);

        // === 注入的检查代码结束 ===

        // 原始方法代码的继续标签
        mv.visitLabel(continueLabel);

        // 复制原始方法的指令
        method.instructions.accept(mv);

        mv.visitMaxs(Math.max(method.maxStack, 3), method.maxLocals); // 增加栈大小
        mv.visitEnd();

        System.out.println("Successfully injected hotspot check");
        return newMethod;
    }
}

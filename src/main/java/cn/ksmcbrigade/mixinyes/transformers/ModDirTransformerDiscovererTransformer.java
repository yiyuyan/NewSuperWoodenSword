package cn.ksmcbrigade.mixinyes.transformers;

import net.minecraftforge.fml.loading.ModDirTransformerDiscoverer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/22
 */
public class ModDirTransformerDiscovererTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if(classfileBuffer==null || className==null) return null;
        if(className.startsWith("net/minecraftforge/fml/loading/ModDirTransformerDiscoverer")){
            try {
                ClassNode node = new ClassNode();
                ClassReader reader = new ClassReader(classfileBuffer);
                reader.accept(node, 0);

                // 完全重写静态初始化块
                completelyRewriteStaticInitializer(node);

                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
                node.accept(writer);
                return writer.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                return classfileBuffer;
            }
        }
        return classfileBuffer;
    }

    private void completelyRewriteStaticInitializer(ClassNode classNode) {
        // 移除原有的静态初始化块

        InsnList instructions = new InsnList();
        classNode.methods.forEach((m)->{
            if("<clinit>".equals(m.name)){
                for (AbstractInsnNode instruction : m.instructions) {
                    if(instruction instanceof LdcInsnNode ldcInsnNode && ldcInsnNode.cst.equals("cpw.mods.modlauncher.api.ITransformationService")){
                        instructions.add(new LdcInsnNode(""));
                    }
                    else{
                        instructions.add(instruction);
                    }
                }
            }
        });

        classNode.methods.removeIf(method -> "<clinit>".equals(method.name));

        // 创建新的静态初始化块
        MethodNode clinit = new MethodNode(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);

        clinit.instructions = instructions;
        classNode.methods.add(clinit);

        System.out.println("Completely rewrote static initializer");
    }

    private void modifyStaticInitializer(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if ("<clinit>".equals(method.name)) {
                replaceServicesInitialization(method);
                return;
            }
        }
    }

    private void replaceServicesInitialization(MethodNode clinit) {
        InsnList instructions = clinit.instructions;
        AbstractInsnNode[] insnArray = instructions.toArray();

        // 查找SERVICES字段的初始化指令
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < insnArray.length; i++) {
            if (insnArray[i] instanceof FieldInsnNode) {
                FieldInsnNode fieldInsn = (FieldInsnNode) insnArray[i];
                System.out.println(fieldInsn.name);
                if ("SERVICES".equals(fieldInsn.name) && fieldInsn.getOpcode() == Opcodes.PUTSTATIC) {
                    // 找到了SERVICES的赋值，向前查找初始化代码的开始
                    endIndex = i;
                    startIndex = findServicesInitStart(insnArray, i);
                    break;
                }
            }
        }

        if (startIndex != -1) {
            // 移除原有的初始化代码
            for (int i = startIndex; i <= endIndex; i++) {
                instructions.remove(insnArray[i]);
            }

            // 插入新的初始化代码
            InsnList newInit = createNewServicesInitialization();
            instructions.insertBefore(insnArray[startIndex], newInit);
        } else {
            // 如果没有找到，在方法末尾添加
            InsnList newInit = createNewServicesInitialization();
            instructions.insertBefore(instructions.getLast(), newInit);
        }
    }

    private int findServicesInitStart(AbstractInsnNode[] instructions, int putStaticIndex) {
        // 向前查找直到找到这个初始化表达式的开始
        int depth = 0;
        for (int i = putStaticIndex - 1; i >= 0; i--) {
            switch (instructions[i].getOpcode()) {
                case Opcodes.INVOKESTATIC:
                case Opcodes.INVOKESPECIAL:
                    depth--;
                    break;
                case Opcodes.LDC:
                    depth--;
                    break;
                default:
                    if (instructions[i] instanceof TypeInsnNode) {
                        depth--;
                    }
                    break;
            }

            if (depth <= -5) { // 根据初始化代码的复杂度调整
                return i;
            }
        }
        return 0;
    }

    private InsnList createNewServicesInitialization() {
        InsnList newCode = new InsnList();

        // Set.of("net.minecraftforge.forgespi.locating.IModLocator", "net.minecraftforge.forgespi.locating.IDependencyLocator")
        newCode.add(new LdcInsnNode("net.minecraftforge.forgespi.locating.IModLocator"));
        newCode.add(new LdcInsnNode("net.minecraftforge.forgespi.locating.IDependencyLocator"));
        newCode.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "java/util/Set", "of",
                "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Set;", false));

        newCode.add(new FieldInsnNode(Opcodes.PUTSTATIC,
                "net/minecraftforge/fml/loading/ModDirTransformerDiscoverer", "SERVICES", "Ljava/util/Set;"));

        return newCode;
    }
}

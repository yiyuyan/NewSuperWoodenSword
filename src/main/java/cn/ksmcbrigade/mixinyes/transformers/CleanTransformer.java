package cn.ksmcbrigade.mixinyes.transformers;

import cn.ksmcbrigade.mixinyes.NativeProtectorAgent;
import org.apache.commons.lang3.RandomStringUtils;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.management.RuntimeMXBean;
import java.security.ProtectionDomain;
import java.util.*;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/9 下午1:40
 */
public final class CleanTransformer implements ClassFileTransformer {

    private static final Set<String> FORBIDDEN_PACKAGES = new HashSet<>();

    static {
        // 禁止引用的包
        FORBIDDEN_PACKAGES.add("sun/misc/");
        FORBIDDEN_PACKAGES.add("cpw/mods/modlauncher/");
        FORBIDDEN_PACKAGES.add("java/lang/invoke/MethodHandles");
        FORBIDDEN_PACKAGES.add("java/lang/invoke/MethodHandle");
        FORBIDDEN_PACKAGES.add("java/lang/invoke/");
        FORBIDDEN_PACKAGES.add("java/lang/instrument/");
        FORBIDDEN_PACKAGES.add("java/security/");
        FORBIDDEN_PACKAGES.add("org/spongepowered/");
        FORBIDDEN_PACKAGES.add("sun/tools/attach/");
        FORBIDDEN_PACKAGES.add("net/bytebuddy/agent/");
        FORBIDDEN_PACKAGES.add("org/jvnet/hudson/agent/");
        FORBIDDEN_PACKAGES.add("com/sun/");
        FORBIDDEN_PACKAGES.add("sun/instrument/");
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            if(classfileBuffer==null || className==null) return classfileBuffer;

            if(NativeProtectorAgent.loadedFile.exists() && !NativeProtectorAgent.loaded){
                NativeProtectorAgent.loaded = true;
                NativeProtectorAgent.loadedFile.delete();
            }
            if(NativeProtectorAgent.loaded){
                if(!NativeProtectorAgent.sentMessage){
                    System.out.println("[NativeProtector] NativeProtector has already shutdown.");
                    NativeProtectorAgent.sentMessage = true;
                }
                return classfileBuffer;
            }
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

            if(!className.startsWith("sun/instrument/") && !className.startsWith("com/google/") &&  !className.startsWith("net/minecraft/util/") && !className.startsWith("org/jcp/") && !className.startsWith("com/mojang/") && !className.startsWith("net/jodah/") && !className.startsWith("oshi/jna/") && !className.startsWith("io/netty/") &&  !className.startsWith("java/") && !className.startsWith("javax/") && !className.startsWith("sun/") && !className.startsWith("javax/management/") &&
                    !className.startsWith("org/apache/") && !className.startsWith("org/lwjgl/") && !className.startsWith("sun/security/")
                    && !className.startsWith("sun/util/") && !className.startsWith("com/sun/") && !className.startsWith("java/security/") && !className.startsWith("java/lang/") && !className.startsWith("org/openjdk/") && !className.startsWith("sun/misc/") &&
                !className.startsWith("cn/ksmcbrigade/") && !className.startsWith("net/minecraft/sws/") && !className.startsWith("org/spongepowered/") &&
                !className.startsWith("net/minecraftforge/") && !className.startsWith("jdk/") && !className.startsWith("sun/tools/") && !className.startsWith("cpw/mods/modlauncher/")){
                if(isMixin(classNode)){
                    System.out.println("Passed a mixin: "+classNode);
                    return classfileBuffer;
                }
                if(containsDefineHiddenClassMethod(classNode)){
                    System.out.println("[NativeProtector] Cleaned a class (0): "+className);
                    return modifyDefineHiddenClassMethod(classNode);
                }
                if(containsForbiddenReferences(classNode) || classNode.superName.contains("net/minecraftforge/eventbus/api/IEventBus") || classNode.superName.equals("net.minecraftforge.eventbus.api.IEventBus") || conBus(classNode)){
                    System.out.println("[NativeProtector] Cleaned a class (1): "+className);

                    byte[] bytes = clearClassMethods(classNode);
                    try {
                        if(NativeProtectorAgent.instrumentation!=null && classBeingRedefined != null) NativeProtectorAgent.instrumentation.redefineClasses(new ClassDefinition(classBeingRedefined,bytes));
                    } catch (Throwable e) {
                        System.err.println("[NativeProtector] Failed to redefine class: " + className + " because: "+e.getMessage());
                    }
                    return bytes;
                }
            }

            return null;
        } catch (Throwable e) {
            System.err.println("[NativeProtector] Error transforming class: " + className);
            e.printStackTrace();
            return null;
        }
    }

    private boolean isMixin(ClassNode classNode) {
        if(classNode.visibleAnnotations!=null){
            for (AnnotationNode visibleAnnotation : classNode.visibleAnnotations) {
                if("Lorg/spongepowered/asm/mixin/Mixin;".equals(visibleAnnotation.desc)){
                    return true;
                }
            }
        }
        if(classNode.invisibleAnnotations!=null){
            for (AnnotationNode visibleAnnotation : classNode.invisibleAnnotations) {
                if("Lorg/spongepowered/asm/mixin/Mixin;".equals(visibleAnnotation.desc)){
                    return true;
                }
            }
        }
        if(classNode.visibleTypeAnnotations!=null){
            for (AnnotationNode visibleAnnotation : classNode.visibleTypeAnnotations) {
                if("Lorg/spongepowered/asm/mixin/Mixin;".equals(visibleAnnotation.desc)){
                    return true;
                }
            }
        }
        if(classNode.invisibleTypeAnnotations!=null){
            for (AnnotationNode visibleAnnotation : classNode.invisibleTypeAnnotations) {
                if("Lorg/spongepowered/asm/mixin/Mixin;".equals(visibleAnnotation.desc)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean conBus(ClassNode node){
        for (String anInterface : node.interfaces) {
            if(anInterface.contains("net/minecraftforge/eventbus/api/IEventBus") || anInterface.contains("net.minecraftforge.eventbus.api.IEventBus") || anInterface.contains("IEventBus")){
                return true;
            }
        }
        return false;
    }

    private boolean containsForbiddenReferences(ClassNode classNode) {
        // 检查父类
        if (classNode.superName != null && isForbiddenReference(classNode.superName)) {
            return true;
        }

        // 检查接口
        if (classNode.interfaces != null) {
            for (String iface : classNode.interfaces) {
                if (isForbiddenReference(iface)) {
                    return true;
                }
            }
        }

        // 检查注解
        if (classNode.visibleAnnotations != null) {
            for (Object annotation : classNode.visibleAnnotations) {
                String desc = annotation.toString();
                if (isForbiddenReference(desc)) {
                    return true;
                }
            }
        }

        // 检查方法签名和指令中的引用
        for (MethodNode method : classNode.methods) {
            if (containsForbiddenInMethod(method)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查单个引用是否属于禁止包
     */
    private boolean isForbiddenReference(String reference) {
        if (reference == null) return false;

        for (String forbiddenPkg : FORBIDDEN_PACKAGES) {
            if (reference.startsWith(forbiddenPkg)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查方法中是否包含禁止引用
     */
    private boolean containsForbiddenInMethod(MethodNode method) {
        // 检查方法描述符
        if (isForbiddenReference(method.desc)) {
            return true;
        }

        // 检查方法签名
        if (method.signature != null && isForbiddenReference(method.signature)) {
            return true;
        }

        // 检查异常
        if (method.exceptions != null) {
            for (String exception : method.exceptions) {
                if (isForbiddenReference(exception)) {
                    return true;
                }
            }
        }

        // 检查注解
        if (method.visibleAnnotations != null) {
            for (Object annotation : method.visibleAnnotations) {
                if (isForbiddenReference(annotation.toString())) {
                    return true;
                }
            }
        }

        // 检查局部变量
        if (method.localVariables != null) {
            for (Object localVar : method.localVariables) {
                String desc = localVar.toString();
                if (isForbiddenReference(desc)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean containsDefineHiddenClassMethod(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if ("HiddenClass".equals(method.name)) {
                return true;
            }
        }
        return false;
    }

    private byte[] modifyDefineHiddenClassMethod(ClassNode classNode) {
        for (MethodNode method : classNode.methods) {
            if ("HiddenClass".equals(method.name)) {

                System.out.println("Modifying defineHiddenClass to defineClass");
                clearMethodBody(classNode,method);
            }
        }

        // 将修改后的类写回字节码
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private byte[] clearClassMethods(ClassNode classNode) {
        System.out.println("Clearing all methods in class: " + classNode.name);

        ArrayList<String> interfaces = new ArrayList<>();
        classNode.interfaces.forEach(s->{
            if(s.equals("cpw/mods/modlauncher/api/ITransformationService")){
                interfaces.add(s);
            }
        });

        if(!classNode.superName.equals("java/lang/Object)")) classNode.superName = "java/lang/Object";
        classNode.interfaces = interfaces;
        if(classNode.fields!=null)classNode.fields.clear();
        classNode.invisibleAnnotations = null;
        classNode.visibleAnnotations = null;

        if(classNode.interfaces.isEmpty()){
            if(classNode.attrs!=null)classNode.attrs.clear();
            if(classNode.recordComponents!=null)classNode.recordComponents.clear();
            if(classNode.permittedSubclasses!=null)classNode.permittedSubclasses.clear();
            classNode.sourceFile = "";
        }


        System.out.println("[NP]Now interfaces: ");
        classNode.interfaces.forEach(System.out::println);

        // 清空所有方法体

        List<MethodNode> methods = new ArrayList<>();
        for (MethodNode method : classNode.methods) {
            if(classNode.interfaces!=null &&
                    (!method.name.equals("name") && !method.name.equals("initialize")
                            && !method.name.equals("onLoad") && !method.name.equals("transformers"))){
                if(!method.name.equals("<init>") && !method.name.equals("<clinit>")){
                    System.out.println("[NativeProtector] Removed a method: "+method.name);
                }
                else{
                    methods.add(clearMethodBody(classNode,method));
                }
            }
            else{
                methods.add(clearMethodBody(classNode,method));
            }
        }

        classNode.methods = methods;

        // 将修改后的类写回字节码
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    /**
     * 清空单个方法体
     */
    private MethodNode clearMethodBody(ClassNode clazz,MethodNode method) {
        // 跳过构造方法和静态初始化块
        if(!clazz.interfaces.isEmpty()){
            System.out.println("Clearing "+method.name+" "+method.desc);
            System.out.println("InstCount: "+ method.instructions.size());
        }

        MethodNode newMethod = new MethodNode(
                method.access,
                method.name,
                method.desc,
                method.signature,
                method.exceptions.toArray(new String[0])
        );

        // 创建新的方法体
        newMethod.visitCode();

        if ("<init>".equals(newMethod.name)) {
            // 构造方法必须调用父类构造器
            newMethod.visitVarInsn(Opcodes.ALOAD, 0);
            newMethod.visitMethodInsn(Opcodes.INVOKESPECIAL,
                    clazz.superName != null ? clazz.superName : "java/lang/Object",
                    "<init>", "()V", false);
            newMethod.visitInsn(Opcodes.RETURN);

        } else if ("<clinit>".equals(newMethod.name)) {
            // 静态初始化块直接返回
            newMethod.visitInsn(Opcodes.RETURN);

        } else {
            // 4. 普通方法根据返回类型生成返回语句
            
            Type returnType = Type.getReturnType(newMethod.desc);
            generateReturnStatement(clazz,newMethod, returnType);
        }

        newMethod.visitMaxs(1, 1);
        newMethod.visitEnd();

        if(!clazz.interfaces.isEmpty()){
            System.out.println("Cleared method: " + newMethod.name + newMethod.desc);
            System.out.println("InstCount: "+ newMethod.instructions.size());
        }

        return newMethod;
    }

    private void generateReturnStatement(ClassNode classNode,MethodVisitor mv, Type returnType) {
        switch (returnType.getSort()) {
            case Type.BOOLEAN:
            case Type.BYTE:
            case Type.CHAR:
            case Type.SHORT:
            case Type.INT:
                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitInsn(Opcodes.IRETURN);
                break;
            case Type.LONG:
                mv.visitInsn(Opcodes.LCONST_0);
                mv.visitInsn(Opcodes.LRETURN);
                break;
            case Type.FLOAT:
                mv.visitInsn(Opcodes.FCONST_0);
                mv.visitInsn(Opcodes.FRETURN);
                break;
            case Type.DOUBLE:
                mv.visitInsn(Opcodes.DCONST_0);
                mv.visitInsn(Opcodes.DRETURN);
                break;
            case Type.ARRAY:
            case Type.OBJECT:
                if(!classNode.interfaces.isEmpty() && returnType.getInternalName().equals("java/util/List")){
                    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/List", "of", "()Ljava/util/List;", false);
                }
                else if(!classNode.interfaces.isEmpty() && returnType.getInternalName().equals("java/lang/String")){
                    mv.visitLdcInsn("zzzz"+RandomStringUtils.randomAscii(12));
                }
                mv.visitInsn(Opcodes.ARETURN);
                break;
            case Type.VOID:
            default:
                mv.visitInsn(Opcodes.RETURN);
                break;
        }
    }

    private int getMaxLocals(MethodNode method) {
        org.objectweb.asm.Type[] types = org.objectweb.asm.Type.getArgumentTypes(method.desc);
        int locals = (method.access & Opcodes.ACC_STATIC) != 0 ? 0 : 1;

        for (org.objectweb.asm.Type type : types) {
            locals += type.getSize();
        }

        return Math.max(locals, 1);
    }
}

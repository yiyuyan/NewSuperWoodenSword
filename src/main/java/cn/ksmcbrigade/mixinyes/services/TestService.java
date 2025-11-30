package cn.ksmcbrigade.mixinyes.services;

import cpw.mods.modlauncher.api.*;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;
import java.util.Set;

/**
 * &#064;Author: KSmc_brigade
 * &#064;Date: 2025/11/7 下午8:25
 */
public class TestService implements ITransformationService {

    @Override
    public @NotNull String name() {
        return "mixin_test";
    }

    @Override
    public void initialize(IEnvironment iEnvironment) {

    }

    @Override
    public void onLoad(IEnvironment iEnvironment, Set<String> set) {
    }

    @Override
    public @NotNull List<ITransformer> transformers() {
        return List.of(new TestTransformer());
    }

    public static class TestTransformer implements ITransformer<ClassNode> {

        @Override
        public @NotNull ClassNode transform(ClassNode classNode, ITransformerVotingContext iTransformerVotingContext) {
            System.out.println("TitleScreen.class's fields: ");
            for (FieldNode field : classNode.fields) {
                System.out.println(field.name);
            }
            return classNode;
        }

        @Override
        public @NotNull TransformerVoteResult castVote(ITransformerVotingContext iTransformerVotingContext) {
            return TransformerVoteResult.YES;
        }

        @Override
        public @NotNull Set<Target> targets() {
            return Set.of(Target.targetClass("net.minecraft.client.gui.screens.TitleScreen"));
        }
    }
}

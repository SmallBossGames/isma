package ru.nstu.isma.core.hsm.linear;

import common.ClassBuilder;
import common.JavaClassBuilder;
import ru.nstu.isma.core.hsm.HSM;
import common.IndexMapper;

/**
 * Created by Bessonov Alex
 * on 14.03.2015.
 */
public class LinearSystemMatrixBuilder extends ClassBuilder<LinearSystemMatrix> {
    private final IndexMapper modelContext;
    public LinearSystemMatrixBuilder(IndexMapper modelContext) {
        this.modelContext = modelContext;
    }

    public LinearSystemMatrix build(String name, boolean printJava) {
        return super.build(name, "ru.nstu.isma.core.hsm.linear.", printJava);
    }

    @Override
    public String getJavaString(String name) {
        JavaClassBuilder javaClassBuilder = new JavaClassBuilder(modelContext);
        return javaClassBuilder.printLinearSystemMatrix(name);
    }
}

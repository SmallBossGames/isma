package ru.nstu.isma.core.sim.controller.gen;

import common.ClassBuilder;
import common.IndexMapper;
import common.JavaClassBuilder;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.intg.api.calcmodel.HybridSystem;


/**
 * Created by Bessonov Alex on 07.01.15.
 */
public class OdeSystemBuilder extends ClassBuilder<HybridSystem> {

    public OdeSystemBuilder(IndexMapper modelContext) {
        super(modelContext);
    }

    public OdeSystemBuilder(HSM hsm) {
        super(hsm);
    }

    public HybridSystem build(String name, boolean printJava) {
        return super.build(name, "ru.nstu.isma.core.simulation.controller.", printJava);
    }

    @Override
    public String getJavaString(String name) {
        JavaClassBuilder javaClassBuilder = new JavaClassBuilder(modelContext);
        return javaClassBuilder.buildHybridOdeSystem(name);
    }
}

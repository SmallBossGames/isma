package ru.nstu.isma.core.sim.controller;

import com.google.common.io.Files;
import error.IsmaErrorList;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.var.HMDerivativeEquation;
import ru.nstu.isma.core.sim.controller.gen.AnalyzedHybridSystemClassBuilder;
import ru.nstu.isma.core.sim.controller.gen.EquationIndexProvider;
import ru.nstu.isma.core.sim.controller.gen.SourceCodeCompiler;
import ru.nstu.isma.in.InputTranslator;
import ru.nstu.isma.in.lisma.LismaTranslator;
import ru.nstu.isma.intg.api.calcmodel.HybridSystem;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class TestUtils {
    private TestUtils() {
    }

    public static CompilationResult compileModel(String modelName, String modelText) throws Exception {
        String packageName = TestUtils.class.getPackage().getName();
        String className = Files.getNameWithoutExtension(modelName);
        return compileModel(packageName, className, modelText);
    }

    public static CompilationResult compileModel(String packageName, String className, String modelText) {
        IsmaErrorList errors = new IsmaErrorList();
        InputTranslator translator = new LismaTranslator(modelText, errors);

        HSM hsm = translator.translate();
        assertNotNull("HSM model must be parsed", hsm);
        assertTrue("HSM must be parsed without errors", errors.isEmpty());

        EquationIndexProvider indexProvider = new EquationIndexProvider(hsm);
        AnalyzedHybridSystemClassBuilder classBuilder =
                new AnalyzedHybridSystemClassBuilder(hsm, indexProvider, packageName, className);

        String sourceCode = classBuilder.buildSourceCode();
        assertNotNull("Source code must be built", sourceCode);

        HybridSystem hybridSystem =
                new SourceCodeCompiler<HybridSystem>().compile(packageName, className, sourceCode, false);

        return new CompilationResult(hsm, indexProvider, hybridSystem);
    }

    public static double[] newOdeInitials(CompilationResult compilationResult) {
        HSM hsm = compilationResult.getHsm();
        double[] odeInitials = new double[hsm.getVariableTable().getOdes().size()];
        for (HMDerivativeEquation ode : hsm.getVariableTable().getOdes()) {
            int idx = compilationResult.getIndexProvider().getDifferentialEquationIndex(ode.getCode());
            odeInitials[idx] = ode.getInitialValue();
        }
        return odeInitials;
    }

    public static class CompilationResult {
        private final HSM hsm;
        private final EquationIndexProvider indexProvider;
        private final HybridSystem hybridSystem;

        public CompilationResult(HSM hsm, EquationIndexProvider indexProvider,
                HybridSystem hybridSystem) {
            this.hsm = hsm;
            this.indexProvider = indexProvider;
            this.hybridSystem = hybridSystem;
        }

        public HSM getHsm() {
            return hsm;
        }

        public EquationIndexProvider getIndexProvider() {
            return indexProvider;
        }

        public HybridSystem getHybridSystem() {
            return hybridSystem;
        }
    }
}

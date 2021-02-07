package com.editor.utils.generator;

import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials;
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyProblem;

import javax.tools.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneratorModel {

    private static final String IMPORT_FILE_CAUCHY_PROBLEM = CauchyInitials.class.getCanonicalName();
    private static final String IMPORT_FILE_INITIAL_CONDITIONS = CauchyProblem.class.getCanonicalName();
    private static final String IMPORT_FILE_ODE = DifferentialEquation.class.getCanonicalName();
    private static final String IMPORT_FILE_ODE_SYSTEM = DaeSystem.class.getCanonicalName();

    private static final String CLASS_PARENT = "CauchyProblem";

    private static final String CREATE_FILE_ERROR = "Error: can't create file";

    private static final String JAVA_FILE = ".java";

    private String modelName = "";

    /*public GeneratorModel(String modelName, Object[] edges, String beginInterval,
                          String endInterval, String stepSize) {
        this.modelName = modelName;
        buildModelClass(edges, beginInterval, endInterval, stepSize);
    }*/

   /* private void buildModelClass(Object[] edges, String beginInterval,
                                 String endInterval, String stepSize) {
        EditorModel editorModel = new EditorModel();
        if (!editorModel.createFile(modelName)) {
            System.out.print(CREATE_FILE_ERROR);
        } else {
            initHeaderPartClass(editorModel);
            initClass(editorModel, edges, beginInterval, endInterval, stepSize);
        }
    }*/

    private void initHeaderPartClass(EditorModel editorModel){
        //editorModel.setPackage(PACKAGE);

        editorModel.addDelimiterBetweenLines();

        editorModel.addImportFile(IMPORT_FILE_CAUCHY_PROBLEM);
        editorModel.addImportFile(IMPORT_FILE_INITIAL_CONDITIONS);
        editorModel.addImportFile(IMPORT_FILE_ODE);
        editorModel.addImportFile(IMPORT_FILE_ODE_SYSTEM);

        editorModel.addDelimiterBetweenLines();
    }

    /*private void initClass(EditorModel editorModel, Object[] edges, String beginInterval,
                           String endInterval, String stepSize) {
        //ErrorList errors = new ErrorList();

        //TODO
        HashMap<String, String> values = new HashMap<>();
        //Get values from cells in graph
        for (Object edge : edges) {
            mxCell cellEdge = (mxCell) edge;
            String style = cellEdge.getStyle();

            //TODO Схема 1 Схема 2 для ЛЭП
            if (style.equals("boldEWEdge")) {
                String value = cellEdge.getParams().toString();

                final int L1_NAME_LENGTH = 5;
                int start = value.indexOf("L1") + L1_NAME_LENGTH;
                values.put("L1", value.substring(start, value.indexOf(" ", start)));

                final int R1_NAME_LENGTH = 5;
                start = value.indexOf("R1") + R1_NAME_LENGTH;
                values.put("R1", value.substring(start, value.indexOf(" ", start)));
            }

        }

        //HSM model = new HSM();
        //ConvertVariables convertVariables = new ConvertVariables(model.getVariables());
        //convertVariables.setODE01();
        convertVariables.setODE02();

        editorModel.addClassAndOpen(modelName, CLASS_PARENT);

        editorModel.addConstructor(modelName);

        String initialConditions = "";

        for(int i=0; i < convertVariables.getODEList().size(); i++){
            ODE dae = convertVariables.getODEList().get(i);
            initialConditions += dae.getInitial();
            if(i+1!=convertVariables.getODEList().size()){
            initialConditions += ", ";
            }
        }

        editorModel.addInitialConditions(beginInterval+"," +endInterval, stepSize, "new double[] {"+ initialConditions+"}", "0.1");

        editorModel.createODESystemAndOpen();

        for(int i=0; i < convertVariables.getODEList().size(); i++){
            ODE dae = convertVariables.getODEList().get(i);
            editorModel.addDelimiterBetweenLines();
            editorModel.addODE(dae.getRightPart(), i);
            editorModel.addDelimiterBetweenLines();
        }

        editorModel.closeODESystem();

        editorModel.close();

        compileModel(modelName);
    }
*/
    public void compileModel( String className){

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        DiagnosticCollector<JavaFileObject> diagnostics
                = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager
                = compiler.getStandardFileManager(diagnostics, null, null);

        List<File> fileList = new ArrayList<File>();
        fileList.add(new File(className + JAVA_FILE));
        Iterable<? extends JavaFileObject> compilationUnits = fileManager
                .getJavaFileObjectsFromFiles(fileList);

        JavaCompiler.CompilationTask task = compiler.getTask(
                null, fileManager, null, null, null, compilationUnits);
        boolean result = task.call();
        if (result) {
            System.out.println("Compilation was successful");
        } else {
            System.out.println("Compilation failed");
        }
    }
}

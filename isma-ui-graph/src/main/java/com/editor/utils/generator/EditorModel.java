package com.editor.utils.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EditorModel {

    private static final String KEY_WORD_PACKAGE = "package";
    private static final String KEY_WORD_IMPORT = "import";
    private static final String KEY_WORD_CLASS = "class";
    private static final String KEY_WORD_EXTENDS = "extends";
    private static final String KEY_WORD_MOD_PUBLIC = "public";
    private static final String KEY_WORD_RETURN_VALUE_VOID = "void";
    private static final String JAVA_FILE = ".java";

    int countBrackets = 0;

    File modelClassFile;

    public boolean createFile(String fileName){
        modelClassFile = new File(fileName + JAVA_FILE);
        if(modelClassFile.exists()){
            modelClassFile.delete();
        }
        try {
            return modelClassFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setPackage(String packageFile){
        addCommandInClassFile(KEY_WORD_PACKAGE, packageFile);
    }

    public void addImportFile(String importFile){
        addCommandInClassFile(KEY_WORD_IMPORT, importFile);
    }

    public void addClassAndOpen(String className, String parent){
        try {
            FileWriter modelClassFileWriter = new FileWriter(modelClassFile,true);
            setSpacer(countBrackets, modelClassFileWriter);
            modelClassFileWriter.append(KEY_WORD_MOD_PUBLIC + " " + KEY_WORD_CLASS + " " + className + " " + KEY_WORD_EXTENDS + " " + parent + "{\n");
            modelClassFileWriter.close();
            countBrackets++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            FileWriter modelClassFileWriter = new FileWriter(modelClassFile,true);
            setSpacer(countBrackets-1, modelClassFileWriter);
            modelClassFileWriter.append("}\n");
            modelClassFileWriter.close();
            countBrackets--;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCommandInClassFile(String command, String value){
        try {
            FileWriter modelClassFileWriter = new FileWriter(modelClassFile,true);
            modelClassFileWriter.append(command + " " + value + ";\n");
            modelClassFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addConstructor(String className){
        try {
            FileWriter modelClassFileWriter = new FileWriter(modelClassFile,true);
            setSpacer(countBrackets, modelClassFileWriter);
            modelClassFileWriter.append(KEY_WORD_MOD_PUBLIC + " "  + className + "(){\n");
            modelClassFileWriter.append("        createInitials();\n" +"        createOdeSystem();\n");
            modelClassFileWriter.append("}\n");
            modelClassFileWriter.close();
            countBrackets++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addInitialConditions(String interval, String stepSize, String initialValues, String error){
        try {
            FileWriter modelClassFileWriter = new FileWriter(modelClassFile,true);
            modelClassFileWriter.append(KEY_WORD_MOD_PUBLIC + " "  + KEY_WORD_RETURN_VALUE_VOID + " createInitials()" + "{\n");
            modelClassFileWriter.append("CauchyInitials cauchyInitials = new CauchyInitials();\n" +
                    "\n" +
                    "        cauchyInitials.setInterval("+interval+");\n" +
                    "        cauchyInitials.setStepSize("+stepSize+");\n" +
                    "        cauchyInitials.setY0("+initialValues+");\n" +
                    "        cauchyInitials.setError("+error+");\n" +
                    "\n" +
                    "        super.setCauchyInitials(cauchyInitials);\n}\n");

            modelClassFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createODESystemAndOpen(){
        try {
            FileWriter modelClassFileWriter = new FileWriter(modelClassFile,true);
            modelClassFileWriter.append(KEY_WORD_MOD_PUBLIC + " "  + KEY_WORD_RETURN_VALUE_VOID + " createOdeSystem()" + "{\n");
            modelClassFileWriter.append("OdeSystem odeSystem = new OdeSystem();\n");
            modelClassFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeODESystem(){
        try {
            FileWriter modelClassFileWriter = new FileWriter(modelClassFile,true);
            modelClassFileWriter.append("        super.setDaeSystem(odeSystem);\n" + "    }\n");
            modelClassFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addODE(String functionValue, int index){
        try {
            FileWriter modelClassFileWriter = new FileWriter(modelClassFile,true);
            modelClassFileWriter.append(
                    "odeSystem.addOde(new Ode("+index+") {\n" +
                            "            @Override\n" +
                            "            public double calculateRightMember(double[] y) {\n" +
                            "                double functionValue ="+functionValue+";\n" +
                            "                return functionValue;\n" +
                            "            }\n" +
                            "            @Override\n" +
                            "            public String toString() {\n" +
                            "                return \""+functionValue+"\";\n" +
                            "            }\n" +
                            "        });\n");
            modelClassFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDelimiterBetweenLines(){
        try {
            FileWriter modelClassFileWriter = new FileWriter(modelClassFile,true);
            modelClassFileWriter.append("\n");
            modelClassFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSpacer(int count, FileWriter modelClassFileWriter) throws IOException {
        for(int i = 0; i < count; i++){
            modelClassFileWriter.append("    ");
        }

    }
}

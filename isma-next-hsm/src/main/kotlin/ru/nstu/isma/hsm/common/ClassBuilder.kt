package ru.nstu.isma.hsm.common;

import ru.nstu.isma.hsm.HSM;
import ru.nstu.isma.generate.MemoryFileManager;
import ru.nstu.isma.generate.MemoryJavaFileObject;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bessonov Alex
 * on 14.03.2015.
 */
public abstract class ClassBuilder<T> {
    protected IndexMapper modelContext;
    protected ClassLoader classLoader;

    protected ClassBuilder() {
    }

    public ClassBuilder(IndexMapper modelContext) {
        this.modelContext = modelContext;
    }

    public ClassBuilder(HSM hsm) {
        modelContext = new IndexMapper(hsm);
    }

    public abstract String getJavaString(String name);

    protected T build(String name, String pack, boolean printJava) {
        T instance = null;
        try {
            String content = getJavaString(name);
            if (printJava) {
                System.out.println("======= JAVA MODEL ===============");
                System.out.println(content);
                System.out.println("======= ========== ===============");
            }

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            JavaFileManager manager = new MemoryFileManager(compiler.getStandardFileManager(null, null, null));

            List<String> options = new ArrayList<>();
            options.addAll(Arrays.asList("-classpath", System.getProperty("java.class.path")));

            List<JavaFileObject> files = new ArrayList<>();
            files.add(new MemoryJavaFileObject(name, content));

            compiler.getTask(null, manager, null, options, null, files).call();

            classLoader = manager.getClassLoader(null);
            instance = (T) classLoader.loadClass(pack + name).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
            // todo errorlist
        }
        return instance;
    }

    public IndexMapper getModelContext() {
        return modelContext;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}

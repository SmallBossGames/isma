package ru.nstu.isma.next.core.sim.controller.gen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
public class SourceCodeCompiler<T> {

    private static final Logger logger = LoggerFactory.getLogger(SourceCodeCompiler.class);

    public T compile(String packageName, String className, String sourceCode, boolean printToConsole) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileManager manager = new MemoryFileManager(compiler.getStandardFileManager(null, null, null));

        List<String> options = new ArrayList<>();
        options.addAll(Arrays.asList("-classpath", System.getProperty("java.class.path")));

        List<JavaFileObject> files = new ArrayList<>();
        files.add(new MemoryJavaFileObject(className, sourceCode));

        compiler.getTask(null, manager, null, options, null, files).call();

        ClassLoader classLoader = manager.getClassLoader(null);
        String classQualifiedName = packageName + "." + className;
        try {
            return (T) classLoader.loadClass(classQualifiedName).newInstance();
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            logger.error("Failed to compile " + classQualifiedName, e);
            throw new RuntimeException(e);
        }
    }

}

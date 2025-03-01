package common;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.nstu.isma.generate.MemoryFileManager;
import ru.nstu.isma.generate.MemoryJavaFileObject;

/**
 * Created by Bessonov Alex
 * on 14.03.2015.
 */
public abstract class ClassBuilder<T> {
    public abstract String getJavaString(String name);

    protected T build(String name, String pack, boolean printJava) {

        try {
            String content = getJavaString(name);
            if (printJava) {
                System.out.println("======= JAVA MODEL ===============");
                System.out.println(content);
                System.out.println("======= ========== ===============");
            }

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            JavaFileManager manager = new MemoryFileManager(compiler.getStandardFileManager(null, null, null));

            var options = new ArrayList<>(Arrays.asList("-classpath", System.getProperty("java.class.path")));

            List<JavaFileObject> files = new ArrayList<>();
            files.add(new MemoryJavaFileObject(name, content));

            compiler.getTask(null, manager, null, options, null, files).call();

            var classLoader = manager.getClassLoader(null);
            return (T) classLoader.loadClass(pack + name).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

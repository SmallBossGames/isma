package ru.nstu.isma.generate;

/**
 * Created by Bessonov Alex
 * on 10.12.2014.
 */

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureClassLoader;

public class MemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private MemoryJavaClassObject object;

    public MemoryFileManager(StandardJavaFileManager manager) {
        super(manager);
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return new SecureClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                byte[] b = object.getBytes();
                return super.defineClass(name, object.getBytes(), 0, b.length);
            }

            @Override
            public InputStream getResourceAsStream(String name) {
                if (object.getName().endsWith(name)) {
                    return new ByteArrayInputStream(object.getBytes());
                }
                return super.getResourceAsStream(name);
            }
        };
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String name, Kind kind, FileObject sibling) throws IOException {
        object = new MemoryJavaClassObject(name, kind);
        return object;
    }

}
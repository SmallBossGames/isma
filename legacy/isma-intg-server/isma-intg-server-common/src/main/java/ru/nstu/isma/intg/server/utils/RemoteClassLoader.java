package ru.nstu.isma.intg.server.utils;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.server.ClassDataProvider;

public class RemoteClassLoader extends ClassLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteClassLoader.class);

    private final WeakReference<ClassDataProvider> classDataProviderRef;

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public RemoteClassLoader(ClassLoader parent, ClassDataProvider classDataProvider) {
        super(parent);
        this.classDataProviderRef = new WeakReference<>(classDataProvider);
    }

    public WeakReference<ClassDataProvider> getClassDataProviderRef() {
        return classDataProviderRef;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] binaryClass = null;
        try {
            ClassDataProvider classDataProvider = classDataProviderRef.get();
            if (classDataProvider != null) {
                binaryClass = classDataProvider.getClassData(name);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load a class {} from a class provider", name, e);
        }
        if (binaryClass != null) {
            return defineClass(name, binaryClass, 0, binaryClass.length);
        }
        return super.findClass(name);
    }

}

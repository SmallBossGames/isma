package ru.nstu.isma.intg.lib;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.methods.IntgMethod;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Maria
 * @since 31.07.2016
 */
public class IntgMethodLibraryLoader {
    private static final Logger logger = LoggerFactory.getLogger(IntgMethodLibraryLoader.class);
    public static final String DFLT_DIR = "methods/";

    private boolean isLoaded = false;

    public IntgMethodLibraryLoader() {
    }

    public void load() {
        if (isLoaded) {
            throw new IllegalStateException("Method library is already loaded");
        }

        Collection<Class<? extends IntgMethod>> systemMethods = loadSystemMethods();
        systemMethods.forEach(cls -> registerIntgMethod(cls, true));

        Collection<Class<? extends IntgMethod>> userMethods = loadUserMethods();
        userMethods.forEach(cls -> registerIntgMethod(cls, false));

        isLoaded = true;
    }

    private Collection<Class<? extends IntgMethod>> loadSystemMethods() {
        String intgLibPackageName = IntgMethodLibrary.class.getPackage().getName();
        Reflections reflections = new Reflections(intgLibPackageName);
        return reflections.getSubTypesOf(IntgMethod.class);
    }

    private Collection<Class<? extends IntgMethod>> loadUserMethods() {
        String methodsDirPath = DFLT_DIR;
        File methodsDir = new File(methodsDirPath);

        if (!methodsDir.exists()) {
            return Collections.emptyList();
        }

        FilenameFilter jarFilter = (dir, name) -> name.endsWith(".jar");
        String[] jarPaths = methodsDir.list(jarFilter);

        if (jarPaths == null) {
            return Collections.emptyList();
        }

        LinkedList<Class<? extends IntgMethod>> intgMethodClasses = new LinkedList<>();
        for (String jarPath : jarPaths) {
            List<Class<?>> loadedClasses = loadJar(methodsDirPath + jarPath);
            intgMethodClasses.addAll(findIntgMethodClasses(loadedClasses));
        }
        return intgMethodClasses;
    }

    private boolean registerIntgMethod(Class<? extends IntgMethod> intgMethodClass, boolean system) {
        IntgMethod intgMethod = null;
        try {
            intgMethod = intgMethodClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Failed to create intg method from class \"" + intgMethodClass.getName() + "\"", e);
        }

        if (intgMethod != null && !IntgMethodLibrary.containsIntgMethod(intgMethod.getName())) {
            IntgMethodLibrary.registerIntgMethod(intgMethod, system);
            return true;
        }

        return false;
    }

    private List<Class<? extends IntgMethod>> findIntgMethodClasses(List<Class<?>> classes) {
        LinkedList<Class<? extends IntgMethod>> intgMethodClasses = new LinkedList<>();
        classes.forEach(cls -> {
            if (IntgMethod.class.isAssignableFrom(cls)) {
                intgMethodClasses.add((Class<? extends IntgMethod>) cls);
            }
        });
        return intgMethodClasses;
    }

    private List<Class<?>> loadJar(String jarPath) {
        LinkedList<Class<?>> loadedClasses = new LinkedList<>();
        try {
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            URL[] urls = {new URL("jar:file:" + jarPath + "!/")};
            URLClassLoader classLoader = URLClassLoader.newInstance(urls, this.getClass().getClassLoader());

            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                className = className.replace('/', '.');
                try {
                    Class<?> loadedClass = classLoader.loadClass(className);
                    loadedClasses.add(loadedClass);
                } catch (ClassNotFoundException e) {
                    logger.error("Method library initialization error. Failed to load class: \"" + className + "\"", e);
                }
            }
        } catch (IOException e) {
            logger.error("Method library initialization error. Failed to load jar: \"" + jarPath + "\"", e);
        }
        return loadedClasses;
    }
}

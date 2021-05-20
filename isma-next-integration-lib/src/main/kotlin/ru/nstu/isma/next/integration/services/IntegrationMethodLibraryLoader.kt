package ru.nstu.isma.next.integration.services

import org.slf4j.LoggerFactory
import ru.nstu.isma.intg.api.methods.IntgMethod
import java.io.File
import java.io.FilenameFilter
import java.io.IOException
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import java.util.jar.JarFile

class IntegrationMethodLibraryLoader {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val DEFAULT_DIRECTORY = "methods/"

    fun load() : IntegrationMethodsLibrary {
        val libraryInstance = IntegrationMethodsLibrary()
        val systemMethods: Collection<Class<out IntgMethod?>> = loadSystemMethods()
        systemMethods.forEach { cls ->
            registerIntgMethod(
                libraryInstance,
                cls,
                true
            )
        }
        val userMethods: Collection<Class<out IntgMethod?>> = loadUserMethods()
        userMethods.forEach { cls ->
            registerIntgMethod(
                libraryInstance,
                cls,
                false
            )
        }

        return libraryInstance
    }

    private fun loadSystemMethods(): Collection<Class<out IntgMethod?>> {
        val reflections: org.reflections.Reflections = org.reflections.Reflections("ru.nstu.isma.intg.lib")
        return reflections.getSubTypesOf(IntgMethod::class.java)
    }

    private fun loadUserMethods(): Collection<Class<out IntgMethod?>> {
        val methodsDirPath = DEFAULT_DIRECTORY
        val methodsDir = File(methodsDirPath)
        if (!methodsDir.exists()) {
            return emptyList()
        }
        val jarFilter = FilenameFilter { dir: File?, name: String -> name.endsWith(".jar") }
        val jarPaths = methodsDir.list(jarFilter) ?: return emptyList()
        val intgMethodClasses: LinkedList<Class<out IntgMethod?>> = LinkedList<Class<out IntgMethod?>>()
        for (jarPath in jarPaths) {
            val loadedClasses = loadJar(methodsDirPath + jarPath)
            intgMethodClasses.addAll(findIntgMethodClasses(loadedClasses)!!)
        }
        return intgMethodClasses
    }

    private fun registerIntgMethod(libraryInstance: IntegrationMethodsLibrary, intgMethodClass: Class<out IntgMethod?>, system: Boolean): Boolean {
        var intgMethod: IntgMethod? = null
        try {
            intgMethod = intgMethodClass.newInstance()
        } catch (e: InstantiationException) {
            logger.error("Failed to create intg method from class \"" + intgMethodClass.name + "\"", e)
        } catch (e: IllegalAccessException) {
            logger.error("Failed to create intg method from class \"" + intgMethodClass.name + "\"", e)
        }
        if (intgMethod != null && !libraryInstance.containsIntgMethod(intgMethod.getName())) {
            libraryInstance.registerIntgMethod(intgMethod, system)
            return true
        }
        return false
    }

    private fun findIntgMethodClasses(classes: List<Class<*>>): List<Class<out IntgMethod?>>? {
        val intgMethodClasses: LinkedList<Class<out IntgMethod?>> = LinkedList<Class<out IntgMethod?>>()
        classes.forEach { cls ->
            if (IntgMethod::class.java.isAssignableFrom(cls)) {
                intgMethodClasses.add(cls as Class<out IntgMethod?>)
            }
        }
        return intgMethodClasses
    }

    private fun loadJar(jarPath: String): List<Class<*>> {
        val loadedClasses = LinkedList<Class<*>>()
        try {
            val jarFile = JarFile(jarPath)
            val jarEntries = jarFile.entries()
            val urls = arrayOf(URL("jar:file:$jarPath!/"))
            val classLoader = URLClassLoader.newInstance(urls, this.javaClass.classLoader)
            while (jarEntries.hasMoreElements()) {
                val jarEntry = jarEntries.nextElement()
                if (jarEntry.isDirectory || !jarEntry.name.endsWith(".class")) {
                    continue
                }
                // -6 because of .class
                var className = jarEntry.name.substring(0, jarEntry.name.length - 6)
                className = className.replace('/', '.')
                try {
                    val loadedClass = classLoader.loadClass(className)
                    loadedClasses.add(loadedClass)
                } catch (e: ClassNotFoundException) {
                    logger.error("Method library initialization error. Failed to load class: \"$className\"", e)
                }
            }
        } catch (e: IOException) {
            logger.error("Method library initialization error. Failed to load jar: \"$jarPath\"", e)
        }
        return loadedClasses
    }
}
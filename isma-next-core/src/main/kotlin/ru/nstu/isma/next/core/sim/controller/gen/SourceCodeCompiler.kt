package ru.nstu.isma.next.core.sim.controller.gen

import org.slf4j.LoggerFactory
import ru.nstu.isma.generate.MemoryFileManager
import ru.nstu.isma.generate.MemoryJavaFileObject
import java.util.*
import javax.tools.JavaFileManager
import javax.tools.JavaFileObject
import javax.tools.ToolProvider

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class SourceCodeCompiler<T> {
    fun compile(packageName: String, className: String, sourceCode: String?): T {
        val compiler = ToolProvider.getSystemJavaCompiler()
        val manager: JavaFileManager = MemoryFileManager(compiler.getStandardFileManager(null, null, null))
        val options: MutableList<String> = ArrayList()
        options.addAll(Arrays.asList("-classpath", System.getProperty("java.class.path")))
        val files: MutableList<JavaFileObject> = ArrayList()
        files.add(MemoryJavaFileObject(className, sourceCode))
        compiler.getTask(null, manager, null, options, null, files).call()
        val classLoader = manager.getClassLoader(null)
        val classQualifiedName = "$packageName.$className"
        return try {
            classLoader.loadClass(classQualifiedName).getDeclaredConstructor().newInstance() as T
        } catch (e: InstantiationException) {
            logger.error("Failed to compile $classQualifiedName", e)
            throw RuntimeException(e)
        } catch (e: ClassNotFoundException) {
            logger.error("Failed to compile $classQualifiedName", e)
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            logger.error("Failed to compile $classQualifiedName", e)
            throw RuntimeException(e)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SourceCodeCompiler::class.java)
    }
}
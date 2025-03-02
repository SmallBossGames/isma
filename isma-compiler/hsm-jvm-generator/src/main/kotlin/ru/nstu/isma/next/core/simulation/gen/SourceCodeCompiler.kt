package ru.nstu.isma.next.core.simulation.gen

import org.slf4j.LoggerFactory
import ru.nstu.isma.generate.MemoryFileManager
import ru.nstu.isma.generate.MemoryJavaFileObject
import javax.tools.ToolProvider

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class SourceCodeCompiler<T> {
    @Suppress("UNCHECKED_CAST")
    fun compile(packageName: String, className: String, sourceCode: String?): T {
        val compiler = ToolProvider.getSystemJavaCompiler()
        val manager = MemoryFileManager(compiler.getStandardFileManager(null, null, null))
        val options = mutableListOf(
            "-p", System.getProperty("jdk.module.path"),
            "--add-modules=isma.isma.intg.api.main",
            "--add-modules=isma.isma.next.core.simulation.gen.main",
        )
        val files = arrayListOf(MemoryJavaFileObject(className, sourceCode))
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
package ru.nstu.isma.compiler.hsm.jvm

import org.slf4j.LoggerFactory
import ru.nstu.isma.compiler.hsm.jvm.utils.MemoryFileManager
import ru.nstu.isma.compiler.hsm.jvm.utils.MemoryJavaFileObject
import java.io.File
import javax.tools.ToolProvider

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class SourceCodeCompiler<T> {
    private val compiler: javax.tools.JavaCompiler by lazy {
        ToolProvider.getSystemJavaCompiler()
            ?: throw IllegalStateException("System JavaCompiler not available")
    }

    @Suppress("UNCHECKED_CAST")
    fun compile(packageName: String, className: String, sourceCode: String?): T {
        val manager = MemoryFileManager(compiler.getStandardFileManager(null, null, null))
        val options = mutableListOf("-classpath", runtimeClasspath())
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
    /**
     * Builds the classpath for the in-memory javac. In classpath mode this is
     * [java.class.path]; in module mode that property is empty, so the module
     * path entries are expanded (jars inside directories are listed explicitly)
     * and passed as classpath entries instead.
     */
    private fun runtimeClasspath(): String {
        val parts = mutableListOf<String>()
        System.getProperty("java.class.path")?.takeIf { it.isNotBlank() }?.let { parts.add(it) }
        System.getProperty("jdk.module.path")?.takeIf { it.isNotBlank() }
            ?.split(File.pathSeparator)
            ?.forEach { entry ->
                val file = File(entry)
                if (file.isDirectory) {
                    file.listFiles { f -> f.name.endsWith(".jar") }?.forEach { parts.add(it.absolutePath) }
                } else {
                    parts.add(entry)
                }
            }
        return parts.joinToString(File.pathSeparator)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SourceCodeCompiler::class.java)
    }
}
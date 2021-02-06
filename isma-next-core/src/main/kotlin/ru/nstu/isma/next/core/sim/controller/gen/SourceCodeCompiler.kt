package ru.nstu.isma.next.core.sim.controller.gen

import ru.nstu.isma.intg.api.calcmodel.DifferentialEquation
import ru.nstu.isma.intg.api.calcmodel.AlgebraicEquation
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup.StepChoiceRule
import java.lang.IllegalStateException
import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup
import java.util.stream.Collectors
import common.IndexMapper
import ru.nstu.isma.core.hsm.HSM
import common.JavaClassBuilder
import javax.tools.JavaFileManager
import java.util.Arrays
import javax.tools.JavaFileObject
import java.lang.RuntimeException
import java.lang.ClassNotFoundException
import java.lang.IllegalAccessException
import java.util.HashMap
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import common.IndexProvider
import org.apache.commons.lang3.text.StrSubstitutor
import org.slf4j.LoggerFactory
import ru.nstu.isma.generate.MemoryFileManager
import ru.nstu.isma.generate.MemoryJavaFileObject
import java.util.ArrayList
import javax.tools.ToolProvider

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class SourceCodeCompiler<T> {
    fun compile(packageName: String, className: String, sourceCode: String?, printToConsole: Boolean): T {
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
            classLoader.loadClass(classQualifiedName).newInstance() as T
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
package ru.nstu.isma.hsm.common

import ru.nstu.isma.generate.MemoryFileManager
import ru.nstu.isma.generate.MemoryJavaFileObject
import ru.nstu.isma.hsm.HSM
import java.util.*
import javax.tools.JavaFileManager
import javax.tools.JavaFileObject
import javax.tools.ToolProvider

/**
 * Created by Bessonov Alex
 * on 14.03.2015.
 */
abstract class ClassBuilder<T> {
    var modelContext: IndexMapper? = null
        protected set
    var classLoader: ClassLoader? = null
        protected set

    protected constructor() {}
    constructor(modelContext: IndexMapper?) {
        this.modelContext = modelContext
    }

    constructor(hsm: HSM) {
        modelContext = IndexMapper(hsm)
    }

    abstract fun getJavaString(name: String): String?
    protected fun build(name: String, pack: String, printJava: Boolean): T? {
        val instance: T?
        try {
            val content = getJavaString(name)
            if (printJava) {
                println("======= JAVA MODEL ===============")
                println(content)
                println("======= ========== ===============")
            }
            val compiler = ToolProvider.getSystemJavaCompiler()
            val manager: JavaFileManager = MemoryFileManager(compiler.getStandardFileManager(null, null, null))
            val options: MutableList<String> = ArrayList()
            options.addAll(Arrays.asList("-classpath", System.getProperty("java.class.path")))
            val files: MutableList<JavaFileObject> = ArrayList()
            files.add(MemoryJavaFileObject(name, content))
            compiler.getTask(null, manager, null, options, null, files).call()
            classLoader = manager.getClassLoader(null)
            instance = classLoader!!.loadClass(pack + name).newInstance() as T
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException(e)
            // todo errorlist
        }
        return instance
    }
}
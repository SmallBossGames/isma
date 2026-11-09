package ru.nstu.isma.lisma.runtime

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.compiler.hsm.jvm.calcmodel.HybridSystem
import java.io.ByteArrayInputStream
import java.io.File
import java.io.ObjectInputStream

/**
 * A LISMA model loaded from a compiled `.class` file, ready to be launched.
 *
 * [hybridSystem] is the instantiated analyzed hybrid-system class; [hsm] is the
 * deserialized model (equation metadata, initial conditions, column naming);
 * [equationIndexProvider] maps equation names to indices.
 */
public class CompiledLismaModel(
    public val hybridSystem: HybridSystem,
    public val hsm: HSM,
) {
    public val equationIndexProvider: EquationIndexProvider = EquationIndexProvider(hsm)
}

/**
 * Minimal public API for launching a model from a compiled `.class` file
 * (produced by the LISMA compiler, with the HSM inlined as a serialized blob).
 *
 * Each [load] defines the class in a fresh class loader (parented to the loader
 * that sees the calcmodel types) because every compiled model produces the same
 * fully-qualified class name.
 */
public object LismaModelRuntime {

    /**
     * Loads a model from the bytes of a compiled `.class` file.
     *
     * @throws IllegalStateException if the class cannot be defined or instantiated.
     */
    public fun load(classBytes: ByteArray): CompiledLismaModel {
        val loader = object : ClassLoader(HybridSystem::class.java.classLoader) {
            fun defineGeneratedClass(): Class<*> =
                defineClass(GENERATED_CLASS_NAME, classBytes, 0, classBytes.size)
        }

        return try {
            val clazz = loader.defineGeneratedClass()
            val hybridSystem = clazz
                .getDeclaredConstructor()
                .newInstance() as HybridSystem
            val hsmBytes = clazz.getMethod("ismaHsmBytes").invoke(null) as ByteArray
            CompiledLismaModel(hybridSystem, deserializeHsm(hsmBytes))
        } catch (e: Exception) {
            throw IllegalStateException("Failed to load compiled hybrid-system class", e)
        }
    }

    /** Loads a model from a compiled `.class` file on disk. */
    public fun load(file: File): CompiledLismaModel = load(file.readBytes())

    private fun deserializeHsm(bytes: ByteArray): HSM =
        ObjectInputStream(ByteArrayInputStream(bytes)).use { it.readObject() as HSM }

    private const val GENERATED_CLASS_NAME = "ru.nstu.isma.core.simulation.controller.AnalyzedHybridSystem"
}

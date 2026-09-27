package ru.nstu.isma.domain.compiler

/**
 * A compiled LISMA model: the self-contained compiled-model `.class` file
 * produced by the standalone LISMA compiler CLI. The `.class` is the analyzed
 * hybrid-system class with the translated HSM inlined as a serialized blob
 * (`__ISMA_HSM__`); it is the unit handed to the simulation runner CLI.
 *
 * The daemon stores the bytes opaquely (it does not parse them) and passes
 * them to the runner by writing them to a file.
 */
data class CompiledModel(
    val artifact: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CompiledModel) return false
        return artifact.contentEquals(other.artifact)
    }

    override fun hashCode(): Int = artifact.contentHashCode()
}

interface ICompiledModelStore {
    fun create(model: CompiledModel): String
    fun get(id: String): CompiledModel?
    fun delete(id: String): Boolean
    fun exists(id: String): Boolean
}

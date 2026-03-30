package ru.isma.next.external

import ru.isma.next.domain.models.IEquationIndexProvider
import ru.isma.next.domain.models.SimulationMetadata

class BinaryEquationIndexProvider(
    metadata: SimulationMetadata,
) : IEquationIndexProvider {
    private val columnNames: List<String> = metadata.columnNames

    private val deCount: Int
    private val aeCount: Int

    init {
        val firstAeIdx = columnNames.indexOfFirst { it.startsWith("AE_") }
        val firstFIdx = columnNames.indexOfFirst { it.startsWith("f") }

        deCount = if (firstAeIdx > 0) firstAeIdx - 1 else if (firstFIdx > 0) firstFIdx - 1 else columnNames.size - 1
        aeCount = if (firstAeIdx > 0 && firstFIdx > 0) firstFIdx - firstAeIdx else 0
    }

    override fun getDifferentialEquationCode(index: Int): String? {
        val idx = 1 + index
        return columnNames.getOrNull(idx)
    }

    override fun getAlgebraicEquationCode(index: Int): String? {
        val idx = 1 + deCount + index
        return columnNames.getOrNull(idx)
    }

    override fun getDifferentialEquationCount(): Int = deCount

    override fun getAlgebraicEquationCount(): Int = aeCount
}

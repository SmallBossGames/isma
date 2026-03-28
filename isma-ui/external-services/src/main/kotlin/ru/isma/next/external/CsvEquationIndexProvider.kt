package ru.isma.next.external

import ru.isma.next.domain.models.IEquationIndexProvider
import ru.nstu.isma.intg.api.utilities.CsvMetadata

class CsvEquationIndexProvider(
    private val metadata: CsvMetadata,
) : IEquationIndexProvider {
    private val variableNames: List<String> = metadata.variableNames

    override fun getDifferentialEquationCode(index: Int): String? =
        variableNames.getOrNull(index)

    override fun getAlgebraicEquationCode(index: Int): String? =
        variableNames.getOrNull(metadata.yForDeCount + index)

    override fun getDifferentialEquationCount(): Int = metadata.yForDeCount
    override fun getAlgebraicEquationCount(): Int = metadata.rhsForAeCount
}

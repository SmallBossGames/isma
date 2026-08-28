package ru.isma.next.domain.models

data class SimulationResult(
    val data: ByteArray,
    val simulationId: Long,
) {
    override fun equals(other: Any?): Boolean = other is SimulationResult && data.contentEquals(other.data)
    override fun hashCode(): Int = data.contentHashCode()
}

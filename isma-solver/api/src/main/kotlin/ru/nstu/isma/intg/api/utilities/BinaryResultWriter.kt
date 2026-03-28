package ru.nstu.isma.intg.api.utilities

import ru.nstu.isma.intg.api.models.IntgResultPoint
import java.io.DataOutputStream
import java.io.OutputStream

object BinaryResultWriter {
    fun writeHeader(outputStream: OutputStream, columnNames: List<String>) {
        val dos = DataOutputStream(outputStream)
        dos.writeShort(columnNames.size)

        for (name in columnNames) {
            val bytes = name.toByteArray(Charsets.UTF_8)
            dos.writeShort(bytes.size)
            dos.write(bytes)
        }
    }

    fun writePoint(dos: DataOutputStream, point: IntgResultPoint) {
        dos.writeDouble(point.x)
        for (v in point.yForDe) {
            dos.writeDouble(v)
        }
        val rhsDe = point.rhs[0]
        for (v in rhsDe) {
            dos.writeDouble(v)
        }
        val rhsAe = point.rhs[1]
        for (v in rhsAe) {
            dos.writeDouble(v)
        }
    }
}

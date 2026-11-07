package ru.nstu.isma.runner

import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.OutputStream

/**
 * Streams simulation result rows to an [OutputStream] in the ISMA exchange
 * binary format.
 *
 * The stream layout is a metadata header (the column count, then each column
 * name as a length-prefixed UTF-8 string) followed by one row per point (each
 * value as a big-endian double). The header is written and flushed in the
 * constructor, before the first row, so a reader learns the column layout
 * immediately. Rows are buffered and flushed as the buffer fills (and on
 * [close]), so a reader consumes rows incrementally — and derives progress
 * from the TIME column — in chunks rather than paying a syscall per row.
 *
 * The underlying [out] is flushed but never closed: the caller owns its
 * lifetime.
 */
class ResultWriter(
    out: OutputStream,
    columnNames: List<String>,
) {
    private val data = DataOutputStream(BufferedOutputStream(out))

    init {
        data.writeShort(columnNames.size)
        for (name in columnNames) {
            val bytes = name.toByteArray(Charsets.UTF_8)
            data.writeShort(bytes.size)
            data.write(bytes)
        }
        data.flush()
    }

    /** Appends one result row to the buffer; it is flushed as the buffer fills. */
    fun write(point: DoubleArray) {
        for (value in point) {
            data.writeDouble(value)
        }
    }

    /** Flushes any buffered data. Does not close the underlying [out]. */
    fun close() {
        data.flush()
    }
}

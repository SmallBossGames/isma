package ru.nstu.isma.next.core.sim.fdm

import java.util.*

/**
 * by
 * Bessonov Alex.
 * Date: 13.12.13 Time: 1:43
 * Итератор перебора всех вариантов индекса
 */
class FDMIndexIterator {
    private val indexes = ArrayList<FDMIndexedApxVar>()
    private val nameMapping: MutableMap<String, FDMIndexedApxVar> = HashMap()
    var readyToFight = false
    fun addIndex(v: FDMIndexedApxVar) {
        indexes.add(v)
        nameMapping[v.code] = v
        readyToFight = false
    }

    fun start(): List<FDMIndexedApxVar> {
        for (v in indexes) {
            v.setIndex(1)
        }
        readyToFight = true
        return indexes
    }

    operator fun next(): List<FDMIndexedApxVar> {
        if (!readyToFight) {
            throw RuntimeException("Iterator not ready! Call 'start()' method before!")
        }
        // предотвращаем выход за границу
        if (atEnd()) {
            throw RuntimeException("Iterator at the end!")
        }
        var curIdx = 0
        var success = false
        var cur: FDMIndexedApxVar
        while (!success) {
            cur = indexes[curIdx]
            cur.inc()
            if (!cur.isValid) {
                cur.setIndex(1)
                curIdx++
            } else {
                success = true
            }
        }
        return indexes
    }

    fun get(): ArrayList<FDMIndexedApxVar> {
        return indexes
    }

    fun getIndex(code: String): FDMIndexedApxVar? {
        return nameMapping[code]
    }

    fun atEnd(): Boolean {
        for (v in indexes) {
            if (!v.isMax) {
                return false
            }
        }
        return true
    }
}
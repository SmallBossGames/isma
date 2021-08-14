package ru.isma.next.math.engine.shared

class Vector1D(val size: Int) {
    private val innerArray = DoubleArray(size)

    val indices get() = innerArray.indices

    operator fun get(x: Int) = innerArray[x]
    operator fun set(x: Int, value: Double) {
        innerArray[x] = value
    }

    operator fun plusAssign(other: Vector1D){
        for (i in innerArray.indices)
            innerArray[i] += other.innerArray[i]
    }

    operator fun minusAssign(other: Vector1D){
        for (i in innerArray.indices)
            innerArray[i] -= other.innerArray[i]
    }

    operator fun timesAssign (constant: Double){
        for (i in innerArray.indices)
            innerArray[i] *= constant
    }

    operator fun divAssign (constant: Double){
        for (i in innerArray.indices)
            innerArray[i] /= constant
    }
}
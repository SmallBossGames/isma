package ru.isma.next.math.engine.shared

typealias StationaryODE = (y: DoubleArray, outF: DoubleArray) -> Unit
typealias NonStationaryODE = (t:Double, y: DoubleArray, outF: DoubleArray) -> Unit


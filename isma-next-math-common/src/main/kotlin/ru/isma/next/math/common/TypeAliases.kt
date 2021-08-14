package ru.isma.next.math.common

typealias StationaryODE = (y: DoubleArray, outF: DoubleArray) -> Unit
typealias NonStationaryODE = (t:Double, y: DoubleArray, outF: DoubleArray) -> Unit


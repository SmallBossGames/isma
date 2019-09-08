package ru.nstu.grin.converters

interface Converter<In, Out> {

    fun convert(source: In): Out
}
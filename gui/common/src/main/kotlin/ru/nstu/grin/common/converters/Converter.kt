package ru.nstu.grin.common.converters

interface Converter<In, Out> {

    fun convert(source: In): Out
}
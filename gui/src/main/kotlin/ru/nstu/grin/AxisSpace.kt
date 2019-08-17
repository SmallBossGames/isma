package ru.nstu.grin

import ru.nstu.grin.axis.Axis

/**
 * Class represents an abstract definition
 * such as axisSpace
 * @author kostya05983
 */
class AxisSpace {
    /**
     * Набор осей данного пространства функции
     * оси принадлежат определенному месторасположению
     * чтобы совершить маппинг координат, набор координат функции тоже должен маркерироваться
     * специальным положением
     */
    lateinit var axises: List<Axis>
    lateinit var functions: List<Function>

    //Дальнейшая логика первоначальной перерисовки TODO возможно стоит сделать revalidate как в swing framework
    init {

    }
}
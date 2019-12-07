package ru.nstu.grin.controller

import ru.nstu.grin.model.Direction
import ru.nstu.grin.dto.AxisDTO
import ru.nstu.grin.dto.FunctionDTO
import ru.nstu.grin.model.view.ManualEnterFunctionViewModel
import ru.nstu.grin.model.Point
import tornadofx.Controller

/**
 * @author Konstantin Volivach
 */
class ManualEnterFunctionController : Controller() {

    private val model: ManualEnterFunctionViewModel by inject()

    enum class States {
        FIRST_PART,
        SECOND_PART,
        DELIMITER,
        SPACE,
        ERROR_STATE
    }

    enum class LETTERS {
        DIGIT,
        DOT,
        COMMA,
        SPACE
    }

    /**
     * TODO rewrite validators of enters for common usage when read from file
     */
    fun validateFormat(): Boolean {
        val forParse = model.text
        var state = States.FIRST_PART
        val liters = mutableListOf<LETTERS>()
        forParse.forEach { ch ->
            when {
                ch.isDigit() -> {
                    liters.add(LETTERS.DIGIT)
                }
                ch == '.' -> {
                    liters.add(LETTERS.DOT)
                }
                ch == ',' -> {
                    liters.add(LETTERS.COMMA)
                }
                ch == ' ' -> {
                    liters.add(LETTERS.SPACE)
                }
                else -> return false
            }
        }
        liters.forEach {
            when (it) {
                LETTERS.DIGIT -> {
                    when (state) {
                        States.FIRST_PART, States.SECOND_PART -> {
                        }
                        States.DELIMITER -> state = States.SECOND_PART
                        States.SPACE -> state = States.ERROR_STATE
                        States.ERROR_STATE -> {
                            return false
                        }
                    }
                }
                LETTERS.DOT -> {
                    state = when (state) {
                        States.FIRST_PART -> States.DELIMITER
                        States.SECOND_PART, States.DELIMITER, States.SPACE -> States.ERROR_STATE
                        States.ERROR_STATE -> {
                            return false
                        }
                    }
                }
                LETTERS.SPACE -> {
                    when (state) {
                        States.FIRST_PART, States.SECOND_PART -> {
                        }
                        States.DELIMITER, States.SPACE -> state = States.ERROR_STATE
                        else -> return false
                    }
                }
                LETTERS.COMMA -> {
                    when (state) {
                        States.FIRST_PART -> {
                        }
                        States.SECOND_PART -> {
                        }
                        States.DELIMITER -> state = States.ERROR_STATE
                        States.SPACE -> state = States.ERROR_STATE
                        States.ERROR_STATE -> return false
                    }
                }
            }
        }
        return state != States.DELIMITER && state != States.ERROR_STATE
    }

    fun parseFunction(): List<Point> {
        val split = model.text.split(" ")

        val result = mutableListOf<Point>()
        for (item in split) {
            val numbers = item.split(",")
            val point = Point(numbers[0].toDouble(), numbers[1].toDouble())
            result.add(point)
        }
        return result
    }

    fun addFunction(function: List<Point>) {
        val functionDto = FunctionDTO(
            points = function,
            minX = model.minX,
            maxX = model.maxX,
            minY = model.minY,
            maxY = model.maxY,
            xAxis = AxisDTO(
                color = model.xAxisColor,
                delimeterColor = model.xDelimiterColor,
                direction = Direction.valueOf(model.xDirection)

            ),
            yAxis = AxisDTO(
                color = model.yAxisColor,
                delimeterColor = model.yDelimeterColor,
                direction = Direction.valueOf(model.yDirection)
            ),
            functionColor = model.functionColor
        )
        fire(
            AddFunctionEvent(
                functionDTO = functionDto
            )
        )
    }
}
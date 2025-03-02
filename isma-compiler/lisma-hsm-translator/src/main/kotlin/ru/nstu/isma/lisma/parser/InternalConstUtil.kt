package ru.nstu.isma.lisma.parser

import ru.nstu.isma.lisma.analysis.gen.LismaParser.Spatial_var_boundContext
import ru.nstu.isma.lisma.parser.InternalConstUtil
import java.util.*

/**
 * Created by Bessonov Alex
 * on 29.04.2015.
 */
object InternalConstUtil {
    @JvmStatic
    fun getConst(name: String): Double? {
        when (name.uppercase(Locale.getDefault())) {
            "PI" -> return Math.PI
            "E" -> return Math.E
            "G" -> return 9.80665
        }
        return null
    }

    @JvmStatic
    fun getSpatialBound(ctx: Spatial_var_boundContext): Double {
        val o = getConst(ctx.text)
        return o ?: java.lang.Double.valueOf(ctx.text)
    }
}
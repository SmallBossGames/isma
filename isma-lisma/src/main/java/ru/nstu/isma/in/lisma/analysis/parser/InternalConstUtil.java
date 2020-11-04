package ru.nstu.isma.in.lisma.analysis.parser;

import ru.nstu.isma.in.lisma.analysis.gen.LismaParser;

/**
 * Created by Bessonov Alex
 * on 29.04.2015.
 */
public class InternalConstUtil {

    public static Double getConst(String name) {
        switch (name.toUpperCase()) {
            case "PI":
                return Math.PI;
            case "E":
                return Math.E;
            case "G":
                return 9.81;
        }
        return null;
    }

    public static Double getSpatialBound(LismaParser.Spatial_var_boundContext ctx) {
        Double o = getConst(ctx.getText());
        if (o != null)
            return o;
        return Double.valueOf(ctx.getText());
    }

}

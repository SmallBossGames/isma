package ru.nstu.isma.intg.lib;

import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.lib.euler.EulerIntgMethod;
import ru.nstu.isma.intg.lib.rungeKutta.rk22.Rk2IntgMethod;
import ru.nstu.isma.intg.lib.rungeKutta.rk3.IntegrationMethodFactory;
import ru.nstu.isma.intg.lib.rungeKutta.rk3.Rk3IntgMethod;
import ru.nstu.isma.intg.lib.rungeKutta.rk31.Rk31IntgMethod;
import ru.nstu.isma.intg.lib.rungeKutta.rkMerson.RkMersonIntgMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Deprecated
public final class IntgMethodLibrary {
    private static final Map<String, IntgMethod> SYSTEM_METHODS = new HashMap<>();
    private static final Map<String, IntgMethod> USER_METHODS = new HashMap<>();

    private IntgMethodLibrary() {
    }

    public static IntgMethod getIntgMethod(String name) {
        IntgMethod intgMethod = SYSTEM_METHODS.get(name);
        if (intgMethod == null) {
            intgMethod = USER_METHODS.get(name);
        }
        return intgMethod;
    }

    public static boolean isSystemIntgMethod(String name) {
        return SYSTEM_METHODS.containsKey(name);
    }

    public static boolean containsIntgMethod(String name) {
        return getIntgMethod(name) != null;
    }

    public static List<String> getIntgMethodNames() {
        List<String> methodNames = SYSTEM_METHODS.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        List<String> userMethodNames = USER_METHODS.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        methodNames.addAll(userMethodNames);
        return methodNames;
    }

    static void registerIntgMethod(IntegrationMethodFactory intgMethod, boolean system) {
        String intgMethodName = intgMethod.getName();
        if (getIntgMethod(intgMethodName) != null) {
            throw new IllegalArgumentException("Integration method with name \"" + intgMethodName + "\" is already registered.");
        }

        if (system) {
            SYSTEM_METHODS.put(intgMethodName, intgMethod.create());
        } else {
            USER_METHODS.put(intgMethodName, intgMethod.create());
        }
    }

    @Deprecated
    public static IntgMethod createMethod(IntgMethodType type) {
        switch (type) {
            case EULER:
                return new EulerIntgMethod();
            case RUNGE_KUTTA_2:
                return new Rk2IntgMethod();
            case RUNGE_KUTTA_3:
                return new Rk3IntgMethod();
            case RUNGE_KUTTA_31:
                return new Rk31IntgMethod();
            case RUNGE_KUTTA_MERSON:
                return new RkMersonIntgMethod();
            default:
                throw new IllegalStateException("Unknown method type: " + type);
        }
    }

}

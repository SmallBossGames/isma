package ru.nstu.isma.ui.minor.beditor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Bessonov Alex
 * Date: 05.01.14
 * Time: 1:23
 */
public class UIStatic {
    public static final String PROPERTIES_FILE_NAME = "isma.properties";

    public static final String SKIN_THEME_KEY = "skinTheme";
    public static final String SKIN_THEME_DEFAULT = "Default";
    public static final String SKIN_THEME_DARK = "Dark";
    public static final String SKIN_THEME_NEBULA = "Nebula";
    public static final String SKIN_THEME_BUSINESS = "Business";
    public static final List<String> SKIN_THEME_OPTIONS = Arrays.asList(
            SKIN_THEME_DEFAULT, SKIN_THEME_NEBULA, SKIN_THEME_DARK, SKIN_THEME_BUSINESS
    );

    public static final String EDITOR_THEME_KEY = "editorTheme";
    public static final String EDITOR_THEME_DEFAULT = "Default";
    public static final String EDITOR_THEME_DARK = "Dark";
    public static final String EDITOR_THEME_IDEA = "Idea";
    public static final String EDITOR_THEME_ECLIPSE = "Eclipse";
    public static final String EDITOR_THEME_VS = "Visual Studio";
    public static final List<String> EDITOR_THEME_OPTIONS = Arrays.asList(
            EDITOR_THEME_DEFAULT, EDITOR_THEME_DARK, EDITOR_THEME_IDEA, EDITOR_THEME_ECLIPSE, EDITOR_THEME_VS
    );

    public static final Double SIM_START_DEF = 0.;
    public static final String SIM_START_KEY = "simulation.start";

    public static final Double SIM_END_DEF = 10.;
    public static final String SIM_END_KEY = "simulation.end";

    public static final Double SIM_STEP_DEF = 0.1;
    public static final String SIM_STEP_KEY = "simulation.step";

    public static final String SIM_METHOD_DEF = "Euler";
    public static final String SIM_METHOD_KEY = "simulation.method";
}

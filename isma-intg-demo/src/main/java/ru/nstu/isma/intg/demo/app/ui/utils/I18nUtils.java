package ru.nstu.isma.intg.demo.app.ui.utils;

import ru.nstu.isma.intg.demo.app.models.ProblemType;
import ru.nstu.isma.intg.lib.IntgMethodType;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Mariya Nasyrova
 * @since 16.10.2014
 */
public final class I18nUtils {

    private I18nUtils() {
    }

    public static final Locale RU = new Locale("ru", "RU");
    public static final Locale EN = new Locale("en", "EN");

    private static ResourceBundle messages = PropertyResourceBundle.getBundle("i18n/messages");

    public static String getMessage(String key) {
        return messages.getString(key);
    }

    public static String getMessage(String key, String defaultValue) {
        return messages.containsKey(key) ? messages.getString(key) : defaultValue;
    }

    public static Locale getLocale() {
        return Locale.getDefault();
    }

    public static void setLocale(Locale newLocale) {
        Locale.setDefault(newLocale);
        messages = PropertyResourceBundle.getBundle("i18n/messages");
    }

    public static String getProblemTypeName(ProblemType type) {
        return I18nUtils.getMessage("problemType." + type.toString(), type.getName());
    }

    public static String getMethodTypeName(IntgMethodType type) {
        return I18nUtils.getMessage("methodType." + type.toString(), type.getName());
    }

}

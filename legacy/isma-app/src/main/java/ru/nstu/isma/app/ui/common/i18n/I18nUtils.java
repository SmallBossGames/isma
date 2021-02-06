package ru.nstu.isma.app.ui.common.i18n;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Mariya Nasyrova
 * @since 16.10.2014
 */
public final class I18nUtils {

    public static final Locale RU = new Locale("ru", "RU");
    public static final Locale EN = new Locale("en", "EN");
    public static final Locale DEFAULT = EN;

    private static ResourceBundle messages = PropertyResourceBundle.getBundle("i18n/messages");

    private I18nUtils() {
    }

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

}

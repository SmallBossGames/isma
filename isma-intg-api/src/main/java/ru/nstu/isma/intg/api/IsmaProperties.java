package ru.nstu.isma.intg.api;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для работы с файлом настроек ISMA-ы.
 */
// TODO #3: после интеграции и вынесения в слой API для ИСМА перенести get и set для других настроек.
public class IsmaProperties {

    private static final String FILE_NAME = "isma.properties"; // TODO #1: проинтегрировать с UI Static
    private static final String SOLVER = "solver";
    private static final String INTG_RESULT_FILE = "intg.result.file";
    private static final String INTG_RESULT_FILE_SEPARATOR = "intg.result.file.separator";

    private static final IsmaProperties INSTANCE = new IsmaProperties();

    private final Properties properties;

    private IsmaProperties() {
        properties = new Properties();
        load();
    }

    public static IsmaProperties getInstance() {
        return INSTANCE;
    }

    public String getSolver() {
        return properties.getProperty(SOLVER);
    }

    public String getIntgResultFile() {
        return properties.getProperty(INTG_RESULT_FILE);
    }

    public String getIntgResultFileSeparator() {
        return properties.getProperty(INTG_RESULT_FILE_SEPARATOR);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void load() {
        try(InputStream is = this.getClass().getResourceAsStream(FILE_NAME)) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace(); // TODO #2: Добавить логгер
        }
    }

    public void store() {
        try(FileOutputStream out = new FileOutputStream(this.getClass().getResource(FILE_NAME).getFile())) {
            properties.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

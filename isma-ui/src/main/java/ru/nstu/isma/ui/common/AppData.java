package ru.nstu.isma.ui.common;

import error.IsmaErrorList;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceNebulaLookAndFeel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.core.hsm.HSM;
import ru.nstu.isma.core.hsm.service.HSM2TextDumpTranslator;
import ru.nstu.isma.core.sim.fdm.FDMNewConverter;
import ru.nstu.isma.in.InputTranslator;
import ru.nstu.isma.in.lisma.LismaTranslator;
import ru.nstu.isma.in.lisma.service.Lisma2007Converter;
import ru.nstu.isma.intg.lib.IntgMethodLibraryLoader;
import ru.nstu.isma.ui.minor.beditor.UIStatic;
import ru.nstu.isma.ui.minor.beditor.editor.ErrorTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

/**
 * Created by Bessonov Alex
 * Date: 04.01.14
 * Time: 22:26
 */
public class AppData {

    private static final Logger logger = LoggerFactory.getLogger(AppData.class);

    private String text;

    private String dump;

    private IsmaErrorList errors = new IsmaErrorList();

    private HSM model;

    private ErrorTableModel errorTableModel;

    private Properties properties;

    public AppData() {
        errorTableModel = new ErrorTableModel(errors);

        initProperties();

        IntgMethodLibraryLoader methodLibrary = new IntgMethodLibraryLoader();
        methodLibrary.load();
    }

    public IsmaErrorList getErrors() {
        return errors;
    }

    public HSM getModel() {
        return model;
    }

    public void setModel(HSM model) {
        this.model = model;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDump() {
        return dump;
    }

    public String getISMA2007() {
        return Lisma2007Converter.convert(model);
    }

    public ErrorTableModel getErrorTableModel() {
        return errorTableModel;
    }

    public Properties getProperties() {
        return properties;
    }

    private void initProperties() {
        properties = new Properties();
        File f = new File(UIStatic.PROPERTIES_FILE_NAME);
        if (!f.exists()) {
            try {
                properties = getDefaultProperties();
                saveProperties();
            } catch (IOException e) {
                logger.error("Failed to install properties", e);
            }
        }
        loadProperties();
    }

    private Properties getDefaultProperties() throws IOException {
        Properties properties = new Properties();
        properties.setProperty(UIStatic.SKIN_THEME_KEY, UIStatic.SKIN_THEME_DEFAULT);
        properties.setProperty(UIStatic.EDITOR_THEME_KEY, UIStatic.EDITOR_THEME_DEFAULT);
        return properties;
    }

    public void saveProperties() throws IOException {
        FileOutputStream out = new FileOutputStream(UIStatic.PROPERTIES_FILE_NAME);
        properties.store(out, null);
        out.close();
    }

    private void loadProperties() {
        try {
            try (FileInputStream in = new FileInputStream(UIStatic.PROPERTIES_FILE_NAME)) {
                properties.load(in);
            }
        } catch (FileNotFoundException e) {
            logger.error("Properties file \"" + UIStatic.PROPERTIES_FILE_NAME + "\"not found.", e);
        } catch (IOException e) {
            logger.error("Failed to load properties", e);
        }
    }

    public void text2hsm() {
        errors.clear();
        InputTranslator translator = new LismaTranslator(text, errors);
        model = translator.translate();
        dump = HSM2TextDumpTranslator.convert(model);
    }

    public void fdm() {
        FDMNewConverter converter = new FDMNewConverter(model);
        converter.convert();
    }

    public void setLookAndFeel() {
        try {
            String themeName = getProperties().getProperty(UIStatic.SKIN_THEME_KEY, UIStatic.SKIN_THEME_DEFAULT);
            switch (themeName) {
                case UIStatic.SKIN_THEME_DEFAULT:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case UIStatic.SKIN_THEME_DARK:
                    UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
                    break;
                case UIStatic.SKIN_THEME_NEBULA:
                    UIManager.setLookAndFeel(new SubstanceNebulaLookAndFeel());
                    break;
                case UIStatic.SKIN_THEME_BUSINESS:
                    UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
                    break;
            }

            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "ISMA 2015");

            for (Frame frame : JFrame.getFrames()) {
                SwingUtilities.updateComponentTreeUI(frame);
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.error("Failed to setLookAndFeel", e);
        }
    }

    public void applyEditorTheme(RSyntaxTextArea textArea) {
        String editorTheme = getProperties().getProperty(UIStatic.EDITOR_THEME_KEY, UIStatic.EDITOR_THEME_DEFAULT);
        String editorThemeFileName = getEditorThemeFileName(editorTheme);
        try {
            InputStream is = this.getClass().getResourceAsStream("/editor_color_schema/" + editorThemeFileName);
            Theme theme = Theme.load(is);
            theme.apply(textArea);
        } catch (IOException e) {
            logger.error("Failed to applyEditorTheme", e);
        }
    }

    private String getEditorThemeFileName(String editorTheme) {
        switch (editorTheme) {
            case UIStatic.EDITOR_THEME_IDEA:
                return "idea.xml";
            case UIStatic.EDITOR_THEME_DARK:
                return "dark.xml";
            case UIStatic.EDITOR_THEME_DEFAULT:
                return "default.xml";
            case UIStatic.EDITOR_THEME_VS:
                return "vs.xml";
            case UIStatic.EDITOR_THEME_ECLIPSE:
                return "eclipse.xml";
            default:
                return "default.xml";
        }
    }
}

package ru.nstu.isma.app.env.setting;

import org.springframework.stereotype.Service;
import ru.nstu.isma.app.util.Consts;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Bessonov Alex on 17.07.2016.
 */

@Service
public class ApplicationSettingsManager {

    private IsmaSettings ismaSettings;

    private String homePath;

    private String settingsPath;

    private static final String settingsFileName = "settings.xml";

    @PostConstruct
    public void init() throws JAXBException {
        initHomePath();
        loadSettings();
    }

    public void saveSettings() throws JAXBException {
        File settingsFile = new File(settingsPath + settingsFileName);

        JAXBContext jaxbContext = JAXBContext.newInstance(IsmaSettings.class);

        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(ismaSettings, settingsFile);
    }

    public void loadSettings() throws JAXBException {
        File settingsFile = new File(settingsPath + settingsFileName);

        if (!settingsFile.exists())
            createNew();
        else
            ismaSettings = loadSettings(settingsFile);
    }

    private IsmaSettings loadSettings(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(IsmaSettings.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        return (IsmaSettings) jaxbUnmarshaller.unmarshal(file);
    }

    public void createNew() throws JAXBException {
        ismaSettings = new IsmaSettings();
        ismaSettings.setWorkingDir(settingsPath);
        ismaSettings.setMainWindowWidth(920);
        ismaSettings.setMainWindowHeight(600);

        File path = new File(settingsPath);
        path.mkdirs();

        saveSettings();
    }

    private void initHomePath() {
        homePath = System.getProperty("user.dir");

        if (!homePath.endsWith("\\"))
            homePath += "\\";

        settingsPath = homePath + Consts.ISMA_SETTINGS_FOLDER + "\\";
    }

    public String getHomePath() {
        return homePath;
    }

    public IsmaSettings getIsmaSettings() {
        return ismaSettings;
    }
}

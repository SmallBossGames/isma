package ru.nstu.isma.app;

import com.alee.laf.WebLookAndFeel;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.nstu.isma.app.ui.windows.mainwindow.IsmaMainWindow;
import ru.nstu.isma.app.util.SpringContextHolder;
import ru.nstu.isma.intg.lib.IntgMethodLibraryLoader;

import javax.swing.*;
import java.util.Locale;

public class IsmaApplication {
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Загрузка численных методов
        new IntgMethodLibraryLoader().load();

        SwingUtilities.invokeLater(() -> {
            // делаем красоту
            WebLookAndFeel.install();
            WebLookAndFeel.setDecorateFrames(true);
            WebLookAndFeel.setDecorateDialogs(true);

            // настраиваем на локальный jdk
            String jdkPath = System.getProperty("java.home");
            if (jdkPath == null) {
                throw new RuntimeException("java.home property is not defined. ISMA will exit");
            }

            // создаем контекст
            SpringContextHolder holder = new SpringContextHolder();
            holder.setApplicationContext(new ClassPathXmlApplicationContext(new String[]{"spring/isma-app-root-context.xml"}));

            // Загружаем настройки

            // создаем главное окно, провязываем зависимости
            IsmaMainWindow mainWindow = SpringContextHolder.getBean(IsmaMainWindow.class);
            SpringContextHolder.autowire(mainWindow);

            // открываем окно
            mainWindow.setVisible(true);
        });
    }
}

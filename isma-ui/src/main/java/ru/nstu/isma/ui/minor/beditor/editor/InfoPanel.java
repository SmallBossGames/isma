package ru.nstu.isma.ui.minor.beditor.editor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.ui.i18n.I18ChangedListener;
import ru.nstu.isma.ui.i18n.I18nUtils;

/**
 * @author Maria Nasyrova
 * @since 09.10.2015
 */
public class InfoPanel extends JFrame implements I18ChangedListener {

    private static final Logger logger = LoggerFactory.getLogger(InfoPanel.class);
    private static final String ISMA_LOGO_PATH = "/icons/isma-logo.png";

    private JLabel titleLabel;
    private JLabel versionLabel;
    private JLabel lismaLabel;
    private JLabel intgLabel;
    private JLabel slaeLabel;
    private JLabel chiefLabel;
    private JLabel footerLabel;

    public InfoPanel() {
        setSize(450, 550);
        setTitle(I18nUtils.getMessage("menu.helpAboutMenuItem"));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        init();
        centerWindowLocation();
    }

    private void init() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(mainPanel, BorderLayout.CENTER);

        JPanel logoPanel = new JPanel(new BorderLayout());
        BufferedImage logo = loadIsmaLogo();
        if (logo != null) {
            ImageIcon ismaLogo = createCircledIcon(logo);
            logoPanel.add(new JLabel(ismaLogo), BorderLayout.CENTER);
        }
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(logoPanel, BorderLayout.PAGE_START);

        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.PAGE_AXIS));

        titleLabel = createTitleLabel(getTitleText(
                I18nUtils.getMessage("infoPanel.title"),
                I18nUtils.getMessage("infoPanel.subtitle")
        ));
        aboutPanel.add(titleLabel);

        versionLabel = createTitleLabel(I18nUtils.getMessage("infoPanel.version"));
        aboutPanel.add(versionLabel);

        lismaLabel = createContentLabel(getComponentText(
                I18nUtils.getMessage("infoPanel.components.lisma"),
                I18nUtils.getMessage("infoPanel.components.lisma.author"),
                I18nUtils.getMessage("infoPanel.components.lisma.email")
        ));
        aboutPanel.add(lismaLabel);

        intgLabel = createContentLabel(getComponentText(
                I18nUtils.getMessage("infoPanel.components.intg"),
                I18nUtils.getMessage("infoPanel.components.intg.author"),
                I18nUtils.getMessage("infoPanel.components.intg.email")
        ));
        aboutPanel.add(intgLabel);

        slaeLabel = createContentLabel(getComponentText(
                I18nUtils.getMessage("infoPanel.components.slae"),
                I18nUtils.getMessage("infoPanel.components.slae.author"),
                I18nUtils.getMessage("infoPanel.components.slae.email")
        ));
        aboutPanel.add(slaeLabel);

        chiefLabel = createContentLabel(getComponentText(
                I18nUtils.getMessage("infoPanel.components.chief"),
                I18nUtils.getMessage("infoPanel.components.chief.author"),
                I18nUtils.getMessage("infoPanel.components.chief.email")
        ));
        aboutPanel.add(chiefLabel);

        footerLabel = createFooterLabel(getFooterText(
                I18nUtils.getMessage("infoPanel.footer.years"),
                I18nUtils.getMessage("infoPanel.footer.nstuShort"),
                I18nUtils.getMessage("infoPanel.footer.nstuFull"),
                I18nUtils.getMessage("infoPanel.footer.rights")
        ));
        aboutPanel.add(footerLabel);

        mainPanel.add(aboutPanel, BorderLayout.CENTER);
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        return label;
    }

    private JLabel createContentLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return label;
    }

    private JLabel createFooterLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        return label;
    }

    private String getTitleText(String title, String subtitle) {
        final String titleTemplate = "<html><b>${title}</b><br/>${subtitle}</html>";
        return StrSubstitutor.replace(titleTemplate, ImmutableMap.of(
                "title", title,
                "subtitle", subtitle
        ));
    }

    private String getComponentText(String component, String author, String email) {
        final String authorTemplate = "<html>${component}<br/>${author}<br/>email: ${email}</html>";
        return StrSubstitutor.replace(authorTemplate, ImmutableMap.of(
                "component", component,
                "author", author,
                "email", email
        ));
    }

    private String getFooterText(String years, String nstuShort, String nstuFull, String rights) {
        final String footerTemplate = "<html>\u00a9 ${years} ${nstu_short}.<br/>${nstu_full}<br/>${all_rights_reserved}</html>";
        return StrSubstitutor.replace(footerTemplate, ImmutableMap.of(
                "years", years,
                "nstu_short", nstuShort,
                "nstu_full", nstuFull,
                "all_rights_reserved", rights
        ));
    }

    private BufferedImage loadIsmaLogo() {
        try {
            return ImageIO.read(this.getClass().getResourceAsStream(ISMA_LOGO_PATH));
        } catch (IOException e) {
            logger.warn("Failed to load isma logo", e);
            return null;
        }
    }

    private ImageIcon createCircledIcon(BufferedImage image) {
        int diameter = Math.min(image.getWidth(), image.getHeight());
        BufferedImage mask = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        applyQualityRenderingHints(g2d);
        g2d.fillOval(0, 0, diameter - 1, diameter - 1);
        g2d.dispose();

        BufferedImage masked = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        g2d = masked.createGraphics();
        applyQualityRenderingHints(g2d);
        int x = (diameter - image.getWidth()) / 2;
        int y = (diameter - image.getHeight()) / 2;
        g2d.drawImage(image, x, y, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g2d.drawImage(mask, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(masked);
    }

    private void applyQualityRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    private void centerWindowLocation() {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        // Move the window
        this.setLocation(x, y);
    }

    @Override
    public void i18Changed(Locale newLocale) {
        setTitle(I18nUtils.getMessage("menu.helpAboutMenuItem"));

        titleLabel.setText(getTitleText(
                I18nUtils.getMessage("infoPanel.title"),
                I18nUtils.getMessage("infoPanel.subtitle")
        ));

        versionLabel.setText(I18nUtils.getMessage("infoPanel.version"));

        lismaLabel.setText(getComponentText(
                I18nUtils.getMessage("infoPanel.components.lisma"),
                I18nUtils.getMessage("infoPanel.components.lisma.author"),
                I18nUtils.getMessage("infoPanel.components.lisma.email")
        ));

        intgLabel.setText(getComponentText(
                I18nUtils.getMessage("infoPanel.components.intg"),
                I18nUtils.getMessage("infoPanel.components.intg.author"),
                I18nUtils.getMessage("infoPanel.components.intg.email")
        ));

        slaeLabel.setText(getComponentText(
                I18nUtils.getMessage("infoPanel.components.slae"),
                I18nUtils.getMessage("infoPanel.components.slae.author"),
                I18nUtils.getMessage("infoPanel.components.slae.email")
        ));

        chiefLabel.setText(getComponentText(
                I18nUtils.getMessage("infoPanel.components.chief"),
                I18nUtils.getMessage("infoPanel.components.chief.author"),
                I18nUtils.getMessage("infoPanel.components.chief.email")
        ));

        footerLabel.setText(getFooterText(
                I18nUtils.getMessage("infoPanel.footer.years"),
                I18nUtils.getMessage("infoPanel.footer.nstuShort"),
                I18nUtils.getMessage("infoPanel.footer.nstuFull"),
                I18nUtils.getMessage("infoPanel.footer.rights")
        ));
    }
}

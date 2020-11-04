package src;

import org.apache.commons.codec.binary.Base64;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;


/**
 * Created by Sknictik on 03.06.14.
 */
public class Main {

    public static void main(String args[]) throws IOException, JDOMException {


        File input = new File("elem/item.elmt");
        Document document;
        if (input.exists()) {
            document = new SAXBuilder().build(input);
        }
        else {
            Element tools = new Element("elements");
            document = new Document(tools);
        }
        Element element = new Element("item");
        element.addContent(new Element("name").setText("Диод"));
        element.addContent(new Element("style").setText("ismaReversedDiode"));

        String encodedImage = encodeImg("img/macros_out_big.png");


        element.addContent(new Element("img").setText(encodedImage));
        element.addContent(new Element("width").setText("50"));
        element.addContent(new Element("height").setText("30"));
        element.addContent(new Element("type").setText("vertex"));

        /*
        //START SCHEMES HERE -----------------------
        Element schemes = new Element("schemes");

        Element scheme1 = new Element("scheme");
        scheme1.addContent(new Element("name").setText("Схема 1"));
        encodedImage = encodeImg("img/image_nagruzka.png");
        scheme1.addContent(new Element("img").setText(encodedImage));
        Element initConditions1 = new Element("initialconds");
        Element params1 = new Element("params");


        final Object[][] INIT_CONDITION_LAP = {
                {"i_d",	0.5934},
                {"i_q",	0.8039}
        };

        for (Object[] val : INIT_CONDITION_LAP) {
            Element cond = new Element("cond");
            cond.addContent(new Element("name").setText((String) val[0]));
            cond.addContent(new Element("value").setText(String.valueOf(val[1])));
            initConditions1.addContent(cond);
        }

        final Object[][] DATA_LAP =     {
                {"r", 0.8},
                {"L", 0.6}
        };

        for (Object[] val : DATA_LAP) {
            Element param = new Element("param");
            param.addContent(new Element("name").setText((String) val[0]));
            param.addContent(new Element("value").setText(String.valueOf(val[1])));
            params1.addContent(param);
        }
        scheme1.addContent(initConditions1);
        scheme1.addContent(params1);
        schemes.addContent(scheme1);


       /* Element scheme2 = new Element("scheme");
        scheme2.addContent(new Element("name").setText("Схема 2"));
        encodedImage = encodeImg("img/line2.jpg");
        scheme2.addContent(new Element("img").setText(encodedImage));
        Element initConditions2 = new Element("initialconds");
        Element params2 = new Element("params");

        Object[][] INIT_CONDITION_LAP2 = {
                {"Teta", 0.1},
                {"Omega", 1},
                {"i_d",	0.847},
                {"i_q",	0.316},
                {"i_f",	6.248},
                {"i_g",	0},
                {"i_h",	0},
                {"u_f",	0.00125},
                {"T_d",	0.394}
        };

        for (Object[] val : INIT_CONDITION_LAP2) {
            Element cond = new Element("cond");
            cond.addContent(new Element("name").setText((String) val[0]));
            cond.addContent(new Element("value").setText(String.valueOf(val[1])));
            initConditions2.addContent(cond);
        }

        Object[][] DATA_LAP2 = {
                {"L_d",	0.25},
                {"L_q",	0.25},
                {"L_ad",0.2},
                {"L_aq",0.2},
                {"L_f",	0.35},
                {"L_g",	0.33},
                {"L_h",	0.33},
                {"r",	0.0026},
                {"r_g",	0.011},
                {"r_f",	0.00025},
                {"r_h",	0.006},
                {"T_j",	10000},
                {"r_C",	0.0004},
                {"L_C",	0.05}
        };

        for (Object[] val : DATA_LAP2) {
            Element param = new Element("param");
            param.addContent(new Element("name").setText((String) val[0]));
            param.addContent(new Element("value").setText(String.valueOf(val[1])));
            params2.addContent(param);
        }

        scheme2.addContent(initConditions2);
        scheme2.addContent(params2);
        schemes.addContent(scheme2);

        element.addContent(schemes);
        */
        //END OF SCHEMES -----------------------
        document.getRootElement().addContent(element);

        // Java 7 try-with-resources statement; use a try/finally
        // block to close the output stream if you're not using Java 7
        try(OutputStream out = new FileOutputStream("item.elmt")) {
            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(document, out);

    }

    }

    private static String encodeImg(String imgPath) {
        String encodedImage = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            URL imgUrl = Main.class.getResource(imgPath);
            BufferedImage img = ImageIO.read(new File(imgUrl.getPath()));
            ImageIO.write(img, "png", baos);
            baos.flush();
            encodedImage = DatatypeConverter.printBase64Binary(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return encodedImage;
    }

}

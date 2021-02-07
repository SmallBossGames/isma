package ru.nstu.isma.ui.common.rsyntaxtextarea.fix;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bessonov Alex
 * on 22.04.14.
 */
public class FixJFlexService {
    String TARGET1 = "/**\n" +
            "   * Refills the input buffer.\n" +
            "   *\n" +
            "   * @return      <code>false</code>, iff there was new input.\n" +
            "   * \n" +
            "   * @exception   java.io.IOException  if any I/O-Error occurs\n" +
            "   */\n" +
            "  private boolean zzRefill() throws java.io.IOException {\n" +
            "\n" +
            "    /* first: make room (if you can) */\n" +
            "    if (zzStartRead > 0) {\n" +
            "      System.arraycopy(zzBuffer, zzStartRead,\n" +
            "                       zzBuffer, 0,\n" +
            "                       zzEndRead-zzStartRead);\n" +
            "\n" +
            "      /* translate stored positions */\n" +
            "      zzEndRead-= zzStartRead;\n" +
            "      zzCurrentPos-= zzStartRead;\n" +
            "      zzMarkedPos-= zzStartRead;\n" +
            "      zzStartRead = 0;\n" +
            "    }\n" +
            "\n" +
            "    /* is the buffer big enough? */\n" +
            "    if (zzCurrentPos >= zzBuffer.length) {\n" +
            "      /* if not: blow it up */\n" +
            "      char newBuffer[] = new char[zzCurrentPos*2];\n" +
            "      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);\n" +
            "      zzBuffer = newBuffer;\n" +
            "    }\n" +
            "\n" +
            "    /* finally: fill the buffer with new input */\n" +
            "    int numRead = zzReader.read(zzBuffer, zzEndRead,\n" +
            "                                            zzBuffer.length-zzEndRead);\n" +
            "\n" +
            "    if (numRead > 0) {\n" +
            "      zzEndRead+= numRead;\n" +
            "      return false;\n" +
            "    }\n" +
            "    // unlikely but not impossible: read 0 characters, but not at end of stream    \n" +
            "    if (numRead == 0) {\n" +
            "      int c = zzReader.read();\n" +
            "      if (c == -1) {\n" +
            "        return true;\n" +
            "      } else {\n" +
            "        zzBuffer[zzEndRead++] = (char) c;\n" +
            "        return false;\n" +
            "      }     \n" +
            "    }\n" +
            "\n" +
            "\t// numRead < 0\n" +
            "    return true;\n" +
            "  }";

    String TARGET2 = "/**\n" +
            "   * Resets the scanner to read from a new input stream.\n" +
            "   * Does not close the old reader.\n" +
            "   *\n" +
            "   * All internal variables are reset, the old input stream \n" +
            "   * <b>cannot</b> be reused (internal buffer is discarded and lost).\n" +
            "   * Lexical state is set to <tt>ZZ_INITIAL</tt>.\n" +
            "   *\n" +
            "   * @param reader   the new input stream \n" +
            "   */\n" +
            "  public final void yyreset(java.io.Reader reader) {\n" +
            "    zzReader = reader;\n" +
            "    zzAtBOL  = true;\n" +
            "    zzAtEOF  = false;\n" +
            "    zzEOFDone = false;\n" +
            "    zzEndRead = zzStartRead = 0;\n" +
            "    zzCurrentPos = zzMarkedPos = 0;\n" +
            "    yyline = yychar = yycolumn = 0;\n" +
            "    zzLexicalState = YYINITIAL;\n" +
            "  }";

    Map<String, String> REPLASE = new HashMap<String, String>();

    private String path;

    public FixJFlexService(String baseDir) {
        init(baseDir);
    }

    public void fix(String fileName) throws IOException {
        String source = readFile(fileName);
        source = source.replace(TARGET1, "");
        source = source.replace(TARGET2, "");
        for (String k : REPLASE.keySet()) {
            source = source.replace(k, REPLASE.get(k));
        }
        File file = Paths.get(path, fileName).toFile();
        FileWriter fw = new FileWriter(file, false);
        fw.write("package ru.nstu.isma.ui.common.rsyntaxtextarea;\n");
        fw.write(source);
        fw.close();
        System.out.println("YAY! File " + fileName + " fixed.");
    }

    private void init(String baseDir) {
		path = Paths.get(baseDir, "src", "main", "java", "ru", "nstu", "isma", "ui", "common", "rsyntaxtextarea").toString();
		REPLASE.put("private char zzBuffer[] = new char[ZZ_BUFFERSIZE];", "private char zzBuffer[];");
		REPLASE.put("zzCurrentPos = zzMarkedPos = zzPushbackPos = s.offset;", "zzCurrentPos = zzMarkedPos = s.offset;");
		REPLASE.put("return new Token();", "return new TokenImpl();");
	}

    private String readFile(String fileName) throws IOException {
        File file =  Paths.get(path, fileName).toFile();
        System.out.println("Try read file: " + file.getAbsolutePath());
        BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), "UTF-8"
                )
        );
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            //variable line does NOT have new-line-character at the end
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }


}

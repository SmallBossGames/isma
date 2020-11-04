package ru.nstu.isma.ui.common.rsyntaxtextarea.fix;

import java.io.IOException;

/**
 * Created by Bessonov Alex
 * on 22.04.14.
 */
public class FixJFlex {
    public static void main(String[] args) throws IOException {
        System.out.println("BaseDir: " + args[0]);
        FixJFlexService fixFlesJava = new FixJFlexService(args[0]);
        fixFlesJava.fix("DumpTokenMaker.java");
        fixFlesJava.fix("LismaTokenMaker.java");
    }
}

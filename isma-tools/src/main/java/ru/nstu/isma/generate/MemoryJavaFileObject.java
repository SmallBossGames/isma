package ru.nstu.isma.generate;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * Created by Bessonov Alex
 * on 10.12.2014.
 */
public class MemoryJavaFileObject extends SimpleJavaFileObject {

    private CharSequence content;

    public MemoryJavaFileObject(String className, CharSequence content) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return content;
    }

}
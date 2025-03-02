package ru.nstu.isma.generate

import java.net.URI
import javax.tools.JavaFileObject
import javax.tools.SimpleJavaFileObject

/**
 * Created by Bessonov Alex
 * on 10.12.2014.
 */
class MemoryJavaFileObject(className: String, private val content: CharSequence?) : SimpleJavaFileObject(
    URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
    JavaFileObject.Kind.SOURCE
) {
    override fun getCharContent(ignoreEncodingErrors: Boolean): CharSequence? {
        return content
    }
}
package ru.nstu.isma.generate

import javax.tools.StandardJavaFileManager
import javax.tools.ForwardingJavaFileManager
import javax.tools.JavaFileManager
import java.security.SecureClassLoader
import kotlin.Throws
import java.lang.ClassNotFoundException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import javax.tools.JavaFileObject
import javax.tools.FileObject
import javax.tools.SimpleJavaFileObject

/**
 * Created by Bessonov Alex
 * on 10.12.2014.
 */
class MemoryJavaClassObject(name: String, kind: JavaFileObject.Kind) :
    SimpleJavaFileObject(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind) {

    protected val stream = ByteArrayOutputStream()

    val bytes: ByteArray
        get() = stream.toByteArray()

    @Throws(IOException::class)
    override fun openOutputStream(): OutputStream {
        return stream
    }
}
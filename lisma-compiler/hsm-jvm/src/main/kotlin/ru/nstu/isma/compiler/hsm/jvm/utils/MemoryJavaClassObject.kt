package ru.nstu.isma.compiler.hsm.jvm.utils

import kotlin.Throws
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URI
import javax.tools.JavaFileObject
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
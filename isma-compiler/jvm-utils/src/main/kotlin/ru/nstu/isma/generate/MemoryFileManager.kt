package ru.nstu.isma.generate

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.security.SecureClassLoader
import javax.tools.*

/**
 * Created by Bessonov Alex
 * on 10.12.2014.
 */
class MemoryFileManager(manager: StandardJavaFileManager?) :
    ForwardingJavaFileManager<StandardJavaFileManager?>(manager) {
    private var obj: MemoryJavaClassObject? = null
    override fun getClassLoader(location: JavaFileManager.Location?): ClassLoader {
        return object : SecureClassLoader() {
            @Throws(ClassNotFoundException::class)
            override fun findClass(name: String): Class<*> {
                val b = obj!!.bytes
                return super.defineClass(name, b, 0, b.size)
            }

            override fun getResourceAsStream(name: String): InputStream? {
                return if (obj!!.name.endsWith(name)) {
                    ByteArrayInputStream(obj!!.bytes)
                } else super.getResourceAsStream(name)
            }
        }
    }

    @Throws(IOException::class)
    override fun getJavaFileForOutput(
        location: JavaFileManager.Location,
        name: String,
        kind: JavaFileObject.Kind,
        sibling: FileObject
    ): JavaFileObject {
        obj = MemoryJavaClassObject(name, kind)
        return obj!!
    }
}
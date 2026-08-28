package ru.nstu.isma.server.app

import java.util.UUID

object ServerPlatform {

    fun isLinux(): Boolean = System.getProperty("os.name")?.contains("linux", ignoreCase = false) == true

    fun isWindows(): Boolean = System.getProperty("os.name")?.contains("windows", ignoreCase = false) == true

    fun defaultSocketDir(): String = System.getProperty("java.io.tmpdir")

    fun defaultSocketFileName(prefix: String): String = "${defaultSocketDir()}/isma-$prefix-${UUID.randomUUID()}.sock"
}

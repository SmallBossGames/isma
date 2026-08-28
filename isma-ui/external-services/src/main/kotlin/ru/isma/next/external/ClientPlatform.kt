package ru.isma.next.external

object ClientPlatform {

    fun isLinux(): Boolean = System.getProperty("os.name")?.contains("linux", ignoreCase = false) == true

    fun isWindows(): Boolean = System.getProperty("os.name")?.contains("windows", ignoreCase = false) == true
}

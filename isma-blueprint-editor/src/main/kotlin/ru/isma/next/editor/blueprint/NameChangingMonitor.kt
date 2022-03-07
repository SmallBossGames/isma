package ru.isma.next.editor.blueprint

import kotlin.math.max

class NameChangingMonitor(private val itemDefaultName: String) {
    private val defaultNameRegex = Regex("^${itemDefaultName} (\\d+)$")

    private val existedNames = HashSet<String>()
    private var nextNameCounter = 1

    fun tryRegister(name: String): Boolean {
        if(existedNames.contains(name)){
            return false
        }
        val defaultNameMatch = defaultNameRegex.matchEntire(name)
        if (defaultNameMatch != null) {
            val (digit) = defaultNameMatch.destructured
            nextNameCounter = max(nextNameCounter, digit.toInt() + 1)
        }
        existedNames.add(name)
        return true
    }

    fun tryUnregister(name: String): Boolean {
        if(!existedNames.contains(name)){
            return false
        }
        existedNames.remove(name)
        return true
    }

    fun createNextDefaultName(): String = "$itemDefaultName $nextNameCounter"
}
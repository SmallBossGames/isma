package ru.isma.next.app.models.projects

import org.koin.core.component.KoinScopeComponent
import java.io.File

interface IProjectModel : KoinScopeComponent {
    var name: String

    var file: File?

    fun dispose()
}

package ru.isma.next.app.models.projects

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import java.io.File

class LismaProjectModel : IProjectModel, KoinScopeComponent {
    override val scope: Scope by lazy { createScope() }

    var lismaText: String = ""

    override var name: String = ""

    override var file: File? = null

    override fun dispose() { scope.close() }
}

package ru.isma.next.app.models.projects

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import ru.isma.next.editor.blueprint.models.BlueprintModel
import java.io.File

class BlueprintProjectModel : IProjectModel, KoinScopeComponent {
    override val scope: Scope by lazy { createScope() }

    var blueprint: BlueprintModel = BlueprintModel.empty

    override var name: String = ""

    override var file: File? = null

    override fun dispose() { scope.close() }
}

package ru.isma.next.app.models.koin

import javafx.scene.Node
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.isma.next.app.models.projects.BlueprintProjectDataProvider
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.LismaProjectDataProvider
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.editor.blueprint.IsmaBlueprintEditor
import ru.isma.next.editor.text.IsmaTextEditor

class IsmaEditorQualifier

val projectsModule = module {
    scope<BlueprintProjectModel> {
        scopedOf(::BlueprintProjectDataProvider)
        scopedOf(::IsmaBlueprintEditor)
        scoped<Node>(named<IsmaEditorQualifier>()) { get<IsmaBlueprintEditor>() }
    }

    scope<LismaProjectModel> {
        scopedOf(::LismaProjectDataProvider)
        scopedOf(::IsmaTextEditor)
        scoped<Node>(named<IsmaEditorQualifier>()) { get<IsmaTextEditor>() }
    }
}
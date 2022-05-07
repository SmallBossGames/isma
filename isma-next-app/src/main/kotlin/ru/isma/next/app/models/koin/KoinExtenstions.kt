package ru.isma.next.app.models.koin

import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.isma.next.app.models.projects.BlueprintProjectDataProvider
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.LismaProjectDataProvider
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.editor.blueprint.IsmaBlueprintEditor
import ru.isma.next.editor.text.IsmaTextEditor

val projectsModule = module {
    scope<BlueprintProjectModel> {
        scopedOf(::BlueprintProjectDataProvider)
        scopedOf(::IsmaBlueprintEditor)
    }

    scope<LismaProjectModel> {
        scopedOf(::LismaProjectDataProvider)
        scopedOf(::IsmaTextEditor)
    }
}
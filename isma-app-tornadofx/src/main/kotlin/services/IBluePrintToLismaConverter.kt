package services

import views.editors.blueprint.models.BlueprintModel

interface IBluePrintToLismaConverter {
    fun convert(blueprintModel: BlueprintModel) : String
}
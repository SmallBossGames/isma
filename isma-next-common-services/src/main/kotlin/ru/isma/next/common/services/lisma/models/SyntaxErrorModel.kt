package ru.isma.next.common.services.lisma.models

data class SyntaxErrorModel(
        override val row: Int,
        override val position: Int,
        override val message: String) : IErrorModel
package ru.isma.next.common.services.lisma.models

interface IErrorModel {
    val row: Int
    val position: Int
    val message: String
}
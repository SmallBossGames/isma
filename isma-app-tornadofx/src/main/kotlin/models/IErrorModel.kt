package models

interface IErrorModel {
    val row: Int
    val position: Int
    val message: String
}
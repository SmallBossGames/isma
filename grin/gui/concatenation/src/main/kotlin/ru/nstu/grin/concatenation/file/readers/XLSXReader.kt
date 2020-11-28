package ru.nstu.grin.concatenation.file.readers

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

class XLSXReader {
    fun read(file: File, sheet: String, excelRange: ExcelRange): List<List<String>> {
        val book = XSSFWorkbook(file)
        val excelSheet = book.getSheet(sheet)
        val result = mutableListOf<List<String>>()

        for (i in excelRange.startRow..excelRange.endRow) {
            val row = excelSheet.getRow(i)
            val tempList = mutableListOf<String>()
            for (j in excelRange.startCell..excelRange.endCell) {
                val cell = row.getCell(j)
                tempList.add(cell.rawValue)
            }
            result.add(tempList)
        }
        return result
    }
}
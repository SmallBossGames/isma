package ru.nstu.grin.concatenation.file.excel

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.FileInputStream

class XLSReader {
    fun read(file: String, sheet: String, excelRange: ExcelRange): List<List<String>> {
        val book = HSSFWorkbook(FileInputStream(file))
        val excelSheet = book.getSheet(sheet)
        val result = mutableListOf<List<String>>()

        for (i in excelRange.startRow..excelRange.endRow) {
            val row = excelSheet.getRow(i)
            val tempList = mutableListOf<String>()
            for (j in excelRange.startCell..excelRange.endCell) {
                val cell = row.getCell(j)
                tempList.add(cell.stringCellValue)
            }
            result.add(tempList)
        }
        return result
    }
}
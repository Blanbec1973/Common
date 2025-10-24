package org.heyner.common.excelfile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

class ExcelReader {
    private static final Logger logger = LogManager.getLogger(ExcelReader.class);
    private final Workbook workbook;
    public ExcelReader(Workbook workbook) {
        this.workbook = workbook;
    }
    public Cell getCell(String sheet, String address) {
        Sheet dataSheet = workbook.getSheet(sheet);
        CellReference cellReference=new CellReference(address);
        return dataSheet.getRow(cellReference.getRow()).getCell(cellReference.getCol());
    }
    public Cell getCell(String sheet, int rowNum, int colNum) {
        Sheet dataSheet = workbook.getSheet(sheet);
        return dataSheet.getRow(rowNum).getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }
    public String getCellValue(String sheet, String address) {
        Sheet dataSheet = workbook.getSheet(sheet);
        CellReference cellReference = new CellReference(address);
        Cell cell = dataSheet.getRow(cellReference.getRow()).getCell(cellReference.getCol());
        if (cell.getCellType() == CellType.STRING) {
            logger.info(cell.getStringCellValue());
        }
        return cell.getStringCellValue();
    }
    public String getCellValue(String sheet, int numCol, int rowNum) {
        Sheet dataSheet = workbook.getSheet(sheet);
        Cell cell = dataSheet.getRow(rowNum).getCell(numCol, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        if (cell.getCellType() == CellType.STRING) {
            logger.info(cell.getStringCellValue());
        }
        return cell.toString();
    }
    public Integer rowCount(String sheet1, int colNum) {
        //Count number of rows with effective data in column colNum.
        Sheet currentSheet = this.workbook.getSheet(sheet1);
        int rowNum = 0;
        for (Row row : currentSheet) {
            Cell cell= row.getCell(colNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                rowNum++;
            }
        }
        logger.info("Number of effective row with data : {}",rowNum);
        return rowNum;
    }
}

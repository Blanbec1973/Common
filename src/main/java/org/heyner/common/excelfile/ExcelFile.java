package org.heyner.common.excelfile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRangeCopier;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelFile implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(ExcelFile.class);
    private final Workbook workbook;
    private final ExcelReader reader;
    private final ExcelWriter writer;
    private final XSSFFormulaEvaluator formulaEvaluator;
    private CellRangeAddress tileRange;
    public static ExcelFile open(String path) throws IOException {
        return new ExcelFile(path, true);
    }
    public static ExcelFile create(String path) throws IOException {
        return new ExcelFile(path, false);
    }
    private ExcelFile(String name, boolean openIfExists) throws IOException {
        File file = new File(name);
        if (openIfExists && file.exists()) {
            logger.info("Opening existing file {}",name);
            try(FileInputStream fileInputStream = new FileInputStream(name)) {
                workbook = new XSSFWorkbook(fileInputStream);
            }
        } else {
            logger.info("Creating new workbook for {}",name);
            workbook = new XSSFWorkbook();
        }
        formulaEvaluator = (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
        reader = new ExcelReader(workbook);
        writer = new ExcelWriter(workbook, name);
    }

    public void evaluateFormulaCell(Cell cell) {
        formulaEvaluator.evaluateFormulaCell(cell);
    }
    public void writeFichierExcel() {
        writer.writeFichierExcel();
    }
    public Cell getCell(String sheet, String address) {
        return reader.getCell(sheet, address);
    }
    public Cell getCell(String sheet, int rowNum, int colNum) {
        return reader.getCell(sheet, rowNum, colNum);
    }
    public String getCellValue(String sheet, String address) {
        return reader.getCellValue(sheet,address);
    }

    public String getCellValue(String sheet, int numCol, int rowNum) {
        return reader.getCellValue(sheet, numCol, rowNum);
    }

    public Workbook getWorkBook() {
        return workbook;
    }

    public void setTileRange(CellRangeAddress tileRange) {this.tileRange = tileRange;}

    public void deleteFirstLineContaining(String sheet, String string) {
        if (string.equals(this.getCellValue(sheet,0,0))) {
            removeRow((XSSFSheet) workbook.getSheet(sheet),0);
            logger.info("First line is deleted");
        }
        else {
            Object cellValue = this.getCellValue(sheet,0,0);
            logger.info("First cell {} <> {} ==> skipping delete first line",
                    cellValue,
                    string);
        }
    }
    public void removeRow(XSSFSheet sheet, int rowIndex) {
        ExcelWriter.removeRow(sheet, rowIndex);
    }

    public void copyRange(ExcelFile excelOut, String sheetIn, String sheetOut) {
        Sheet inputSheet = this.workbook.getSheet(sheetIn);
        Sheet outputSheet = excelOut.workbook.getSheet(sheetOut);

        RangeCopier rangeCopier = new XSSFRangeCopier(inputSheet,outputSheet);
        rangeCopier.copyRange(this.tileRange,excelOut.tileRange);
    }

    public Integer rowCount(String sheet1, int colNum) {
        return reader.rowCount(sheet1,colNum);
    }

    public void close() throws IOException {
        workbook.close();
    }

    public Sheet createSheet(String sheetName) {
        return workbook.createSheet(sheetName);
    }


    public Integer copySheet(Sheet sheetIn, Sheet sheetOut, boolean ignoreFirstLine, int rowOffset) {
        logger.info("Beginning transfer sheet : {}",sheetIn.getSheetName());
        int rowNum = rowOffset;
        for (Row sourceRow : sheetIn) {
            if ((sourceRow.getRowNum()==0 && !ignoreFirstLine) || sourceRow.getRowNum() != 0) {
                logger.debug("Creation line {}",rowNum);
                Row destRow = sheetOut.createRow(rowNum++);
                copyRow(sourceRow, destRow);
            }
        }
        return rowNum;
    }

    private void copyRow(Row sourceRow, Row destRow) {
        for (Cell sourceCell : sourceRow) {
            Cell destCell = destRow.createCell(sourceCell.getColumnIndex());
            copyCell(sourceCell,destCell);
        }
    }

    private void copyCell(Cell sourceCell, Cell destCell) {
        CellType cellType = sourceCell.getCellType();

        switch (cellType)  {
            case NUMERIC -> destCell.setCellValue(sourceCell.getNumericCellValue());
            case FORMULA -> destCell.setCellFormula(sourceCell.getCellFormula());
            default -> destCell.setCellValue(sourceCell.getStringCellValue());
        }
    }
}


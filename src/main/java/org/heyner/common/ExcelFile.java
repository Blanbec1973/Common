package org.heyner.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRangeCopier;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.heyner.common.exceptions.ExcelWriteException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelFile implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(ExcelFile.class);
    private final Workbook workbook;
    private final String name;
    private final XSSFFormulaEvaluator formulaEvaluator;

    private CellRangeAddress tileRange;

    public ExcelFile(String name) throws IOException {
        this.name = name;
        if (new File(name).exists()) {
            logger.info("Opening existing file {}",name);
            try(FileInputStream fileInputStream = new FileInputStream(name)) {
                workbook = new XSSFWorkbook(fileInputStream);
            }
        } else {
            logger.info("Opening new file {}",name);
            workbook = new XSSFWorkbook();
        }
        formulaEvaluator = (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
    }

    public void evaluateFormulaCell(Cell cell) {
        formulaEvaluator.evaluateFormulaCell(cell);
    }
    public void writeFichierExcel() {
        try(FileOutputStream outputStream = new FileOutputStream(this.name)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            logger.error("Error saving Excel file: {}", this.name, e);
            throw new ExcelWriteException("Unable to write Excel file: " + this.name, e);
        }
        logger.info("Saving OK for {}",this.name);

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
    public static void removeRow(XSSFSheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum) {
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
        }
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null) {
                sheet.removeRow(removingRow);
            }
        }
    }

    public void copyRange(ExcelFile excelOut, String sheetIn, String sheetOut) {
        Sheet inputSheet = this.workbook.getSheet(sheetIn);
        Sheet outputSheet = excelOut.workbook.getSheet(sheetOut);

        RangeCopier rangeCopier = new XSSFRangeCopier(inputSheet,outputSheet);
        rangeCopier.copyRange(this.tileRange,excelOut.tileRange);
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


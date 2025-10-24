package org.heyner.common.excelfile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.heyner.common.exceptions.ExcelWriteException;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter {
    private static final Logger logger = LogManager.getLogger(ExcelWriter.class);
    private final Workbook workbook;
    private final String name;
    public ExcelWriter(Workbook workbook, String name) {
        this.workbook = workbook;
        this.name = name;
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
}

package org.heyner.common;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExcelFileTest {
    private final String fileName1 = "target/temp-" + this.getClass().getSimpleName() + "/ClasseurTest.xlsx";
    private final String fileName2 = "target/temp-" + this.getClass().getSimpleName() + "/SuppressionLigne.xlsx";
    private final String newFileName = "target/temp-" + this.getClass().getSimpleName() + "/NewFile.xlsx";
    private final String notDeleteFileName = "target/temp-" + this.getClass().getSimpleName() + "/notDeleteFileName.xlsx";

    @BeforeAll
    void beforeAll() throws IOException {
        TestInitializerFactory.action(this.getClass().getSimpleName());
    }

    @Test
    void testGetCellValue() {
        try(ExcelFile fichierExcel = new ExcelFile(fileName1)) {
            assertEquals("15", fichierExcel.getCellValue("Feuil1", "A1"));
            assertEquals("20", fichierExcel.getCellValue("Feuil1", "D5"));
            assertEquals(10,fichierExcel.getCell("Feuil1","D1").getNumericCellValue());
            assertEquals(20,fichierExcel.getCell("Feuil1",1,3).getNumericCellValue());
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

    }

    @Test
    void testDeleteLine() {
        try (ExcelFile fichierExcel = new ExcelFile(fileName2)) {
            fichierExcel.deleteFirstLineContaining("sheet1", "AR Historic by client");
            fichierExcel.writeFichierExcel();
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        try (ExcelFile fichierExcel1 = new ExcelFile(fileName2)) {
            assertEquals("From Date", fichierExcel1.getCellValue("sheet1", 0, 0));
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void notDeleteLine() {
        try (ExcelFile excelFile=new ExcelFile(notDeleteFileName)) {
            Sheet sheet = excelFile.createSheet("TestSheet");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("toto");
            row.createCell(1).setCellValue(1);
            row.createCell(2).setCellValue("toto");

            excelFile.deleteFirstLineContaining("TestSheet","NotFound");
            excelFile.writeFichierExcel();
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        try (ExcelFile excelFile2 = new ExcelFile(notDeleteFileName)){
            Sheet sheet = excelFile2.getWorkBook().getSheet("TestSheet");

            assertEquals("toto",sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals(1,sheet.getRow(0).getCell(1).getNumericCellValue());
            assertEquals("toto",sheet.getRow(0).getCell(2).getStringCellValue());

        }catch (IOException ex) {
            fail(ex.getMessage());
        }

    }
    @Test
    void testRowCount() {
        try (ExcelFile fichierExcel = new ExcelFile(fileName1))
        {
            assertEquals(5, fichierExcel.rowCount("Feuil1", 3));
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void testNewFile() {
        try (ExcelFile excelFile=new ExcelFile(newFileName)) {
            Sheet sheet = excelFile.createSheet("TestSheet");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue(1);
            row.createCell(1).setCellValue(2);
            row.createCell(2).setCellFormula("A1+B1");
            row.createCell(3).setCellValue("Value");

            excelFile.evaluateFormulaCell(row.getCell(2));
            excelFile.copySheet(sheet, excelFile.createSheet("TestSheet2"),false,0);
            excelFile.evaluateFormulaCell(excelFile.getWorkBook().getSheet("TestSheet2").getRow(0).getCell(2));

            excelFile.writeFichierExcel();
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        try (ExcelFile excelFile2 = new ExcelFile(newFileName)){
            Sheet sheet = excelFile2.getWorkBook().getSheet("TestSheet");
            Sheet sheet2 = excelFile2.getWorkBook().getSheet("TestSheet2");

            assertEquals(1,sheet.getRow(0).getCell(0).getNumericCellValue());
            assertEquals(2,sheet.getRow(0).getCell(1).getNumericCellValue());
            assertEquals(3,sheet.getRow(0).getCell(2).getNumericCellValue());
            assertEquals("Value",sheet.getRow(0).getCell(3).getStringCellValue());

            assertEquals(1,sheet2.getRow(0).getCell(0).getNumericCellValue());
            assertEquals(2,sheet2.getRow(0).getCell(1).getNumericCellValue());
            assertEquals(3,sheet2.getRow(0).getCell(2).getNumericCellValue());
            assertEquals("Value",sheet2.getRow(0).getCell(3).getStringCellValue());

        }catch (IOException ex) {
            fail(ex.getMessage());
        }


    }
}
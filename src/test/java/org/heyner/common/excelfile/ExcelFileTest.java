package org.heyner.common.excelfile;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.TestInitializerFactory;
import org.heyner.common.exceptions.ExcelWriteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
        try(ExcelFile fichierExcel = ExcelFile.open(fileName1)) {
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
        try (ExcelFile fichierExcel = ExcelFile.open(fileName2)) {
            fichierExcel.deleteFirstLineContaining(ExcelConstants.DEFAULT_SHEET, ExcelConstants.AR_HISTORIC_HEADER);
            fichierExcel.writeFichierExcel();
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        try (ExcelFile fichierExcel1 = ExcelFile.open(fileName2)) {
            assertEquals("From Date", fichierExcel1.getCellValue(ExcelConstants.DEFAULT_SHEET, 0, 0));
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void notDeleteLine() {
        try (ExcelFile excelFile=ExcelFile.open(notDeleteFileName)) {
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

        try (ExcelFile excelFile2 = ExcelFile.open(notDeleteFileName)){
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
        try (ExcelFile fichierExcel = ExcelFile.open(fileName1))
        {
            assertEquals(5, fichierExcel.rowCount("Feuil1", 3));
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    void testNewFile() {
        try (ExcelFile excelFile=ExcelFile.create(newFileName)) {
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

        try (ExcelFile excelFile2 = ExcelFile.open(newFileName)){
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
    @Test
    void shouldThrowExcelWriteExceptionOnInvalidPath() throws IOException {
        ExcelFile file = ExcelFile.open("///invalid/path.xlsx");
        assertThrows(ExcelWriteException.class, file::writeFichierExcel);
    }

    @Test
    void testOpenWithNullPath() {
        assertThrows(IllegalArgumentException.class, () -> ExcelFile.open(null));
    }

    @Test
    void testOpenWithEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> ExcelFile.open(""));
    }

    @Test
    void testCreateWithNullPath() {
        assertThrows(IllegalArgumentException.class, () -> ExcelFile.create(null));
    }

    @Test
    void testCreateWithEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> ExcelFile.create(""));
    }

    @Test
    void testGetCellWithNullSheet() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCell(null, "A1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellWithEmptySheet() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCell("", "A1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellWithNullAddress() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCell("Feuil1", null));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellWithEmptyAddress() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCell("Feuil1", ""));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellWithNegativeRow() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCell("Feuil1", -1, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellWithNegativeCol() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCell("Feuil1", 0, -1));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellValueWithNullSheet() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCellValue(null, "A1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellValueWithEmptySheet() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCellValue("", "A1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellValueWithNullAddress() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCellValue("Feuil1", null));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellValueWithEmptyAddress() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCellValue("Feuil1", ""));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellValueWithNegativeRow() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCellValue("Feuil1", -1, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetCellValueWithNegativeCol() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.getCellValue("Feuil1", 0, -1));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testEvaluateFormulaCellWithNull() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.evaluateFormulaCell(null));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDeleteFirstLineContainingWithNullSheet() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.deleteFirstLineContaining(null, "test"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDeleteFirstLineContainingWithEmptySheet() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.deleteFirstLineContaining("", "test"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDeleteFirstLineContainingWithNullString() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.deleteFirstLineContaining("Feuil1", null));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testRemoveRowWithNullSheet() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.removeRow(null, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testRemoveRowWithNegativeRowIndex() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.removeRow((org.apache.poi.xssf.usermodel.XSSFSheet) excelFile.getWorkBook().getSheet("Feuil1"), -1));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCopyRangeWithNullExcelOut() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.copyRange(null, "Feuil1", "Feuil1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCopyRangeWithNullSheetIn() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1);
             ExcelFile excelOut = ExcelFile.create("temp.xlsx")) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.copyRange(excelOut, null, "Feuil1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCopyRangeWithEmptySheetIn() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1);
             ExcelFile excelOut = ExcelFile.create("temp.xlsx")) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.copyRange(excelOut, "", "Feuil1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCopyRangeWithNullSheetOut() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1);
             ExcelFile excelOut = ExcelFile.create("temp.xlsx")) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.copyRange(excelOut, "Feuil1", null));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCopyRangeWithEmptySheetOut() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1);
             ExcelFile excelOut = ExcelFile.create("temp.xlsx")) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.copyRange(excelOut, "Feuil1", ""));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCopyRangeWithoutTileRange() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1);
             ExcelFile excelOut = ExcelFile.create("temp.xlsx")) {
            assertThrows(IllegalStateException.class, () -> excelFile.copyRange(excelOut, "Feuil1", "Feuil1"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testRowCountWithNullSheet() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.rowCount(null, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testRowCountWithEmptySheet() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.rowCount("", 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testRowCountWithNegativeCol() {
        try (ExcelFile excelFile = ExcelFile.open(fileName1)) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.rowCount("Feuil1", -1));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCreateSheetWithNullName() {
        try (ExcelFile excelFile = ExcelFile.create("temp.xlsx")) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.createSheet(null));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCreateSheetWithEmptyName() {
        try (ExcelFile excelFile = ExcelFile.create("temp.xlsx")) {
            assertThrows(IllegalArgumentException.class, () -> excelFile.createSheet(""));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCopySheetWithNullSheetIn() {
        try (ExcelFile excelFile = ExcelFile.create("temp.xlsx")) {
            Sheet sheetOut = excelFile.createSheet("Out");
            assertThrows(IllegalArgumentException.class, () -> excelFile.copySheet(null, sheetOut, false, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCopySheetWithNullSheetOut() {
        try (ExcelFile excelFile = ExcelFile.create("temp.xlsx")) {
            Sheet sheetIn = excelFile.createSheet("In");
            assertThrows(IllegalArgumentException.class, () -> excelFile.copySheet(sheetIn, null, false, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCopySheetWithNegativeOffset() {
        try (ExcelFile excelFile = ExcelFile.create("temp.xlsx")) {
            Sheet sheetIn = excelFile.createSheet("In");
            Sheet sheetOut = excelFile.createSheet("Out");
            assertThrows(IllegalArgumentException.class, () -> excelFile.copySheet(sheetIn, sheetOut, false, -1));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
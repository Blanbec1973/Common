package org.heyner.common.excelfile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRangeCopier;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.heyner.common.exceptions.ExcelWriteException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Classe principale pour la manipulation des fichiers Excel (.xlsx).
 * Fournit des fonctionnalités de lecture, écriture et manipulation avancée des feuilles Excel.
 * Implémente AutoCloseable pour une gestion automatique des ressources.
 *
 * @author Projet Common
 * @version 4.0
 * @since 3.0
 */
@SuppressWarnings("unused")
public class ExcelFile implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(ExcelFile.class);
    public static final String NULL_OU_VIDE = "Le nom de la feuille ne peut pas être null ou vide";
    private final Workbook workbook;
    private final ExcelReader reader;
    private final ExcelWriter writer;
    private final XSSFFormulaEvaluator formulaEvaluator;
    private CellRangeAddress tileRange;

    /**
     * Ouvre un fichier Excel existant en mode lecture/écriture.
     *
     * @param path Le chemin du fichier Excel à ouvrir
     * @return Une nouvelle instance d'ExcelFile
     * @throws IOException si le fichier n'existe pas ou ne peut pas être ouvert
     * @throws IllegalArgumentException si le chemin est null ou vide
     */
    public static ExcelFile open(String path) throws IOException {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier ne peut pas être null ou vide");
        }
        return new ExcelFile(path, true);
    }

    /**
     * Crée un nouveau fichier Excel.
     *
     * @param path Le chemin où créer le fichier Excel
     * @return Une nouvelle instance d'ExcelFile
     * @throws IOException si le fichier ne peut pas être créé
     * @throws IllegalArgumentException si le chemin est null ou vide
     */
    public static ExcelFile create(String path) throws IOException {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier ne peut pas être null ou vide");
        }
        return new ExcelFile(path, false);
    }

    /**
     * Constructeur privé pour l'initialisation interne.
     *
     * @param name Le nom/chemin du fichier
     * @param openIfExists true pour ouvrir un fichier existant, false pour en créer un nouveau
     * @throws IOException en cas d'erreur d'E/S
     */
    private ExcelFile(String name, boolean openIfExists) throws IOException {
        File file = new File(name);
        if (openIfExists && file.exists()) {
            logger.info("Ouverture du fichier existant {}", name);
            try(FileInputStream fileInputStream = new FileInputStream(name)) {
                workbook = new XSSFWorkbook(fileInputStream);
            }
        } else {
            logger.info("Création d'un nouveau classeur pour {}", name);
            workbook = new XSSFWorkbook();
        }
        formulaEvaluator = (XSSFFormulaEvaluator) workbook.getCreationHelper().createFormulaEvaluator();
        reader = new ExcelReader(workbook);
        writer = new ExcelWriter(workbook, name);
    }

    /**
     * Évalue une cellule contenant une formule.
     *
     * @param cell La cellule à évaluer
     * @throws IllegalArgumentException si la cellule est null
     */
    public void evaluateFormulaCell(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("La cellule ne peut pas être null");
        }
        formulaEvaluator.evaluateFormulaCell(cell);
    }

    /**
     * Sauvegarde le fichier Excel sur le disque.
     *
     * @throws ExcelWriteException en cas d'erreur d'écriture
     */
    public void writeFichierExcel() throws ExcelWriteException {
        writer.writeFichierExcel();
    }

    /**
     * Récupère une cellule par son adresse (ex: "A1").
     *
     * @param sheet Le nom de la feuille
     * @param address L'adresse de la cellule (ex: "A1", "B2")
     * @return La cellule correspondante, ou null si elle n'existe pas
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public Cell getCell(String sheet, String address) {
        if (sheet == null || sheet.trim().isEmpty()) {
            throw new IllegalArgumentException(NULL_OU_VIDE);
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse de la cellule ne peut pas être null ou vide");
        }
        return reader.getCell(sheet, address);
    }

    /**
     * Récupère une cellule par ses coordonnées numériques.
     *
     * @param sheet Le nom de la feuille
     * @param rowNum Le numéro de ligne (0-indexé)
     * @param colNum Le numéro de colonne (0-indexé)
     * @return La cellule correspondante, ou null si elle n'existe pas
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public Cell getCell(String sheet, int rowNum, int colNum) {
        if (sheet == null || sheet.trim().isEmpty()) {
            throw new IllegalArgumentException(NULL_OU_VIDE);
        }
        if (rowNum < 0 || colNum < 0) {
            throw new IllegalArgumentException("Les numéros de ligne et colonne doivent être positifs ou nuls");
        }
        return reader.getCell(sheet, rowNum, colNum);
    }

    /**
     * Récupère la valeur d'une cellule sous forme de chaîne.
     *
     * @param sheet Le nom de la feuille
     * @param address L'adresse de la cellule
     * @return La valeur de la cellule sous forme de chaîne
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public String getCellValue(String sheet, String address) {
        if (sheet == null || sheet.trim().isEmpty()) {
            throw new IllegalArgumentException(NULL_OU_VIDE);
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse de la cellule ne peut pas être null ou vide");
        }
        return reader.getCellValue(sheet, address);
    }

    /**
     * Récupère la valeur d'une cellule par ses coordonnées.
     *
     * @param sheet Le nom de la feuille
     * @param numCol Le numéro de colonne (0-indexé)
     * @param rowNum Le numéro de ligne (0-indexé)
     * @return La valeur de la cellule sous forme de chaîne
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public String getCellValue(String sheet, int numCol, int rowNum) {
        if (sheet == null || sheet.trim().isEmpty()) {
            throw new IllegalArgumentException(NULL_OU_VIDE);
        }
        if (rowNum < 0 || numCol < 0) {
            throw new IllegalArgumentException("Les numéros de ligne et colonne doivent être positifs ou nuls");
        }
        return reader.getCellValue(sheet, numCol, rowNum);
    }

    /**
     * Retourne le workbook Apache POI sous-jacent.
     *
     * @return Le workbook POI
     */
    public Workbook getWorkBook() {
        return workbook;
    }

    /**
     * Définit la plage de cellules à utiliser pour les opérations de copie.
     *
     * @param tileRange La plage de cellules à définir
     */
    public void setTileRange(CellRangeAddress tileRange) {
        this.tileRange = tileRange;
    }

    /**
     * Supprime la première ligne si elle contient la chaîne spécifiée.
     *
     * @param sheet Le nom de la feuille
     * @param string La chaîne à rechercher dans la première cellule
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public void deleteFirstLineContaining(String sheet, String string) {
        if (sheet == null || sheet.trim().isEmpty()) {
            throw new IllegalArgumentException(NULL_OU_VIDE);
        }
        if (string == null) {
            throw new IllegalArgumentException("La chaîne de recherche ne peut pas être null");
        }

        if (string.equals(this.getCellValue(sheet, 0, 0))) {
            removeRow((XSSFSheet) workbook.getSheet(sheet), 0);
            logger.info("Première ligne supprimée");
        } else {
            Object cellValue = this.getCellValue(sheet, 0, 0);
            logger.info("Première cellule {} <> {} ==> suppression de la première ligne ignorée",
                    cellValue, string);
        }
    }

    /**
     * Supprime une ligne spécifique d'une feuille.
     *
     * @param sheet La feuille Excel
     * @param rowIndex L'index de la ligne à supprimer (0-indexé)
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public void removeRow(XSSFSheet sheet, int rowIndex) {
        if (sheet == null) {
            throw new IllegalArgumentException("La feuille ne peut pas être null");
        }
        if (rowIndex < 0) {
            throw new IllegalArgumentException("L'index de ligne doit être positif ou nul");
        }
        ExcelWriter.removeRow(sheet, rowIndex);
    }

    /**
     * Copie une plage de cellules entre deux fichiers Excel.
     *
     * @param excelOut Le fichier Excel de destination
     * @param sheetIn Le nom de la feuille source
     * @param sheetOut Le nom de la feuille destination
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public void copyRange(ExcelFile excelOut, String sheetIn, String sheetOut) {
        if (excelOut == null) {
            throw new IllegalArgumentException("Le fichier Excel de destination ne peut pas être null");
        }
        if (sheetIn == null || sheetIn.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la feuille source ne peut pas être null ou vide");
        }
        if (sheetOut == null || sheetOut.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la feuille destination ne peut pas être null ou vide");
        }
        if (tileRange == null) {
            throw new IllegalStateException("La plage de cellules (tileRange) doit être définie avant la copie");
        }

        Sheet inputSheet = this.workbook.getSheet(sheetIn);
        Sheet outputSheet = excelOut.workbook.getSheet(sheetOut);

        RangeCopier rangeCopier = new XSSFRangeCopier(inputSheet, outputSheet);
        rangeCopier.copyRange(this.tileRange, excelOut.tileRange);
    }

    /**
     * Compte le nombre de lignes dans une colonne donnée.
     *
     * @param sheet1 Le nom de la feuille
     * @param colNum Le numéro de colonne (0-indexé)
     * @return Le nombre de lignes, ou null si la feuille n'existe pas
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public Integer rowCount(String sheet1, int colNum) {
        if (sheet1 == null || sheet1.trim().isEmpty()) {
            throw new IllegalArgumentException(NULL_OU_VIDE);
        }
        if (colNum < 0) {
            throw new IllegalArgumentException("Le numéro de colonne doit être positif ou nul");
        }
        return reader.rowCount(sheet1, colNum);
    }

    /**
     * Ferme le fichier Excel et libère les ressources.
     *
     * @throws IOException en cas d'erreur de fermeture
     */
    @Override
    public void close() throws IOException {
        workbook.close();
    }

    /**
     * Crée une nouvelle feuille dans le workbook.
     *
     * @param sheetName Le nom de la nouvelle feuille
     * @return La feuille créée
     * @throws IllegalArgumentException si le nom est null ou vide
     */
    public Sheet createSheet(String sheetName) {
        if (sheetName == null || sheetName.trim().isEmpty()) {
            throw new IllegalArgumentException(NULL_OU_VIDE);
        }
        return workbook.createSheet(sheetName);
    }

    /**
     * Copie le contenu d'une feuille vers une autre.
     *
     * @param sheetIn La feuille source
     * @param sheetOut La feuille destination
     * @param ignoreFirstLine true pour ignorer la première ligne lors de la copie
     * @param rowOffset L'offset de ligne pour le départ de la copie
     * @return Le numéro de la dernière ligne copiée
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public Integer copySheet(Sheet sheetIn, Sheet sheetOut, boolean ignoreFirstLine, int rowOffset) {
        if (sheetIn == null) {
            throw new IllegalArgumentException("La feuille source ne peut pas être null");
        }
        if (sheetOut == null) {
            throw new IllegalArgumentException("La feuille destination ne peut pas être null");
        }
        if (rowOffset < 0) {
            throw new IllegalArgumentException("L'offset de ligne doit être positif ou nul");
        }

        logger.info("Début du transfert de feuille : {}", sheetIn.getSheetName());
        int rowNum = rowOffset;
        for (Row sourceRow : sheetIn) {
            if ((sourceRow.getRowNum() == 0 && !ignoreFirstLine) || sourceRow.getRowNum() != 0) {
                logger.debug("Création de la ligne {}", rowNum);
                Row destRow = sheetOut.createRow(rowNum++);
                copyRow(sourceRow, destRow);
            }
        }
        return rowNum;
    }

    /**
     * Copie une ligne d'une feuille à une autre.
     *
     * @param sourceRow La ligne source
     * @param destRow La ligne destination
     */
    private void copyRow(Row sourceRow, Row destRow) {
        for (Cell sourceCell : sourceRow) {
            Cell destCell = destRow.createCell(sourceCell.getColumnIndex());
            copyCell(sourceCell, destCell);
        }
    }

    /**
     * Copie une cellule d'une ligne à une autre.
     *
     * @param sourceCell La cellule source
     * @param destCell La cellule destination
     */
    private void copyCell(Cell sourceCell, Cell destCell) {
        CellType cellType = sourceCell.getCellType();

        switch (cellType) {
            case NUMERIC -> destCell.setCellValue(sourceCell.getNumericCellValue());
            case FORMULA -> destCell.setCellFormula(sourceCell.getCellFormula());
            default -> destCell.setCellValue(sourceCell.getStringCellValue());
        }
    }
}


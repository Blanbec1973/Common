package org.heyner.common.excelfile;

public final class ExcelConstants {

    private ExcelConstants() {
        // Classe utilitaire, pas d’instanciation
    }

    // Noms de feuilles
    public static final String DEFAULT_SHEET = "sheet1";
    public static final String FUSION_SHEET = "Fusion";
    public static final String DATAS_SHEET = "Datas";

    // Cellules spécifiques
    public static final String PSA_PREFIX_CELL = "B3";
    public static final String ACTIVITY_PREFIX_CELL = "G3";

    // Colonnes utilisées
    public static final int FOREIGN_AMOUNT_COLUMN = 29;

    // Formats
    public static final String DATE_TEMPLATE = "aaaa-mm-jj";

    // Autres
    public static final String AR_HISTORIC_HEADER = "AR Historic by client";
}

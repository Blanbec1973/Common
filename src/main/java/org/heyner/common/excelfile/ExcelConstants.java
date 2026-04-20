package org.heyner.common.excelfile;

/**
 * Constantes utilisées dans les opérations Excel.
 * Cette classe contient toutes les constantes relatives aux noms de feuilles,
 * adresses de cellules et autres valeurs fixes utilisées dans le traitement Excel.
 *
 * @author Projet Common
 * @version 4.0
 * @since 3.0
 */
@SuppressWarnings("unused")
public final class ExcelConstants {

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private ExcelConstants() {
        // Classe utilitaire, pas d'instanciation
    }

    // Noms de feuilles
    /** Nom de la feuille par défaut */
    public static final String DEFAULT_SHEET = "sheet1";
    /** Nom de la feuille de fusion */
    public static final String FUSION_SHEET = "Fusion";
    /** Nom de la feuille de données */
    public static final String DATAS_SHEET = "Datas";

    // Cellules spécifiques
    /** Adresse de la cellule contenant le préfixe PSA */
    public static final String PSA_PREFIX_CELL = "B3";
    /** Adresse de la cellule contenant le préfixe d'activité */
    public static final String ACTIVITY_PREFIX_CELL = "G3";

    // Colonnes utilisées
    /** Numéro de colonne pour les montants étrangers (0-indexé) */
    public static final int FOREIGN_AMOUNT_COLUMN = 29;

    // Formats
    /** Template de format de date */
    public static final String DATE_TEMPLATE = "aaaa-mm-jj";

    // Autres
    /** En-tête pour l'historique AR par client */
    public static final String AR_HISTORIC_HEADER = "AR Historic by client";
}

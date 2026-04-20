package org.heyner.common;

import java.io.File;
import java.nio.file.Path;

/**
 * Classe utilitaire pour les opérations sur les fichiers.
 * Fournit des méthodes statiques pour manipuler les noms de fichiers et les chemins.
 *
 * @author Projet Common
 * @version 4.0
 * @since 3.0
 */
public class FileUtils {

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private FileUtils() {}

    /**
     * Ajoute un suffixe au nom d'un fichier avant son extension.
     * Par exemple : "document.xlsx" avec suffixe "backup" devient "document-backup.xlsx"
     *
     * @param filePath Le chemin du fichier d'origine
     * @param suffix Le suffixe à ajouter (ne doit pas être null ou vide)
     * @return Le nouveau chemin avec le suffixe ajouté, ou null si les paramètres sont invalides
     * @throws IllegalArgumentException si filePath est null ou si suffix est null/vide
     */
    public static String addSuffixToFileName(Path filePath, String suffix) {
        if (filePath == null) {
            throw new IllegalArgumentException("Le chemin du fichier ne peut pas être null");
        }
        if (suffix == null || suffix.trim().isEmpty()) {
            throw new IllegalArgumentException("Le suffixe ne peut pas être null ou vide");
        }

        File file = filePath.toFile();
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');

        // Si le fichier a une extension
        if (dotIndex != -1) {
            String nameWithoutExtension = fileName.substring(0, dotIndex);
            String extension = fileName.substring(dotIndex);
            return file.getParent() + File.separator + nameWithoutExtension + "-" + suffix + extension;
        } else {
            // Si le fichier n'a pas d'extension
            return file.getParent() + File.separator + fileName + "-" + suffix;
        }
    }
}

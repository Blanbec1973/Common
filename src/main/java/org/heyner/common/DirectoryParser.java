package org.heyner.common;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Classe pour analyser un répertoire et trouver les fichiers correspondant à une expression régulière.
 * Utilise l'API NIO.2 pour une analyse efficace des répertoires.
 *
 * @author Projet Common
 * @version 4.0
 * @since 3.0
 */
public class DirectoryParser {
    private final List<Path> matchingFiles;

    /**
     * Constructeur qui analyse un répertoire et trouve tous les fichiers correspondant au regex.
     *
     * @param directoryPath Le chemin du répertoire à analyser (ne doit pas être null)
     * @param regex L'expression régulière pour filtrer les fichiers (ne doit pas être null)
     * @throws IOException si une erreur d'E/S se produit lors de l'accès au répertoire
     * @throws IllegalArgumentException si directoryPath ou regex sont null/vides
     */
    public DirectoryParser(String directoryPath, String regex) throws IOException {
        if (directoryPath == null || directoryPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du répertoire ne peut pas être null ou vide");
        }
        if (regex == null || regex.trim().isEmpty()) {
            throw new IllegalArgumentException("L'expression régulière ne peut pas être null ou vide");
        }

        matchingFiles = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);

        Path directory = Paths.get(directoryPath);
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Le chemin spécifié n'est pas un répertoire valide: " + directoryPath);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) { // Ne traiter que les fichiers réguliers
                    Matcher matcher = pattern.matcher(entry.getFileName().toString());
                    if (matcher.matches()) {
                        matchingFiles.add(entry);
                    }
                }
            }
        }
    }

    /**
     * Retourne la liste des fichiers correspondant au critère de recherche.
     * La liste retournée est non-modifiable pour préserver l'encapsulation.
     *
     * @return Une liste non-modifiable des chemins des fichiers correspondants
     */
    public List<Path> getMatchingFiles() {
        return Collections.unmodifiableList(matchingFiles);
    }

    /**
     * Vérifie si aucun fichier correspondant n'a été trouvé.
     *
     * @return true si la liste des fichiers correspondants est vide, false sinon
     */
    public boolean isEmpty() {
        return matchingFiles.isEmpty();
    }
}

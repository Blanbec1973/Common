package org.heyner.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe utilitaire pour la gestion des paramètres de configuration.
 * Permet de charger et d'accéder aux propriétés depuis des fichiers de configuration.
 *
 * @author Projet Common
 * @version 4.0
 * @since 3.0
 */
public class Parameter {
    private final Properties prop;
    private static final Logger logger = LogManager.getLogger(Parameter.class);

    /**
     * Constructeur qui charge les propriétés depuis un fichier de configuration.
     *
     * @param nomFichier Le nom du fichier de configuration (doit être dans le classpath)
     * @throws IllegalArgumentException si le nom du fichier est null ou vide
     */
    public Parameter(String nomFichier) {
        if (nomFichier == null || nomFichier.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du fichier de configuration ne peut pas être null ou vide");
        }

        prop = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(nomFichier)) {
            if (input == null) {
                logger.error("Fichier de configuration '{}' introuvable dans le classpath", nomFichier);
                return;
            }
            prop.load(input);
            logger.info("Configuration chargée avec succès depuis '{}'", nomFichier);
        } catch (IOException e) {
            logger.error("Erreur lors du chargement de la configuration '{}': {}", nomFichier, e.getMessage(), e);
        }
    }

    /**
     * Récupère la valeur d'une propriété depuis le fichier de configuration.
     *
     * @param nomProperty Le nom de la propriété à récupérer
     * @return La valeur de la propriété, ou null si elle n'existe pas ou si le paramètre est invalide
     */
    public String getProperty(String nomProperty) {
        if (nomProperty == null || nomProperty.trim().isEmpty()) {
            logger.warn("Nom de propriété null ou vide fourni à getProperty()");
            return null;
        }
        return prop.getProperty(nomProperty);
    }

    /**
     * Récupère la version de l'application depuis la configuration.
     *
     * @return La version de l'application, ou null si non définie
     */
    public String getVersion() {
        return prop.getProperty("version");
    }
}

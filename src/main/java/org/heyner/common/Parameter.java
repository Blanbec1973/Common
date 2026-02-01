package org.heyner.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Parameter {
    private final Properties prop;
    private static final Logger logger = LogManager.getLogger(Parameter.class);

    public Parameter(String nomFichier) {
        prop = new Properties();
        boolean loaded = false;

        // 1. Essayer de charger comme fichier externe (chemin absolu ou relatif)
        try (InputStream input = new java.io.FileInputStream(nomFichier)) {
            prop.load(input);
            loaded = true;
        } catch (IOException e) {
            logger.warn("Fichier externe non trouvé ou illisible : " + nomFichier);
        }

        // 2. Fallback : charger depuis le classpath (dans le JAR)
        if (!loaded) {
            try (InputStream input = getClass().getClassLoader().getResourceAsStream(nomFichier)) {
                if (input != null) {
                    prop.load(input);
                } else {
                    logger.error("Impossible de charger le fichier de propriétés : " + nomFichier);
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public String getProperty(String nomProperty) { return prop.getProperty(nomProperty); }
    public String getVersion() { return prop.getProperty("version"); }
}

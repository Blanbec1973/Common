package org.heyner.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Classe utilitaire pour les interactions utilisateur en ligne de commande.
 * Fournit des méthodes pour obtenir des réponses oui/non de l'utilisateur.
 *
 * @author Projet Common
 * @version 4.0
 * @since 3.0
 */
public class PromptUtil {
    private static final Logger logger = LogManager.getLogger(PromptUtil.class);

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private PromptUtil() {}

    /**
     * Demande à l'utilisateur une réponse oui/non.
     * Affiche le message et attend une réponse 'y' (oui) ou 'n' (non).
     *
     * @param message Le message à afficher à l'utilisateur (ne doit pas être null)
     * @return true si l'utilisateur répond 'y', false sinon
     * @throws IllegalArgumentException si le message est null ou vide
     */
    public static boolean getYesOrNoResponse(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Le message ne peut pas être null ou vide");
        }

        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("{} (y/n)", message);
            String response = scanner.nextLine().trim().toLowerCase();
            boolean result = response.equals("y");
            logger.debug("Réponse utilisateur: {} -> {}", response, result);
            return result;
        }
    }
}

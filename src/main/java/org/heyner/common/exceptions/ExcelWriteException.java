package org.heyner.common.exceptions;

/**
 * Exception levée lors d'erreurs d'écriture dans les fichiers Excel.
 * Cette exception est utilisée pour signaler les problèmes survenant lors
 * des opérations d'écriture ou de sauvegarde de fichiers Excel.
 *
 * @author Projet Common
 * @version 4.0
 * @since 3.0
 */
public class ExcelWriteException extends RuntimeException {

    /**
     * Constructeur avec message et cause.
     *
     * @param message Le message d'erreur décrivant le problème
     * @param cause La cause racine de l'exception
     */
    public ExcelWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}

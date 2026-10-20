package tn.esprit.autoloc.service;

import java.time.LocalDate;

/**
 * Règles de gestion réutilisables par les services (classe utilitaire sans état).
 */
final class ValidationUtils {

    private ValidationUtils() {
    }

    static void exigerNonVide(String valeur, String champ) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le champ '" + champ + "' est obligatoire");
        }
    }

    static void exigerNonNull(Object valeur, String champ) {
        if (valeur == null) {
            throw new IllegalArgumentException("Le champ '" + champ + "' est obligatoire");
        }
    }

    static void exigerPositifOuNul(Number valeur, String champ) {
        if (valeur != null && valeur.doubleValue() < 0) {
            throw new IllegalArgumentException("Le champ '" + champ + "' ne doit pas être négatif");
        }
    }

    static void exigerFinApresDebut(LocalDate debut, LocalDate fin) {
        if (debut != null && fin != null && fin.isBefore(debut)) {
            throw new IllegalArgumentException("La date de fin doit être postérieure à la date de début");
        }
    }
}

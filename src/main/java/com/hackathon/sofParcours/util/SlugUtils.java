package com.hackathon.sofParcours.util;

import java.text.Normalizer;

/**
 * Utilitaire pour la normalisation de texte en slug
 */
public class SlugUtils {

    private SlugUtils() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Normalise un texte en slug URL-friendly
     * Ex: "DevOps avancé" → "devops-avance"
     * 
     * @param text Texte à normaliser
     * @return Slug normalisé
     * @throws IllegalArgumentException si le texte est null ou vide
     */
    public static String normalize(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        // Normalisation Unicode (décomposition des accents)
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        
        // Suppression des diacritiques
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        
        // Conversion en minuscules
        normalized = normalized.toLowerCase();
        
        // Remplacement des espaces et caractères spéciaux par des tirets
        normalized = normalized.replaceAll("[^a-z0-9]+", "-");
        
        // Suppression des tirets en début et fin
        normalized = normalized.replaceAll("^-+|-+$", "");
        
        return normalized;
    }

    /**
     * Vérifie si un slug est valide
     * 
     * @param slug Slug à vérifier
     * @return true si valide
     */
    public static boolean isValid(String slug) {
        if (slug == null || slug.isEmpty()) {
            return false;
        }
        return slug.matches("^[a-z0-9]+(-[a-z0-9]+)*$");
    }
}

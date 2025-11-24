package com.hackathon.sofParcours.util;

import java.util.Random;

/**
 * Utilitaire pour générer des codes aléatoires
 */
public class CodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    private CodeGenerator() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Génère un code unique de room (6 caractères alphanumériques)
     * @return Code généré (ex: "ABC123")
     */
    public static String generateRoomCode() {
        return generateCode(6);
    }

    /**
     * Génère un code aléatoire de longueur spécifique
     * @param length Longueur du code
     * @return Code généré
     */
    public static String generateCode(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }

        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}

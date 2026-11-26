package com.gestionemployes.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {

    private static final String ALGORITHME = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 120_000;
    private static final int TAILLE_SEL = 16;
    private static final int TAILLE_HASH_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String hacher(String motDePasse) {
        byte[] sel = new byte[TAILLE_SEL];
        RANDOM.nextBytes(sel);
        byte[] hash = deriver(motDePasse.toCharArray(), sel, ITERATIONS);

        return "pbkdf2$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(sel) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifier(String motDePasse, String stocke) {
        if (motDePasse == null || stocke == null) {
            return false;
        }

        String[] parties = stocke.split("\\$");
        if (parties.length != 4 || !"pbkdf2".equals(parties[0])) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parties[1]);
            byte[] sel = Base64.getDecoder().decode(parties[2]);
            byte[] attendu = Base64.getDecoder().decode(parties[3]);
            byte[] calcule = deriver(motDePasse.toCharArray(), sel, iterations);

            return MessageDigest.isEqual(attendu, calcule);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] deriver(char[] motDePasse, byte[] sel, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(motDePasse, sel, iterations, TAILLE_HASH_BITS);
            return SecretKeyFactory.getInstance(ALGORITHME).generateSecret(spec).getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Hachage du mot de passe impossible", e);
        }
    }

}

package com.iit.cm2601.teammate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class PasswordVerifier {

    public static boolean verifyPassword(String inputPassword, String storedSalt, String storedHash) {
        try {
            byte[] salt = Base64.getDecoder().decode(storedSalt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] computedHash = md.digest(inputPassword.getBytes(StandardCharsets.UTF_8));

            String computedHashString = Base64.getEncoder().encodeToString(computedHash);

            return MessageDigest.isEqual(
                    computedHashString.getBytes(StandardCharsets.UTF_8),
                    storedHash.getBytes(StandardCharsets.UTF_8)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


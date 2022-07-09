package org.example.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public enum HASH_ALGORITHM {
        SHA256("SHA-256");

        private final String name;
        HASH_ALGORITHM(String name) {
            this.name = name;
        }
        public String getname() {
            return name;
        }
    }

    /**
     * concatenate message with salt, digest and repeat
     * @param messageDigest
     * @param repetition number of repetion
     * @param message the message to hash
     * @param salt the salt
     * @return the hashed message
     */
    public static byte[] repeatedHashWithSalt(MessageDigest messageDigest, int repetition, byte[] message, byte[] salt) {
        for (int i = 0; i < repetition; i++) {
            messageDigest.update(message);
            messageDigest.update(salt);
            message = messageDigest.digest();
        }
        return message;
    }

    /**
     * creates a new instance of MessageDigest
     * @param algorithm the message digest algorithm
     * @return a message digest
     * @throws RuntimeException
     */
    public static MessageDigest createMessageDigest(HASH_ALGORITHM algorithm) throws RuntimeException {
        try {
            return MessageDigest.getInstance(algorithm.getname());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

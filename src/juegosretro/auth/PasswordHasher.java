package juegosretro.auth;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    private PasswordHasher() {
    }

    public static HashData hash(String password) {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        byte[] hashed = pbkdf2(password.toCharArray(), salt);
        return new HashData(encode(hashed), encode(salt));
    }

    public static boolean verify(String password, String saltBase64, String expectedHashBase64) {
        byte[] salt = decode(saltBase64);
        byte[] expectedHash = decode(expectedHashBase64);
        byte[] actualHash = pbkdf2(password.toCharArray(), salt);
        return MessageDigest.isEqual(expectedHash, actualHash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo generar el hash de la contraseña.", ex);
        }
    }

    private static String encode(byte[] value) {
        return Base64.getEncoder().encodeToString(value);
    }

    private static byte[] decode(String value) {
        return Base64.getDecoder().decode(value);
    }

    public static final class HashData {
        private final String hash;
        private final String salt;

        public HashData(String hash, String salt) {
            this.hash = hash;
            this.salt = salt;
        }

        public String getHash() {
            return hash;
        }

        public String getSalt() {
            return salt;
        }
    }
}

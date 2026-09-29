package utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Hashes and verifies passwords using PBKDF2WithHmacSHA256.
 *
 * Uses only classes built into the JDK (javax.crypto) — no external library
 * required. Each password gets its own random salt, and the stored value is
 * "salt:hash" (both Base64), which fits comfortably in the existing
 * VARCHAR(255) password column.
 *
 * @author Sean
 */
public final class PasswordUtil
{
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil()
    {
    }

    // Hashes a plaintext password with a freshly generated random salt.
    public static String hash(String plainPassword)
    {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] hash = pbkdf2(plainPassword.toCharArray(), salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    } //hash

    // Verifies a plaintext password against a stored "salt:hash" value.
    public static boolean verify(String plainPassword, String storedValue)
    {
        try
        {
            String[] parts = storedValue.split(":");
            if (parts.length != 2) return false; // not a hash we produced (e.g. old plaintext row)

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
            byte[] actualHash = pbkdf2(plainPassword.toCharArray(), salt);

            return java.security.MessageDigest.isEqual(expectedHash, actualHash); // constant-time comparison
        } catch (IllegalArgumentException ex)
        {
            return false;
        }
    } //verify

    private static byte[] pbkdf2(char[] password, byte[] salt)
    {
        try
        {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex)
        {
            throw new RuntimeException("Password hashing failed", ex);
        }
    } //pbkdf2
} //PasswordUtil

package com.codemaniac.appointment.Util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

  private static final String ALGORITHM = "AES";

  public static boolean isEncrypted(final String value) {
    return value.startsWith("ENC(") && value.endsWith(")");
  }

  // Encrypt a value (only if not already encrypted)
  public static String encrypt(final String secretKey, final String value) {
    try {
      if (isEncrypted(value)) {
        return value; // Skip encryption if already encrypted
      }

      final SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
      final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, keySpec);
      final byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

      return "ENC(" + Base64.getEncoder().encodeToString(encrypted) + ")";
    } catch (final Exception e) {
      throw new RuntimeException("Error while encrypting", e);
    }
  }

  // Decrypt a value
  public static String decrypt(final String secretKey, final String encryptedValue) {
    try {
      if (!isEncrypted(encryptedValue)) {
        return encryptedValue;
      }

      final String base64Encoded = encryptedValue.substring(4, encryptedValue.length() - 1); // Remove ENC(...)
      final SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
      final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, keySpec);

      final byte[] decodedValue = Base64.getDecoder().decode(base64Encoded);
      final byte[] decrypted = cipher.doFinal(decodedValue);

      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (final Exception e) {
      throw new RuntimeException("Error while decrypting", e);
    }
  }
}

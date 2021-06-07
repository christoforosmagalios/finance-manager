package com.github.cmag.financemanager.util;

import com.github.cmag.financemanager.config.AppConstants;
import com.github.cmag.financemanager.util.exception.FinanceManagerException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AES {

  private static final String AES_ECB_PKCS_5_PADDING = "AES/ECB/PKCS5Padding";
  private static final String ALGORITHM = "AES";

  @Value("${finance.manager.aes.secret}")
  private String secret;

  /**
   * Update the secret key with the given one.
   *
   * @param secret The secret key.
   */
  private SecretKeySpec getKey(String secret) {
    try {
      byte[] key = secret.getBytes(StandardCharsets.UTF_8);
      MessageDigest sha = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_512);
      key = sha.digest(key);
      key = Arrays.copyOf(key, 16);
      return new SecretKeySpec(key, ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      log.error("Error setting AES Secret: ", e);
      throw new FinanceManagerException(AppConstants.GENERIC_ERROR);
    }
  }

  /**
   * Encrypt the given string.
   *
   * @param str The string to be encrypted.
   * @return The encrypted representation of the given string.
   */
  public String encrypt(String str) {
    try {
      SecretKeySpec secretKey = getKey(secret);
      Cipher cipher = Cipher.getInstance(AES_ECB_PKCS_5_PADDING);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return Base64.getEncoder()
          .encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    } catch (Exception e) {
      log.error("Error while encrypting: " + e.toString());
    }
    return null;
  }

  /**
   * Decrypt the given string.
   *
   * @param str The string to be decrypted.
   * @return The decrypted representation of the given string.
   */
  public String decrypt(String str) {
    try {
      SecretKeySpec secretKey = getKey(secret);
      Cipher cipher = Cipher.getInstance(AES_ECB_PKCS_5_PADDING);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return new String(cipher.doFinal(Base64.getDecoder().decode(str)));
    } catch (Exception e) {
      log.error("Error while decrypting: " + e.toString());
    }
    return null;
  }
}


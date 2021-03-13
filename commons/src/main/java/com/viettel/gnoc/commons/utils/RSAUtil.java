package com.viettel.gnoc.commons.utils;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Quang.NgoVan
 * @since 2109-05-08
 */
public class RSAUtil {

  private Cipher cipher;
  private String keyStoreLocation;
  private String keyStorePass;
  private String keyStoreAlias;

  public RSAUtil(String keyStoreLocation, String keyStorePass, String keyStoreAlias)
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    this.keyStoreLocation = keyStoreLocation;
    this.keyStorePass = keyStorePass;
    this.keyStoreAlias = keyStoreAlias;
    this.cipher = Cipher.getInstance("RSA");
  }

  /**
   * Get Private Key from KeyStore
   *
   * @return PrivateKey
   */
  private PrivateKey getPrivate() throws Exception {
    Resource resource = new ClassPathResource(keyStoreLocation);
    InputStream inputStream = resource.getInputStream();
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(inputStream, keyStorePass.toCharArray());
    PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyStoreAlias, keyStorePass.toCharArray());
    return privateKey;
  }

  /**
   * Get Private Key from KeyStore
   *
   * @return PrivateKey
   */
  private PublicKey getPublic() throws Exception {
    Resource resource = new ClassPathResource(keyStoreLocation);
    InputStream inputStream = resource.getInputStream();
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(inputStream, keyStorePass.toCharArray());
    Certificate cert = keyStore.getCertificate(keyStoreAlias);
    PublicKey publicKey = cert.getPublicKey();
    return publicKey;
  }

  /**
   * Encrypt text using private key
   *
   * @return String
   */
  public String encryptString(String msg) throws Exception {
    PrivateKey privateKey = getPrivate();
    this.cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    return Base64.encodeBase64String(cipher.doFinal(msg.getBytes("UTF-8")));
  }

  /**
   * Decrypt text using private key
   *
   * @return String
   */
  public String decryptString(String msg) throws Exception {
    PrivateKey privateKey = getPrivate();
    this.cipher.init(Cipher.DECRYPT_MODE, privateKey);
    return new String(cipher.doFinal(Base64.decodeBase64(msg)));
  }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.viettel.gnoc.commons.utils.ws;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class PassProtector {

  private static final String ALGORITHM = "AES";
  private static final int ITERATIONS = 2;
  private static final byte[] keyValue = new byte[]{87, 104, 97, 116, 73, 115, 65, 83, 101, 99, 114,
      101, 116, 75, 101, 121};

  public PassProtector() {
  }

  public static String encrypt(String value, String salt) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance("AES");
    c.init(1, key);
    String valueToEnc = null;
    String eValue = value;

    for (int i = 0; i < 2; ++i) {
      valueToEnc = salt + eValue;
      byte[] encValue = c.doFinal(valueToEnc.getBytes());
      eValue = Base64.getEncoder().encodeToString(encValue);
    }
    return eValue;
  }

  public static String decrypt(String value, String salt) throws Exception {
    Key key = generateKey();
    Cipher c = Cipher.getInstance("AES");
    c.init(2, key);
    String dValue = null;
    String valueToDecrypt = value;

    for (int i = 0; i < 2; ++i) {
      byte[] decordedValue = Base64.getDecoder().decode(valueToDecrypt);
      byte[] decValue = c.doFinal(decordedValue);
      dValue = (new String(decValue)).substring(salt.length());
      valueToDecrypt = dValue;
    }

    return dValue;
  }

  private static Key generateKey() throws Exception {
    Key key = new SecretKeySpec(keyValue, "AES");
    return key;
  }
}

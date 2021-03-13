package com.viettel.gnoc.cr.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

public class SessionIdentifierGenerator {

  private SecureRandom random = new SecureRandom();

  public String nextSessionId() {
    return new BigInteger(130, random).toString(32);
  }

  public String nextUUID() {

    return UUID.randomUUID().toString();
  }
}

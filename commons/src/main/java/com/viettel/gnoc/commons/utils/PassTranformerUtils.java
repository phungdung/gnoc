package com.viettel.gnoc.commons.utils;

import com.viettel.security.PassTranformer;

public class PassTranformerUtils {

  public static void main(String args[]) {
    String url = PassTranformer.encrypt("jdbc:oracle:thin:@10.60.109.209:1521:vt");
    System.out.println("url: " + url);
  }
}

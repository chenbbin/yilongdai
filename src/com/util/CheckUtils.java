package com.util;

import org.apache.http.util.TextUtils;

public class CheckUtils
{
  public static boolean isNum(String str) {
    if (TextUtils.isEmpty(str)) {
      return false;
    }
    return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
  }
}


package com.plugin;

import com.util.Keys;
import com.util.RSAUtil;
import it.sauronsoftware.base64.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;



public class LicenseCheck
{
  public static boolean checkLicense(String account)
    throws Exception
  {
    String filePath = System.getProperty("user.dir");
    filePath = filePath + File.separator + "eloan_license";
    BufferedReader br = new BufferedReader(new FileReader(filePath));
    String line = "";
    StringBuffer buffer = new StringBuffer();
    while ((line = br.readLine()) != null) {
      buffer.append(line);
    }
    String fileContent = buffer.toString();
    String license = fileContent.trim();
    String account2 = RSAUtil.decrypt(RSAUtil.getPrivateKey(Keys.privateKeyL), license);
    account2 = Base64.decode(account2, "GBK");
    return account.equals(account2);
  }
}


/* Location:              D:\cb\eloan\eloan_plugin_10.12.jar!\com\plugin\LicenseCheck.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
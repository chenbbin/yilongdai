package com.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;
import org.apache.http.HttpEntity;

public class ImgRecTianyiTest
{
  private static String DLLPATH = null;
  private static int netIndex = 0;
  
  static
  {
    String filePath = System.getProperty("user.dir");
    DLLPATH = filePath + File.separator + "lib" + File.separator + "CaptchaOCR.dll";
    //DLLPATH=Const.DLLPATH;
    EloanCodeUtils.printlog(DLLPATH);
    netIndex = JPYZM.INSTANCE.VcodeInit("6968289A373F56FEA71A4708373A8E6F");
    EloanCodeUtils.printlog("netIndex:" + netIndex);
  }
  

  public static abstract interface JPYZM
    extends com.sun.jna.win32.StdCallLibrary
  {
    public static final JPYZM INSTANCE = (JPYZM)com.sun.jna.Native.loadLibrary(ImgRecTianyiTest.DLLPATH, JPYZM.class);
    
    public abstract int VcodeInit(String paramString);
    
    public abstract boolean GetVcode(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2);
  }
  
  public static byte[] getContent(String filePath) throws IOException {
    File file = new File(filePath);
    long fileSize = file.length();
    if (fileSize > 2147483647L) {
      EloanCodeUtils.printlog("file too big...");
      return null;
    }
    FileInputStream fi = new FileInputStream(file);
    byte[] buffer = new byte[(int)fileSize];
    int offset = 0;
    int numRead = 0;
    while ((offset < buffer.length) && ((numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0))
    {
      offset += numRead;
    }
    
    if (offset != buffer.length) {
      throw new IOException("Could not completely read file " + file.getName());
    }
    
    fi.close();
    return buffer;
  }
  
  private static byte[] getContent() throws IOException {
    org.apache.http.impl.client.CloseableHttpClient httpclient = org.apache.http.impl.client.HttpClients.createDefault();
    org.apache.http.client.methods.HttpGet httpget = new org.apache.http.client.methods.HttpGet("http://www.eloancn.com/randCode/randCode.jsp?te=" + new Date().getTime());
    org.apache.http.client.methods.CloseableHttpResponse response = httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();
    InputStream fi = entity.getContent();
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer1 = new byte['Ѐ'];
    int len;
    while ((len = fi.read(buffer1)) > -1) {
      baos.write(buffer1, 0, len);
    }
    baos.flush();
    
    InputStream stream1 = new ByteArrayInputStream(baos.toByteArray());
    
    InputStream stream2 = new ByteArrayInputStream(baos.toByteArray());
    
    FileOutputStream fos = new FileOutputStream("E:\\111\\b.png");
    byte[] b = new byte['Ѐ'];
    while (stream1.read(b) != -1) {
      fos.write(b);
    }
    
    long length = entity.getContentLength();
    byte[] buffer = new byte['ஸ'];
    int offset = 0;
    int numRead = 0;
    while ((offset < buffer.length) && ((numRead = stream2.read(buffer, offset, buffer.length - offset)) >= 0))
    {
      offset += numRead;
    }
    



    fi.close();
    return buffer;
  }
  
  public static void main(String[] args) throws IOException
  {
    byte[] bs = getContent();
    getCode(bs);
  }
  


  public static void test(String s)
  {
    s = "11";
  }
  
  public static String getCode(byte[] imgbs) throws IOException
  {
    EloanCodeUtils.printlog("imgbs.length:" + imgbs.length);
    long begin = System.currentTimeMillis();
    
    byte[] code = new byte[4];
    String rtnCode = null;
    boolean result = JPYZM.INSTANCE.GetVcode(netIndex, imgbs, imgbs.length, code);
    EloanCodeUtils.printlog(""+result);
    if (result) {
      long end = System.currentTimeMillis();
      rtnCode = new String(code);
      EloanCodeUtils.printlog("识别时间：" + (end - begin) + "ms 识别结果:" + rtnCode);
    }
    return null;
  }
  
  public static void init() {}
}

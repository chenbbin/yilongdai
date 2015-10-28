package com.util;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;



public class EloanCodeUtils
{
  private static String DLLPATH = null;
  private static int netIndex = 0;
  private static String codeTemp;
  
  public static String getCodeTemp() {
    return codeTemp;
  }
  
  public static void setCodeTemp(String codeTemp) {
    codeTemp = codeTemp;
  }
  public static void printlog(String log){
	   Date date = new Date();
	   SimpleDateFormat dfm = new SimpleDateFormat("HH:mm:ss");
	   Thread.currentThread().getId();
	   System.out.println(dfm.format(date)+" "+Thread.currentThread().getId()+":"+log);
  }
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
    extends StdCallLibrary
  {
    public static final JPYZM INSTANCE = (JPYZM)Native.loadLibrary(EloanCodeUtils.DLLPATH, JPYZM.class);
    
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
  
  private static byte[] getContent(CloseableHttpClient httpclient) throws IOException {
    HttpGet httpget = new HttpGet("http://www.eloancn.com/randCode/randCode.jsp?te=" + new Date().getTime());
    CloseableHttpResponse response = httpclient.execute(httpget);
    HttpEntity entity = response.getEntity();
    InputStream fi = entity.getContent();
    long length = entity.getContentLength();
    byte[] buffer = new byte[99999];
    int offset = 0;
    int numRead = 0;
    while ((offset < buffer.length) && ((numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0))
    {
      offset += numRead;
    }
    



    fi.close();
    return buffer;
  }
  
  public static String getCode(byte[] imgbs) throws IOException {
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
    return rtnCode;
  }
  
  public static String getCode(CloseableHttpClient httpclient) throws IOException {
    byte[] bs = getContent(httpclient);
    return getCode(bs);
  }
  
  public static void genTempCode(CloseableHttpClient httpclient) throws IOException {
    String code = getCode(httpclient);
    setCodeTemp(code);
  }
  
  public static void init() {}
}


package com.plugin;

import com.util.EloanCodeUtils;
import com.util.RSAUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class LoginAction
{
  private static final String LT_BEGIN = "<input type=\"hidden\" name=\"lt\" value=\"";
  private static final String LT_END = "\"";
  private static final String EXECUTION_BEGIN = "<input type=\"hidden\" name=\"execution\" value=\"";
  private static final String EXECUTION_END = "\"";
  
  private static String[] getLt(CloseableHttpClient httpclient)
  {
    try
    {
      HttpGet httpget = new HttpGet("https://passport.eloancn.com/login");
      CloseableHttpResponse response = httpclient.execute(httpget);
      HttpEntity entity = response.getEntity();
      
      if (entity != null) {
        String[] ss = new String[2];
        String html = EntityUtils.toString(entity);
        ss[0] = StringUtils.substringBetween(html, "<input type=\"hidden\" name=\"lt\" value=\"", "\"");
        ss[1] = StringUtils.substringBetween(html, "<input type=\"hidden\" name=\"execution\" value=\"", "\"");
        response.close();
        return ss;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  

  public static boolean login(CloseableHttpClient httpclient, String account, String password)
    throws Exception
  {
    String[] ss = getLt(httpclient);
    boolean msg = false;
    HttpPost httppost = new HttpPost("https://passport.eloancn.com/login?service=http%3A%2F%2Fwww.eloancn.com%2Fpage%2FuserMgr%2FmyHome.jsp%3Furl%3D%2527fresh%2FuserDefaultMessage.action%2527%26menuid%3D1");
    setHeader(httppost);
    
    List<NameValuePair> formparams = new ArrayList();
    formparams.add(new BasicNameValuePair("username", account));
    formparams.add(new BasicNameValuePair("tpassword", ""));
    
    password = RSAUtils.encryptPassword(password);
    formparams.add(new BasicNameValuePair("password", password));
    assert (ss != null);
    formparams.add(new BasicNameValuePair("lt", ss[0]));
    formparams.add(new BasicNameValuePair("execution", ss[1]));
    formparams.add(new BasicNameValuePair("_eventId", "submit"));
    try
    {
      UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
      httppost.setEntity(uefEntity);
      EloanCodeUtils.printlog("executing request " + httppost.getURI());
      CloseableHttpResponse response = httpclient.execute(httppost);
      
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        String re = EntityUtils.toString(entity, "UTF-8");
        if ((StringUtils.isNotBlank(re)) && (re.contains("services.js"))) {
          msg = true;
          
          HttpGet httpget = new HttpGet("http://www.eloancn.com/page/userMgr/myHome.jsp");
          httpclient.execute(httpget);
        }
        
      }
      response.close();
      




      return msg;
    }
    catch (IOException e)
    {
    	e.printStackTrace();return msg; } finally {} 
  }
  
  private static void setHeader(HttpRequestBase httpPost)
  {
    httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    httpPost.setHeader("Accept-Encoding", "gzip, deflate");
    httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6,en;q=0.4");
    httpPost.setHeader("Cache-Control", "max-age=0");
    httpPost.setHeader("Connection", "keep-alive");
    
    httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
    httpPost.setHeader("Host", "passport.eloancn.com");
    httpPost.setHeader("Origin", "https://passport.eloancn.com");
    httpPost.setHeader("Referer", "https://passport.eloancn.com/login");
    httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");
  }
}


/* Location:              D:\cb\eloan\eloan_plugin_10.12.jar!\com\plugin\LoginAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
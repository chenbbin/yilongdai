package com.plugin;

import java.io.IOException;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;





public class MyAccountAction
{
  public static JSONObject getAccount(CloseableHttpClient httpclient)
  {
    try
    {
      HttpGet httpget = new HttpGet("http://www.eloancn.com/findUserVerifyInfo.action");
      
      setHeader(httpget);
      
      CloseableHttpResponse response = httpclient.execute(httpget);
      
      HttpEntity entity = response.getEntity();
      
      if (entity != null)
      {
        String html = EntityUtils.toString(entity);
        JSONObject jsonObject = JSONObject.fromObject(html);
        JSONObject userinfo = jsonObject.getJSONObject("userVerifyInfo");
        return userinfo != null ? userinfo : null;
      }
      
      response.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  private static void setHeader(HttpRequestBase httpPost) { httpPost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
    httpPost.setHeader("Accept-Encoding", "gzip, deflate, sdch");
    httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6,en;q=0.4");
    httpPost.setHeader("Cache-Control", "max-age=0");
    httpPost.setHeader("Connection", "keep-alive");
    
    httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
    httpPost.setHeader("Host", "www.eloancn.com");
    httpPost.setHeader("Cookie", "333");
    httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.132 Safari/537.36");
  }
}


/* Location:              D:\cb\eloan\eloan_plugin_10.12.jar!\com\plugin\MyAccountAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
package com.plugin;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.util.EloanCodeUtils;
public class Test
{
  public static void main(String[] ss)
    throws IOException
  {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    for (;;) {
      conect(httpclient);
    }
  }
  
  private static void conect(CloseableHttpClient httpclient)
  {
    try {
      HttpGet httpget = new HttpGet("http://www.eloancn.com/new/loadAllTender.action");
      RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).setConnectionRequestTimeout(1000).build();
      httpget.setConfig(requestConfig);
      CloseableHttpResponse response = httpclient.execute(httpget);
      HttpEntity entity = response.getEntity();
      
      if (entity != null) {
        String html = EntityUtils.toString(entity);
        EloanCodeUtils.printlog("——————————————成功");
      }
    } catch (IOException e) {
      e.printStackTrace();
      EloanCodeUtils.printlog("+++++++++++++++++++++++++++++异常");
    }
  }
}


/* Location:              D:\cb\eloan\eloan_plugin_10.12.jar!\com\plugin\Test.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
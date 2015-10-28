package com.util;

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MacUtils
{
  public static String getLocalMac() throws SocketException, UnknownHostException
  {
    InetAddress ia = InetAddress.getLocalHost();
    

    byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
    
    StringBuilder sb = new StringBuilder("");
    for (int i = 0; i < mac.length; i++)
    {



      int temp = mac[i] & 0xFF;
      String str = Integer.toHexString(temp);
      
      if (str.length() == 1) {
        sb.append("0").append(str);
      } else {
        sb.append(str);
      }
    }
    EloanCodeUtils.printlog("本机MAC地址:" + sb.toString().toUpperCase());
    return sb.toString().toUpperCase();
  }
}

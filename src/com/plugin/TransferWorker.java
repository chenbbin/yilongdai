package com.plugin;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class TransferWorker extends SwingWorker
{
  private String account;
  private String password;
  private String payPassword;
  private int payAmout;
  private int refreshTime;
  private int minRate;
  private int refreshTimeOut;
  private int buyTimeOut;
  private int leftAmount;
  private boolean multiThread;
  private JTextArea logArea;
  
  public void setAccount(String account)
  {
    this.account = account;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public void setLogArea(JTextArea logArea) {
    this.logArea = logArea;
  }
  
  public String getPayPassword() {
    return this.payPassword;
  }
  
  public void setPayPassword(String payPassword) {
    this.payPassword = payPassword;
  }
  
  public int getPayAmout() {
    return this.payAmout;
  }
  
  public void setPayAmout(int payAmout) {
    this.payAmout = payAmout;
  }
  
  public int getRefreshTime() {
    return this.refreshTime;
  }
  
  public void setRefreshTime(int refreshTime) {
    this.refreshTime = refreshTime;
  }
  
  public int getMinRate() {
    return this.minRate;
  }
  
  public void setMinRate(int minRate) {
    this.minRate = minRate;
  }
  
  public int getBuyTimeOut() {
    return this.buyTimeOut;
  }
  
  public void setBuyTimeOut(int buyTimeOut) {
    this.buyTimeOut = buyTimeOut;
  }
  
  public int getRefreshTimeOut() {
    return this.refreshTimeOut;
  }
  
  public void setRefreshTimeOut(int refreshTimeOut) {
    this.refreshTimeOut = refreshTimeOut;
  }
  
  public int getLeftAmount() {
    return this.leftAmount;
  }
  
  public void setLeftAmount(int leftAmount) {
    this.leftAmount = leftAmount;
  }
  
  public boolean isMultiThread() {
    return this.multiThread;
  }
  
  public void setMultiThread(boolean multiThread) {
    this.multiThread = multiThread;
  }
  
  protected Object doInBackground() throws Exception
  {
    MainExecutor.execute(this.account, this.password, this.payPassword, this.payAmout, this.minRate, this.refreshTime, this.refreshTimeOut, this.buyTimeOut, this.leftAmount, this.logArea, this.multiThread);
    return null;
  }
}


/* Location:              D:\cb\eloan\eloan_plugin_10.12.jar!\com\plugin\TransferWorker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
package com.plugin;

import java.awt.Container;
import java.io.BufferedWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

public class GUIEloan extends javax.swing.JFrame
{
  private String verson = "v2.01";
  
  private JTextField username;
  
  private JPasswordField password;
  
  private JPasswordField payPassword;
  
  private JTextField payAmount;
  
  private JTextField refreshTime;
  
  private JTextField minRate;
  
  private JTextField refreshTimeOut;
  
  private JTextField buyTimeOut;
  
  private JCheckBox multiThread;
  
  private JLabel jl1;
  
  private JLabel jl2;
  private JLabel jl3;
  private JLabel jl4;
  private JLabel jl5;
  private JLabel jl6;
  private JLabel jl7;
  private JLabel jl8;
  private JLabel jl9;
  private JLabel jl91;
  private JButton bu1;
  private JLabel amountLabel;
  private JTextField amount;
  private JTextArea logArea;
  
  public GUIEloan()
  {
    setTitle("翼龙助手V2.01");
    
    init();
    setDefaultCloseOperation(3);
    
    setLayout(null);
    
    setBounds(0, 0, 700, 380);
    
    java.awt.Image image = new ImageIcon("e:/a.gif").getImage();
    setIconImage(image);
    

    setResizable(false);
    

    setLocationRelativeTo(null);
    

    setVisible(true);
  }
  



  public void init()
  {
    Container con = getContentPane();
    this.jl1 = new JLabel();
    
    java.awt.Image image1 = new ImageIcon("e:/background.jpg").getImage();
    this.jl1.setIcon(new ImageIcon(image1));
    this.jl1.setBounds(0, 0, 355, 380);
    
    this.jl2 = new JLabel("用户名");
    this.jl2.setBounds(40, 0, 100, 60);
    
    this.jl3 = new JLabel("密     码");
    this.jl3.setBounds(40, 30, 100, 60);
    
    this.jl4 = new JLabel("支付密码");
    this.jl4.setBounds(40, 60, 100, 60);
    
    this.jl5 = new JLabel("每标投入");
    this.jl5.setBounds(40, 90, 100, 60);
    
    this.jl6 = new JLabel("刷新间隔（毫秒）");
    this.jl6.setBounds(40, 120, 100, 60);
    
    this.jl7 = new JLabel("最小利率");
    this.jl7.setBounds(40, 150, 100, 60);
    
    this.jl8 = new JLabel("刷新超时（秒）");
    this.jl8.setBounds(40, 180, 100, 60);
    
    this.jl9 = new JLabel("购买超时（秒）");
    this.jl9.setBounds(40, 210, 100, 60);
    
    this.jl91 = new JLabel("多线程");
    this.jl91.setBounds(40, 240, 100, 60);
    


    this.username = new JTextField();
    this.username.setBounds(150, 20, 160, 20);
    

    this.password = new JPasswordField();
    this.password.setBounds(150, 50, 160, 20);
    
    this.payPassword = new JPasswordField();
    this.payPassword.setBounds(150, 80, 160, 20);
    
    this.payAmount = new JTextField();
    this.payAmount.setBounds(150, 110, 160, 20);
    
    this.refreshTime = new JTextField("500");
    this.refreshTime.setBounds(150, 140, 160, 20);
    
    this.minRate = new JTextField();
    this.minRate.setBounds(150, 170, 160, 20);
    
    this.refreshTimeOut = new JTextField("2");
    this.refreshTimeOut.setBounds(150, 200, 160, 20);
    
    this.buyTimeOut = new JTextField("1");
    this.buyTimeOut.setBounds(150, 230, 160, 20);
    
    this.multiThread = new JCheckBox();
    this.multiThread.setBounds(150, 260, 160, 20);
    

    this.bu1 = new JButton("登录");
    this.bu1.setBounds(150, 300, 65, 20);
    
    this.bu1.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent e)
      {
        String str = e.getActionCommand();
        if ("登录".equals(str)) {
          GUIEloan.this.logArea.append("------------开始登录----------------");
          GUIEloan.this.logArea.append("\n");
          String getName = GUIEloan.this.username.getText();
          Integer my_amount = 2000000;
          try
          {
/*            CloseableHttpClient httpclient = org.apache.http.impl.client.HttpClients.createDefault();
            String url = "http://182.92.5.206:8080/eloan/checkAccount?account=" + getName;
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            org.apache.http.HttpEntity entity = response.getEntity();
            if (entity != null) {
              String ss = org.apache.http.util.EntityUtils.toString(entity);
              if ((StringUtils.isNotBlank(ss)) && (!"null".equals(ss))) {
                my_amount = Integer.valueOf(Integer.parseInt(ss));
              }
            }*/
            if (my_amount != null) {
              String getPwd = new String(GUIEloan.this.password.getPassword());
              String getPayPassword = new String(GUIEloan.this.payPassword.getPassword());
              String getPayAmount = GUIEloan.this.payAmount.getText();
              String getRefreshTime = GUIEloan.this.refreshTime.getText();
              String getMinRate = GUIEloan.this.minRate.getText();
              String getRefreshTimeOut = GUIEloan.this.refreshTimeOut.getText();
              String getBuyTimeOut = GUIEloan.this.buyTimeOut.getText();
              boolean getMultiThread = GUIEloan.this.multiThread.isSelected();
              
              GUIEloan.this.amount.setText(String.valueOf(my_amount));
              GUIEloan.this.writeUserConfig(getName, getPwd, getPayPassword, getPayAmount, getMinRate, getRefreshTime, getRefreshTimeOut, getBuyTimeOut, getMultiThread);
              
              TransferWorker transferWorker = new TransferWorker();
              transferWorker.setAccount(getName);
              transferWorker.setPassword(getPwd);
              transferWorker.setPayPassword(getPayPassword);
              transferWorker.setPayAmout(StringUtils.isNotBlank(getPayAmount) ? Integer.parseInt(getPayAmount) : 0);
              transferWorker.setRefreshTime(StringUtils.isNotBlank(getRefreshTime) ? Integer.parseInt(getRefreshTime) : 0);
              transferWorker.setMinRate(StringUtils.isNotBlank(getMinRate) ? Integer.parseInt(getMinRate) : 0);
              transferWorker.setRefreshTimeOut(StringUtils.isNotBlank(getRefreshTimeOut) ? Integer.parseInt(getRefreshTimeOut) : 0);
              transferWorker.setBuyTimeOut(StringUtils.isNotBlank(getBuyTimeOut) ? Integer.parseInt(getBuyTimeOut) : 0);
              transferWorker.setLeftAmount(my_amount.intValue());
              transferWorker.setMultiThread(getMultiThread);
              transferWorker.setLogArea(GUIEloan.this.logArea);
              transferWorker.execute();
            } else {
              GUIEloan.this.logArea.append("------------账号使用权未开通-----------");
              GUIEloan.this.logArea.append("\n");
            }
          }
          catch (Exception e1) {
            GUIEloan.this.logArea.append("+++++++登录异常+++++++");
            GUIEloan.this.logArea.append("\n");
            e1.printStackTrace();
          }
        }
      }
    });
    this.amountLabel = new JLabel("剩余投资额");
    this.amount = new JTextField();
    this.amount.setEditable(false);
    this.amount.setBackground(java.awt.Color.white);
    this.amountLabel.setBounds(320, 20, 70, 20);
    this.amount.setBounds(400, 20, 270, 20);
    
    this.logArea = new JTextArea();
    this.logArea.setEditable(false);
    this.logArea.setBackground(java.awt.Color.white);
    this.logArea.setBounds(320, 20, 350, 280);
    

    this.jl1.add(this.jl2);
    this.jl1.add(this.jl3);
    this.jl1.add(this.jl4);
    this.jl1.add(this.jl5);
    this.jl1.add(this.jl6);
    this.jl1.add(this.jl7);
    this.jl1.add(this.jl8);
    this.jl1.add(this.jl9);
    this.jl1.add(this.jl91);
    this.jl1.add(this.bu1);
    con.add(this.jl1);
    con.add(this.username);
    con.add(this.password);
    con.add(this.payPassword);
    con.add(this.payAmount);
    con.add(this.refreshTime);
    con.add(this.minRate);
    con.add(this.refreshTimeOut);
    con.add(this.buyTimeOut);
    con.add(this.multiThread);
    JScrollPane jScrollPane = new JScrollPane(this.logArea);
    jScrollPane.setBounds(320, 50, 350, 280);
    con.add(this.amountLabel);
    con.add(this.amount);
    con.add(jScrollPane);
/*    try {
      CloseableHttpClient httpclient = org.apache.http.impl.client.HttpClients.createDefault();
      String url = "http://182.92.5.206:8080/eloan/verson";
      HttpGet httpget = new HttpGet(url);
      CloseableHttpResponse response = httpclient.execute(httpget);
      org.apache.http.HttpEntity entity = response.getEntity();
      if (entity != null) {
        String verson = org.apache.http.util.EntityUtils.toString(entity);
        if (!this.verson.equals(verson)) {
          javax.swing.JOptionPane.showMessageDialog(null, "软件版本号错误，请更新1", "提示信息", 0);
          quit();
        }
      } else {
        javax.swing.JOptionPane.showMessageDialog(null, "软件版本号错误，请更新2", "提示信息", 0);
        quit();
      }
    } catch (IOException e) {
      e.printStackTrace();
      javax.swing.JOptionPane.showMessageDialog(null, "连接异常，请检查网络", "提示信息", 0);
      quit();
    }*/
    
    getUserConfig();
  }
  
  public void quit()
  {
    System.exit(0);
  }
  
  private void writeUserConfig(String account, String password, String payPassword, String balance, String minRate, String refreshTime, String refreshTimeOut, String buyTimeOut, boolean multiThread)
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.element("account", account);
    jsonObject.element("password", password);
    jsonObject.element("payPassword", payPassword);
    jsonObject.element("balance", balance);
    jsonObject.element("minRate", minRate);
    jsonObject.element("refreshTime", refreshTime);
    jsonObject.element("refreshTimeOut", refreshTimeOut);
    jsonObject.element("buyTimeOut", buyTimeOut);
    jsonObject.element("multiThread", multiThread);
    
    String userconfig = jsonObject.toString();
    String filePath = System.getProperty("user.dir");
    filePath = filePath + java.io.File.separator + "user_config";
    try {
      BufferedWriter out = new BufferedWriter(new java.io.FileWriter(filePath, false));
      out.write(userconfig);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void getUserConfig()
  {
    try {
      String filePath = System.getProperty("user.dir");
      filePath = filePath + java.io.File.separator + "user_config";
      java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(filePath));
      String line = "";
      StringBuffer buffer = new StringBuffer();
      while ((line = br.readLine()) != null) {
        buffer.append(line);
      }
      String fileContent = buffer.toString();
      String config = fileContent.trim();
      JSONObject jsonObject = JSONObject.fromObject(config);
      String account = jsonObject.getString("account");
      String password = jsonObject.getString("password");
      String payPassword = jsonObject.getString("payPassword");
      String balance = jsonObject.getString("balance");
      String minRate = jsonObject.getString("minRate");
      String refreshTime = jsonObject.getString("refreshTime");
      String refreshTimeOut = jsonObject.getString("refreshTimeOut");
      String buyTimeOut = jsonObject.getString("buyTimeOut");
      boolean multiThread = jsonObject.containsKey("multiThread") ? jsonObject.getBoolean("multiThread") : false;
      this.username.setText(account);
      this.password.setText(password);
      this.payPassword.setText(payPassword);
      this.payAmount.setText(balance);
      this.minRate.setText(minRate);
      this.refreshTime.setText(StringUtils.isNotBlank(refreshTime) ? refreshTime : "1000");
      this.refreshTimeOut.setText(StringUtils.isNotBlank(refreshTimeOut) ? refreshTimeOut : "1");
      this.buyTimeOut.setText(StringUtils.isNotBlank(buyTimeOut) ? buyTimeOut : "1");
      this.multiThread.setSelected(multiThread);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args)
  {
    GUIEloan rr = new GUIEloan();
  }
}


/* Location:              D:\cb\eloan\eloan_plugin_10.12.jar!\com\plugin\GUIEloan.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
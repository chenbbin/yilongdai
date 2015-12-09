package com.plugin;

import com.util.EloanCodeUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import javax.swing.JTextArea;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TenderAction
{
	public static final String serverIp = "http://182.92.5.206:8080";
	private static final String tender_separator = "<dt class=\"user_img fl wd100\">";
	private static final String tender_begin = "<div class=\"lendtable\">";
	private static final String tender_end = "<div class=\"pagebott\">";
	private static final String buy_flag_begin = "lender.fastLend('";
	private static final String buy_flag_end = "'";
	private static final String rate_begin = "'>";
	private static final String rate_end = "%</span>";
	private static final String sum_begin = "<p class=\"colorCb mt10\">￥";
	private static final String sum_end = "<";
	private static final String progress_begin = "#FF6600;\">";
	private static final String progress_end = "%";
	private static final String cryid_end = "\"";
	private static final int minAmount = 10;
	private static String account;
	private static String payPassword = "";
	private static int payAmount = 0;
	private static int balance;
	private static int minRate = 0;
	private static int refreshTimeOut;
	private static int buyTimeOut;
	private static int leftAmount;
	private static JTextArea logArea;
	private static boolean multiThread;
	private static List<String> buyedList = new ArrayList();

	public static JTextArea getLogArea() {
		return logArea;
	}

	public static void setLogArea(JTextArea logArea1) {
		logArea = logArea1;
	}

	public static String getAccount() {
		return account;
	}

	public static void setAccount(String account1) {
		account = account1;
	}

	public static String getPayPassword() {
		return payPassword;
	}

	public static void setPayPassword(String payPassword1) {
		payPassword = payPassword1;
	}

	public static int getPayAmount() {
		return payAmount;
	}

	public static void setPayAmount(int payAmout1) {
		payAmount = payAmout1;
	}

	public static int getBalance() {
		return balance;
	}

	public static void setBalance(int balance1) {
		balance = balance1;
	}

	public static int getLeftAmount() {
		return leftAmount;
	}

	public static void setLeftAmount(int leftAmount1) {
		leftAmount = leftAmount1;
		System.out.println("leftAmount="+leftAmount);
	}

	public static int getMinRate() {
		return minRate;
	}

	public static void setMinRate(int minRate1) {
		minRate = minRate1;
	}

	public static int getRefreshTimeOut() {
		return refreshTimeOut;
	}

	public static void setRefreshTimeOut(int refreshTimeOut1) {
		refreshTimeOut = refreshTimeOut1;
	}

	public static int getBuyTimeOut() {
		return buyTimeOut;
	}

	public static void setBuyTimeOut(int buyTimeOut1) {
		buyTimeOut = buyTimeOut1;
	}

	public static boolean isMultiThread() {
		return multiThread;
	}

	public static void setMultiThread(boolean multiThread1) {
		multiThread = multiThread1;
	}

	protected static void renderLog(final String msg) {
		if (logArea != null) {
			javax.swing.SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
					TenderAction.logArea.append(msg);
					TenderAction.logArea.append("\n");
					TenderAction.logArea.setCaretPosition(TenderAction.logArea.getDocument().getLength());
				}
			});
		}
		EloanCodeUtils.printlog(msg);
	}

	private static String getCryidBegin(String id) {
		return "&cry=";
	}
	private static String getNeedAmountBegin(String tenderId)
	{
		return "id=\"needAmount_" + tenderId + "\" value=\"";
	}
	private static List<TenderRate> getTenderList(CloseableHttpClient httpclient) {
		List<TenderRate> tenderRateList = new ArrayList();
		try {
			HttpGet httpget = new HttpGet("http://www.eloancn.com/new/loadAllTender.action");
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(refreshTimeOut * 1000).setConnectTimeout(refreshTimeOut * 1000).setConnectionRequestTimeout(refreshTimeOut * 1000).build();
			httpget.setConfig(requestConfig);
			CloseableHttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String html = EntityUtils.toString(entity);
				html = StringUtils.substringBetween(html, "lendtable", "pagebott");
				EloanCodeUtils.printlog(html);
				String[] ss = html.split("user_img");
				EloanCodeUtils.printlog("ss.length="+ss.length);
				for (int i = 1; i < ss.length; i++) {
					String content = ss[i];
					if (!content.contains("cryid_")) {
						EloanCodeUtils.printlog("no cryid");
						break;
					}
					String tenderId = StringUtils.substringBetween(content, "cryid_", "\"").trim();
					EloanCodeUtils.printlog("first tenderId="+tenderId);/*
					tenderId = StringUtils.substringBetween(content, "tenderid=", "&cry=").trim();
					EloanCodeUtils.printlog("second tenderId="+tenderId);*/
					String rate = StringUtils.substringBetween(content, "'>", "%");
					double rated = Double.parseDouble(rate);
					String cryid = StringUtils.substringBetween(content, "&cry=", "\"").trim();

					String needAmount = StringUtils.substringBetween(content, "id=\"needAmount_" + tenderId + "\" value=\"", "\"");

					EloanCodeUtils.printlog("tenderId="+tenderId);
					EloanCodeUtils.printlog("rate="+rate);
					EloanCodeUtils.printlog("rated="+rated);
					EloanCodeUtils.printlog("cryid="+cryid);
					EloanCodeUtils.printlog("needAmount="+needAmount);
					if ((rated >= minRate) && (!buyedList.contains(tenderId)))
					{
						TenderRate tenderRate = new TenderRate(tenderId, rated, cryid, Double.parseDouble(needAmount));
						tenderRateList.add(tenderRate);
					}
				}
				Collections.sort(tenderRateList);
				response.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			renderLog("本次刷新异常：" + e.getMessage());
		}
		return tenderRateList;
	}

	private static JSONObject getTender(CloseableHttpClient httpclient, String tenderId, String cryid) {
		try {
			String url = "http://www.eloancn.com/fastCreadTenderLoadById.action?te=" + new Date().getTime() + "&id=" + tenderId + "&cryid=" + cryid;
			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String html = EntityUtils.toString(entity);
				//EloanCodeUtils.printlog("getTender="+html);
				JSONObject jsonObject = JSONObject.fromObject(html);
				return jsonObject.getJSONObject("tender");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	private static Map makeTenderParams(CloseableHttpClient httpclient, JSONObject jsonObject, int balance)
			throws IOException, InterruptedException
			{
		Map<String, Object> tender = new java.util.HashMap();
		tender.put("tenderid", jsonObject.get("cryid"));
		tender.put("mode", jsonObject.get("mode"));
		tender.put("borrowerid", jsonObject.get("ownerid"));
		tender.put("interestrate", jsonObject.getString("strRealinterestrate").replace("%", ""));
		tender.put("needAmount", jsonObject.get("needAmount"));
		tender.put("cry", jsonObject.get("cry"));
		tender.put("rendererreason", "");
		tender.put("tenderBanlance", "");
		tender.put("redbag", Integer.valueOf(0));
		//needAmount是这个标的额度，balance是可投余额，payAmount是我自己写的每标要投的额度，leftAmount是剩下的买的额度
		int needAmount = Integer.parseInt(jsonObject.getString("needAmount"));
		int amount = needAmount > balance ? balance : needAmount;


		if (payAmount != 0) {
			amount = amount > payAmount ? payAmount : amount;
		}
		amount = amount > leftAmount ? leftAmount : amount;
		renderLog("needAmount="+needAmount);
		renderLog("balance="+balance);
		renderLog("payAmount="+payAmount);
		renderLog("leftAmount="+leftAmount);
		renderLog("finally amount="+amount);
		tender.put("amount", Integer.valueOf(amount));
		tender.put("paypassowrd", payPassword);
		if (StringUtils.isNotBlank(EloanCodeUtils.getCodeTemp())) {
			tender.put("tenderRecordRandCode", EloanCodeUtils.getCodeTemp());
			EloanCodeUtils.setCodeTemp(null);
		} else {
			tender.put("tenderRecordRandCode", EloanCodeUtils.getCode(httpclient));
		}



		return tender;
			}

	static class BuyThread implements java.util.concurrent.Callable
	{
		private CloseableHttpClient httpclient;
		private TenderRate tenderRate;
		private int balance1;

		public BuyThread(CloseableHttpClient httpclient, TenderRate tenderRate, int balance)
		{
			this.httpclient = httpclient;
			this.tenderRate = tenderRate;
			this.balance1 = balance;
		}

		public Object call() throws Exception
		{
			return Boolean.valueOf(buyTender(this.httpclient, this.tenderRate));
		}


		private static boolean buyTender(CloseableHttpClient httpclient, TenderRate tenderRate)
		{
			boolean isRun = true;
			if (TenderAction.balance >= 10) {
				try
				{
					JSONObject tender = TenderAction.getTender(httpclient, tenderRate.getTenderId(), tenderRate.getCryid());

					String msg = "准备购买散标 " + tender.getString("brealname") + " " + tender.getDouble("realinterestrate") * 100.0D + "%";
					TenderAction.renderLog(msg);

					Map<String, Object> paramsMap = TenderAction.makeTenderParams(httpclient, tender, TenderAction.balance);
					if((Integer)(paramsMap.get("amount"))==0){
						renderLog("amount=0, 标已经结束");
						return isRun;
					}
					String url = "http://www.eloancn.com/fastCreateTenderRecord.action?te=" + new Date().getTime();
					for (Map.Entry<String, Object> stringObjectEntry : paramsMap.entrySet()) {
						url = url + "&" + (String)stringObjectEntry.getKey() + "=" + stringObjectEntry.getValue();
					}
					EloanCodeUtils.printlog("url="+url);
					HttpGet httpget = new HttpGet(url);
					RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TenderAction.buyTimeOut * 1000).setConnectTimeout(TenderAction.buyTimeOut * 1000).setConnectionRequestTimeout(TenderAction.buyTimeOut * 1000).build();
					httpget.setConfig(requestConfig);
					CloseableHttpResponse response = httpclient.execute(httpget);
					HttpEntity entity = response.getEntity();
					if (entity != null)
					{
						EloanCodeUtils.printlog("进来了");
						String html = EntityUtils.toString(entity);
						EloanCodeUtils.printlog(html);
						if ((StringUtils.isNotBlank(html)) && (html.trim().startsWith("{")))
						{
							JSONObject jsonObject = JSONObject.fromObject(html);
							if ("success".equals(jsonObject.getString("tip")))
							{
								String msg3 = "成功买入散标 " + tender.getString("brealname") + " " + tender.getDouble("realinterestrate") * 100.0D + "%";
								TenderAction.renderLog(msg3);
								TenderAction.buyedList.add(tenderRate.getTenderId());
								int amount = ((Integer)paramsMap.get("amount")).intValue();

								TenderAction.balance=TenderAction.balance - amount;
								TenderAction.leftAmount=TenderAction.leftAmount - amount;

								String moneyMsg = "账户余额:" + TenderAction.balance;
								TenderAction.renderLog(moneyMsg);
								if (TenderAction.balance < 10)
								{
									String msg2 = "+++++账户余额不足，停止监控++++";
									isRun = false;
									TenderAction.renderLog(msg2);
								}
								moneyMsg = "剩余额度:" + TenderAction.leftAmount;
								TenderAction.renderLog(moneyMsg);
								if (TenderAction.leftAmount < 10)
								{
									String msg2 = "+++++剩余额度不足，停止监控++++";
									isRun = false;
									TenderAction.renderLog(msg2);
								}
								/*	                TenderAction.ChargeThread chargeThread = new TenderAction.ChargeThread(amount, tender.getString("brealname"), Double.valueOf(tender.getDouble("realinterestrate")));
	                chargeThread.start();*/
							}
							else if ("list".equals(jsonObject.getString("tip")))
							{
								String msg3 = "标已结束! " + tender.getString("brealname") + " " + tender.getDouble("realinterestrate") * 100.0D + "%";
								TenderAction.renderLog(msg3);
							}
							else
							{
								String ss = jsonObject.getString("tip");
								String message = "++++购买失败1++++" + ss;
								TenderAction.renderLog(message);
							}
						}
						else
						{
							String message = "++++购买返回错误，有可能买到++++";
							TenderAction.renderLog(message);
						}
					}
					else
					{
						String message = "++++购买失败2++++";
						TenderAction.renderLog(message);
					}
				}
				catch (Exception e)
				{
					String message = "++++购买发生异常++++";
					TenderAction.renderLog(message);
					TenderAction.renderLog(e.getMessage());
					StackTraceElement[] stackTraceElements = e.getStackTrace();
					for (StackTraceElement stackTraceElement : stackTraceElements) {
						TenderAction.renderLog(stackTraceElement.toString());
					}
					e.printStackTrace();
				}
			} else {
				isRun = false;
			}
			return isRun;
		}

		/*		private static boolean buyTender(CloseableHttpClient httpclient, TenderRate tenderRate)
				throws InterruptedException, IOException
				{
			boolean isRun = true;
			if (TenderAction.balance >= 10) {
				JSONObject tender = TenderAction.getTender(httpclient, tenderRate.getTenderId(), tenderRate.getCryid());

				String msg = "准备购买散标 " + tender.getString("brealname") + " " + tender.getDouble("realinterestrate") * 100.0D + "%";
				TenderAction.renderLog(msg);

				Map<String, Object> paramsMap = TenderAction.makeTenderParams(httpclient, tender, TenderAction.balance);
				if((Integer)(paramsMap.get("amount"))==0){
					renderLog("amount=0, 标已经结束");
					return isRun;
				}
				String url = "http://www.eloancn.com/fastCreateTenderRecord.action?te=" + new Date().getTime();
				for (Map.Entry<String, Object> stringObjectEntry : paramsMap.entrySet()) {
					url = url + "&" + (String)stringObjectEntry.getKey() + "=" + stringObjectEntry.getValue();
				}
				try {
					HttpGet httpget = new HttpGet(url);
					EloanCodeUtils.printlog("url="+url);
					RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TenderAction.buyTimeOut * 1000).setConnectTimeout(TenderAction.buyTimeOut * 1000).setConnectionRequestTimeout(TenderAction.buyTimeOut * 1000).build();
					httpget.setConfig(requestConfig);
					CloseableHttpResponse response = httpclient.execute(httpget);
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						EloanCodeUtils.printlog("进来了");
						String html = EntityUtils.toString(entity);
						EloanCodeUtils.printlog("html="+html);
						JSONObject jsonObject = JSONObject.fromObject(html);
						if ("success".equals(jsonObject.getString("tip"))) {
							int amount = ((Integer)paramsMap.get("amount")).intValue();
							              CloseableHttpClient serverClient = org.apache.http.impl.client.HttpClients.createDefault();
              String serverUrl = "http://182.92.5.206:8080/eloan/mygod?account=" + TenderAction.account + "&amount=" + amount;
              HttpGet serverget = new HttpGet(serverUrl);
              CloseableHttpResponse serverresponse = serverClient.execute(serverget);
              HttpEntity serverentity = serverresponse.getEntity();
              if (serverentity != null) {
                String ss = EntityUtils.toString(serverentity);
                if ("success".equals(ss)) {
							String msg3 = "成功买入散标 " + tender.getString("brealname") + " " + tender.getDouble("realinterestrate") * 100.0D + "%";
							TenderAction.renderLog(msg3);
							TenderAction.buyedList.add(tenderRate.getTenderId());
							TenderAction.balance -= amount;

							String moneyMsg = "账户余额:" + TenderAction.balance;
							TenderAction.renderLog(moneyMsg);

							if (TenderAction.balance < 10) {
								String msg2 = "+++++账户余额不足，停止监控++++";
								isRun = false;
								TenderAction.renderLog(msg2);
							}
                } else {
                  isRun = false;
                }
              } else {
                isRun = false;
              }
						} else if ("list".equals(jsonObject.getString("tip"))) {
							String msg3 = "标已结束! " + tender.getString("brealname") + " " + tender.getDouble("realinterestrate") * 100.0D + "%";
							TenderAction.renderLog(msg3);
						} else {
							String ss = jsonObject.getString("tip");
							String message = "++++购买失败1++++" + ss;
							TenderAction.renderLog(message);
						}
					} else {
						String message = "++++购买失败2++++";
						TenderAction.renderLog(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				isRun = false;
			}
			return isRun;
				}*/
	}

	public static boolean buyTenders(CloseableHttpClient httpclient) throws IOException, InterruptedException, ExecutionException
	{
		boolean isRun = true;
		List<TenderRate> tenderRateList = getTenderList(httpclient);
		if (tenderRateList.size() > 0) {
			Collections.sort(tenderRateList);
			String msg = "监测到  " + tenderRateList.size() + "  个散标";
			renderLog(msg);
			ExecutorService threadPool; 
			if (multiThread) {
				renderLog("已启用多线程模式！");
				threadPool = java.util.concurrent.Executors.newCachedThreadPool();
			} else {
				renderLog("已启用单线程模式！");
				threadPool = java.util.concurrent.Executors.newFixedThreadPool(1);
			}

			for (TenderRate tenderRate : tenderRateList)
			{
				BuyThread buyThread = new BuyThread(httpclient, tenderRate, balance);
				try
				{
					Future future = threadPool.submit(buyThread);
					boolean flag = ((Boolean)future.get(1000 * buyTimeOut, java.util.concurrent.TimeUnit.MILLISECONDS)).booleanValue();
					if (!flag) {
						threadPool.shutdown();
						isRun = false;
					}
				} catch (TimeoutException e) {
					renderLog("+++购买超时+++");
					e.printStackTrace();
				}
				catch (InterruptedException e)
				{
					renderLog("+++线程错误1+++");
					e.printStackTrace();
				}
				catch (ExecutionException e)
				{
					renderLog("+++线程错误2+++");
					e.printStackTrace();
				}
			}

			if (!threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		}else{
			String msg = "没有监测到散标";
			renderLog(msg);
		}


		return isRun;
	}

	public static int getBalanceInt(CloseableHttpClient httpclient)
	{
		JSONObject userinfo = MyAccountAction.getAccount(httpclient);
		String moneyS = userinfo.getString("balance");
		int money = Double.valueOf(moneyS).intValue();
		return money;
	}

/*	private static class ChargeThread
	extends Thread
	{
		private int amount;
		private String brealname;
		private Double realinterestrate;

		public ChargeThread(int amount, String brealname, Double realinterestrate)
		{
			this.amount = amount;
			this.brealname = brealname;
			this.realinterestrate = realinterestrate;
		}

		public void run()
		{
			try
			{
				CloseableHttpClient serverClient = HttpClients.createDefault();
				String serverUrl = "http://182.92.5.206:8080/eloan/mygod?account=" + TenderAction.account + "&amount=" + this.amount;
				HttpGet serverget = new HttpGet(serverUrl);
				CloseableHttpResponse serverresponse = serverClient.execute(serverget);
				HttpEntity serverentity = serverresponse.getEntity();
				if (serverentity != null)
				{
					String ss = EntityUtils.toString(serverentity);
					if ("success".equals(ss))
					{
						String msg4 = "记账成功 " + this.brealname + " " + this.realinterestrate.doubleValue() * 100.0D + "%";
						TenderAction.renderLog(msg4);
					}
					else
					{
						String msg4 = "记账失败1 " + this.brealname + " " + this.realinterestrate.doubleValue() * 100.0D + "%";
						TenderAction.renderLog(msg4);
					}
				}
				else
				{
					String msg4 = "记账失败2 " + this.brealname + " " + this.realinterestrate.doubleValue() * 100.0D + "%";
					TenderAction.renderLog(msg4);
				}
			}
			catch (Exception e)
			{
				TenderAction.renderLog("+记账发生异常+");
				TenderAction.renderLog(e.getMessage());
				e.printStackTrace();
			}
		}
	}*/
}

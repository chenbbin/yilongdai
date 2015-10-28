package com.plugin;

import com.util.EloanCodeUtils;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;


public class MainExecutor
{
	private static final String account = "";
	private static final String password = "";
	private static boolean isRun = true;
	private static final long syncWait = 500L;
	private static final long syncWaitLong = 20000L;
	private static final int[][][] startSaleTimes = { { { 10, 59, 0 }, { 11, 4, 0 } }, { { 15, 59, 0 }, { 16, 4, 0 } } };

	protected static void renderLog(final JTextArea logArea, final String msg) {
		if (logArea != null) {
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() {
					logArea.append(msg);
					logArea.append("\n");
					logArea.setCaretPosition(logArea.getDocument().getLength());
				}
			});
		}
	}

	public static void execute(String userName, String passWord, String payPassword, int payAmout, int minRate, int refreshTime, int refreshTimeOut, int buyTimeOut, int leftAmount, JTextArea logArea, boolean multiThread) throws Exception {
		CloseableHttpClient httpclient = SSLClientDefault.createSSLClientDefault();
		boolean login = false;
		try {
			login = LoginAction.login(httpclient, userName, passWord);
		} catch (Exception e) {
			String msg = "登录报错";
			EloanCodeUtils.printlog(msg);
			renderLog(logArea, msg);
			e.printStackTrace();
		}
		if (login) {
			String msg = "------------登录成功------------";
			EloanCodeUtils.printlog(msg);
			renderLog(logArea, msg);
			//这里会连接翼龙贷网站得到可投余额，为了速度，可以不要，但是余额太少需要在页面上的每标金额写上，否则会报notEnough错误
			int balance = TenderAction.getBalanceInt(httpclient);
			renderLog(logArea, "余额="+balance);
			TenderAction.setAccount(userName);
			TenderAction.setPayPassword(payPassword);
			TenderAction.setPayAmount(payAmout);
			TenderAction.setLogArea(logArea);
			TenderAction.setMinRate(minRate);
			TenderAction.setRefreshTimeOut(refreshTimeOut);
			TenderAction.setBuyTimeOut(buyTimeOut);
			TenderAction.setBalance(balance);
			TenderAction.setMultiThread(multiThread);
			while (isRun) {
				try {
					EloanCodeUtils.printlog("进入抢标模式");
					isRun = TenderAction.buyTenders(httpclient);
					if (isSaleDate()) {
						if (StringUtils.isBlank(EloanCodeUtils.getCodeTemp())) {
							EloanCodeUtils.genTempCode(httpclient);
						}
						Thread.sleep(refreshTime);
					} else {
						Thread.sleep(30000L);
					}
				} catch (IOException|InterruptedException e) {
					String msg1 = "买标报错";
					EloanCodeUtils.printlog(msg);
					renderLog(logArea, msg1);
					e.printStackTrace();
				}
			}
		}
		else
		{
			String msg = "------------登录失败------------";
			EloanCodeUtils.printlog(msg);
			renderLog(logArea, msg);
		}
	}

	public static void main(String[] strings)
	{
		CloseableHttpClient httpclient = SSLClientDefault.createSSLClientDefault();
		boolean login = false;
		try {
			login = LoginAction.login(httpclient, "", "");
		} catch (Exception e) {
			EloanCodeUtils.printlog("登录报错");
			e.printStackTrace();
		}
		if (login) {
			EloanCodeUtils.printlog("登录成功");
			while (isRun) {
				try {
					isRun = TenderAction.buyTenders(httpclient);
					if (isSaleDate()) {
						Thread.sleep(500L);
					} else {
						Thread.sleep(20000L);
					}
				}
				catch (IOException|InterruptedException e) {
					EloanCodeUtils.printlog("买标报错");
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private static boolean isSaleDate()
	{
		Date date = new Date();
		long dateLong = date.getTime();
		for (int[][] startSaleTime : startSaleTimes) {
			int[] begin = startSaleTime[0];
			int[] end = startSaleTime[1];
			long beginLong = getNewDateLong(begin[0], begin[1], begin[2]);
			long endLong = getNewDateLong(end[0], end[1], end[2]);
			if ((dateLong > beginLong) && (dateLong < endLong)) {
				return true;
			}
		}
		return false;
	}

	private static long getNewDateLong(int hour, int min, int sec) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.set(11, hour);
		calendar.set(12, min);
		calendar.set(13, sec);
		return calendar.getTimeInMillis();
	}
}


/* Location:              D:\cb\eloan\eloan_plugin_10.12.jar!\com\plugin\MainExecutor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */
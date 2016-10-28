package com;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class HttpClient {
	private static final int DEFAULT_TIMEOUT = 5000;

	public static String requestGet(String url) throws IOException{
		return requestGet(url, DEFAULT_TIMEOUT);
	}

	// timeout in milliseconds
	public static String requestGet(String url, int timeout) throws IOException {
		InputStream in = null;
		final int MAXRETRY = 3;
		for (int i = 0; i < MAXRETRY; ++i) {
			try {
				// 打开和URL之间的连接
				URLConnection connection = new URL(url).openConnection();
				// 设置通用的请求属性
				connection.setRequestProperty("accept", "*/*");
				connection.setRequestProperty("Accept-Charset", "utf-8");
				connection.setRequestProperty("connection", "Keep-Alive");
				connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
				connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
				if (timeout > 0) {
					connection.setConnectTimeout(timeout);
					connection.setReadTimeout(timeout);
				} else {
					connection.setConnectTimeout(DEFAULT_TIMEOUT);
					connection.setReadTimeout(DEFAULT_TIMEOUT);
				}
				// 建立实际的连接
				connection.connect();

				// 定义 BufferedReader输入流来读取URL的响应
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				in = connection.getInputStream();
				byte[] buf = new byte[1024];
				int nn = 0;
				while ((nn = in.read(buf)) > 0) {
					bos.write(buf, 0, nn);
				}
				return new String(bos.toByteArray());
			} catch (IOException e) {
				if (i != MAXRETRY - 1) {
					continue;
				}
				// 到达最大retry次数
				throw e;
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception e2) {
				}
			}
		}
		return "";
	}

}

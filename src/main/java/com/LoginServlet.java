package com;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * monitor调试登录验证
 * 
 * @date 2016年10月10日
 * @author dingyi
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 8251193706749635L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "private");
		response.setHeader("Expires", "0");
		response.setContentType("text/html;charset=UTF-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username != null && password != null) {
			HttpSession session = request.getSession();
			String result;
			try {
				result = HttpClient.requestGet("http://127.0.0.1/commonLogin.do?username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));
				if ("success".equals(result)) {
					session.setAttribute("isMonitorLogin", "yes");
					response.sendRedirect(request.getContextPath() + "/manage/monitor.jsp");
				} else {
					session.setAttribute("isMonitorLogin", "用户名或密码错误");
					response.sendRedirect(request.getContextPath() + "/manage/login.jsp");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			
		} else {
			System.out.println("没有传入账号和密码");
		}
	}
}

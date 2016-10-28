package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.mail.Store;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.constant.Constant;
import com.common.kafka.KafkaProducer;
import com.po.Student;
import com.redis.Redis;
import com.service.StudentService;
import com.util.IOUtil;
import com.util.LogUtil;

import redis.clients.jedis.ShardedJedis;

@Controller
public class ServletController {
	private Logger logger = LogUtil.getLog();

	@Resource
	private StudentService studentService;
	@Autowired
	private Redis redis;

	@RequestMapping("/queryOne")
	public String queryOne(HttpServletRequest request, Model model) {
		String message = "";
		KafkaProducer.produce(Constant.PART_0, "1");

		ShardedJedis jedis = redis.getRedis();
		try {
			String temp = jedis.get("servelt");
			if (temp != null) {
				int time = Integer.parseInt(jedis.ttl("servelt").toString());
				temp = String.valueOf(Integer.parseInt(temp) + 1);
				jedis.setex("servelt", time, temp);
				logger.info("redis统计下请求queryOne:" + temp + "次");
			} else {
				jedis.setex("servelt", 10, "1");
				logger.info("redis统计下请求queryOne:" + jedis.get("servelt") + "次");
			}
			redis.close(jedis);
		} catch (Exception e) {
			logger.info("redis超时");
		}
		if (null == request.getParameter("id") || "".equals(request.getParameter("id"))) {
			message = "未传入查询的ID号";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
		int id = Integer.valueOf(request.getParameter("id"));
		Student s = studentService.queryOne(id);
		if (s != null) {
			model.addAttribute("student", s);
			return "showStudentOne";
		} else {
			message = "数据库中没有编号为‘" + id + "'的同学的信息！";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
	}

	@RequestMapping("/queryList")
	public String queryList(HttpServletRequest request, Model model) {
		 String ip = request.getHeader("x-forwarded-for");
		 System.out.println("x-forwarded-for:"+ip);
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getHeader("Proxy-Client-IP");
		        System.out.println("Proxy-Client-IP:"+ip);
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getHeader("WL-Proxy-Client-IP");
		        System.out.println("WL-Proxy-Client-IP:"+ip);
		    }
		    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
		        ip = request.getRemoteAddr();
		        System.out.println("unknown:"+ip);
		    }
		System.out.println(ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip);
		KafkaProducer.produce(Constant.PART_1, "1");
		Date date = new Date();
		List<Student> s = studentService.queryList();
		try {
			String v= request.getParameter("value");
			System.out.println("原始的request内容:" + request.getInputStream()+";value="+request.getParameter("value"));
			System.out.println(request.getParameterNames());
			Map map=request.getParameterMap();
			Set keSet=map.entrySet();  
			
			System.out.println(map.values());
			
		    for(Iterator itr=keSet.iterator();itr.hasNext();){  
		        Map.Entry me=(Map.Entry)itr.next();  
		        Object ok=me.getKey();  
		        Object ov=map.get(ok); 
		        String[] value=new String[1];  
		        if(ov instanceof String[]){  
		            value=(String[])ov;  
		        }else{  
		            value[0]=ov.toString();  
		        }  
		  
		        for(int k=0;k<value.length;k++){  
		            System.out.println(ok+"="+value[k]);  
		        } 
			}
			String jsonStr = getRequestJson(request);
			System.out.println("转换后的request内容:" + jsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (s.size() > 0) {
			model.addAttribute("student", s);
			System.out.println("消耗时间2=" + ((new Date()).getTime() - date.getTime()));
			return "showStudentList";
		} else {
			String message = "数据库中没有同学的信息！";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
	}

	@RequestMapping("/insetInfo")
	public String insetInfo(HttpServletRequest request, Model model) {
		Student s = new Student();
		String message;
		String name = request.getParameter("name");
		if (null == name || "".equals(name)) {
			message = "未传入姓名";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
		s.setName(name);
		String temp = "^\\d+";
		String age = request.getParameter("age");
		if (null == age || "".equals(age)) {
			message = "未传入年龄";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
		if (age.matches(temp)) {
			s.setAge(Integer.valueOf(request.getParameter("age")));
		} else {
			message = "传入的年龄不是纯数字！传入的值=" + request.getParameter("age");
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
		try {
			studentService.insertInfo(s);
			Student student = studentService.queryOne(studentService.queryId());
			model.addAttribute("student", student);
			return "showStudentOne";
		} catch (Exception e) {
			message = "插入数据出错！";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
	}

	@RequestMapping("/requestCookie")
	public String requestCookie(HttpServletRequest request, Model model, HttpServletResponse respones) throws UnsupportedEncodingException {
		Cookie c = new Cookie("dy", URLEncoder.encode("丁毅", "utf-8"));
		respones.addCookie(c);
		return "redirect:/responesCookie";
	}

	@RequestMapping("/responesCookie")
	public String responesCookie(HttpServletRequest request, Model model, HttpServletResponse respones) throws UnsupportedEncodingException {
		Cookie[] c = request.getCookies();
		for (Cookie cc : c) {
			if ("dy".equals(cc.getName())) {
				System.out.println("CookieDomain=" + cc.getDomain() + ",Cookie存放的数据是：(key=" + cc.getName() + ",value=" + URLDecoder.decode(cc.getValue(), "utf-8") + ")");
			} // end--if
		} // end--for
		return "error";
	}
	@RequestMapping("commonLogin.do")
	public void login(HttpServletRequest request, HttpServletResponse response) {
		logger.info("enter into center commonLogin.do!");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String host = "mail.youku.com";
		logger.info("username="+username+";password="+password);
		PrintWriter out =null;
		try {
			out=response.getWriter();
			if (username.indexOf("@") > 0) {
				username = username.substring(0, username.indexOf("@"));
			}
			Properties props = new Properties();// Get session
			Session session1 = Session.getDefaultInstance(props, null);
			Store store = session1.getStore("pop3");
			store.connect(host, username, password);
			store.close();
			out.print("success");
		} catch (Exception e) {
			out.print("fail");
		}
	}
	protected String getRequestJson(HttpServletRequest request) throws IOException {
		try {
			int length = request.getContentLength();
			String result = IOUtil.toString(request.getInputStream(), request.getCharacterEncoding(), length + 1);
			return result;
		} catch (IOException e) {
			throw e;
		}
	}

}

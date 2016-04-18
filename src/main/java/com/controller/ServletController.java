package com.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.po.Student;
import com.service.StudentService;

@Controller
public class ServletController {
	private Logger logger = Logger.getLogger(ServletController.class);
	@Resource
	private StudentService studentService;

	@RequestMapping("/queryOne")
	public String queryOne(HttpServletRequest request, Model model) {
		if (null == request.getParameter("id") || "".equals(request.getParameter("id"))) {
			String message = "未传入查询的ID号";
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
			String message = "数据库中没有编号为‘" + id + "'的同学的信息！";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
	}

	@RequestMapping("/queryList")
	public String queryList(HttpServletRequest request, Model model) {
		List<Student> s = studentService.queryList();
		if (s.size() > 0) {
			model.addAttribute("student", s);
			return "showStudentList";
		} else {
			String message = "数据库中没有同学的信息！";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
	}

	@SuppressWarnings("unused")
	@RequestMapping("/insetInfo")
	public String insetInfo(HttpServletRequest request, Model model) {
		Student s = new Student();
		String name = request.getParameter("name");
		if (null == name || "".equals(name)) {
			String message = "未传入姓名";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
		s.setName(name);
		String temp = "^\\d+";
		String age = request.getParameter("age");
		if (null == age || "".equals(age)) {
			String message = "未传入年龄";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
		if (age.matches(temp)) {
			s.setAge(Integer.valueOf(request.getParameter("age")));
		} else {
			String message = "传入的年龄不是纯数字！传入的值=" + request.getParameter("age");
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
		try {
			studentService.insertInfo(s);
		} catch (Exception e) {
			String message = "插入数据出错！";
			logger.info(message);
			model.addAttribute("message", message);
			return "error";
		}
		Student student = studentService.queryOne(studentService.queryId());
		if (s != null) {
			model.addAttribute("student", student);
			return "showStudentOne";
		} else {
			String message = "数据库中没有编号为‘" + studentService.queryId() + "'的同学的信息！";
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
				System.out.println("CookieDomain="+cc.getDomain() + ",Cookie存放的数据是：(key=" + cc.getName() + ",value=" +URLDecoder.decode(cc.getValue(),"utf-8") + ")");
			}//end--if 
		}//end--for
		return "error";
	}
}

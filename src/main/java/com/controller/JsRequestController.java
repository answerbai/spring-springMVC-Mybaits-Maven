package com.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.po.Message;
import com.po.Student;
import com.po.Teacher;
import com.service.StudentService;
import com.util.HttpServletHelper;
import com.util.LogUtil;

@Controller
public class JsRequestController {
	private Logger logger = LogUtil.getLog();

	@Resource
	private StudentService studentService;

	@RequestMapping("/commonRequest")
	public String queryOne(HttpServletRequest request, Model model) {
		String common = request.getParameter("common");
		String value = request.getParameter("value1");
		logger.info("common=" + common);
		logger.info("value=" + value);
		model.addAttribute("message", "普通js请求成功");
		return "sucess";
	}

	@RequestMapping("/ajaxRequest")
	public void queryList(HttpServletRequest request, HttpServletResponse response) {
		String common2 = request.getParameter("common2");
		String value = request.getParameter("value2");
		logger.info("common2=" + common2);
		logger.info("value=" + value);
		Student student=new Student();
        student.setId(74);
        student.setName("丁晨星宇");
		student.setAge(25);
		
		Message m=new Message();
		m.setPhone("123456789");
		m.setAddress("北京市海淀区中钢大厦");
		student.setMessage(m);
		
		String str = JSON.toJSONString(student);
		logger.info(str);
		
		try {
			HttpServletHelper.WriteJSON(response, str);
		} catch (Exception e) {
			logger.info("ajax请求失败");
			e.printStackTrace();
		}
	}
	@RequestMapping("/fastJson")
	public String testFastJson(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<Integer,Teacher> map=new HashMap<>();
		Map<Integer,Student> map2=new HashMap<>();
		
		Student student=new Student();
        student.setId(74);
        student.setName("学生1");
		student.setAge(25);	
		map2.put(student.getId(), student);
		Message m=new Message();
		m.setPhone("123456789");
		m.setAddress("北京市");
		student.setMessage(m);
		
		student=new Student();
        student.setId(70);
        student.setName("学生2");
		student.setAge(25);
		m=new Message();
		m.setPhone("2222222");
		m.setAddress("北京市海淀区");
		student.setMessage(m);
		map2.put(student.getId(), student);		
		Teacher t1=new Teacher(1,"老师1",map2);
		map.put(t1.getId(), t1);
		
		map2=new HashMap<>();
		student=new Student();
        student.setId(4);
        student.setName("学生4");
		student.setAge(20);	
		m=new Message();
		m.setPhone("0000000009");
		m.setAddress("北京市海淀区中钢");
		student.setMessage(m);
		map2.put(student.getId(), student);
		student=new Student();
        student.setId(2);
        student.setName("学生2");
		student.setAge(15);
		m=new Message();
		m.setPhone("7777777777777");
		m.setAddress("北京市海淀区中钢大厦");
		student.setMessage(m);
		map2.put(student.getId(), student);	
		student=new Student();
        student.setId(1);
        student.setName("学生1");
		student.setAge(10);
		m=new Message();
		m.setPhone("123456789");
		m.setAddress("北京市昌平区");
		student.setMessage(m);
		map2.put(student.getId(), student);
		t1=new Teacher(3,"老师3",map2);
		map.put(t1.getId(), t1);

		String str = JSON.toJSONString(map);
		model.addAttribute("str", str);
		return "fastJson";
	}
	
}

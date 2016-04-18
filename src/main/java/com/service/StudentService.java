package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dao.StudentDao;
import com.po.Student;

@Component("studentService")
public class StudentService {
	@Autowired
	private StudentDao studentDao;

	public Student queryOne(int id) {
		return studentDao.queryOneById(id);
	}

	public Student queryOne(String name) {
		return studentDao.queryOneByName(name);
	}

	public List<Student> queryList() {
		return studentDao.queryList();
	}

	public void insertInfo(Student s) {
		studentDao.insertInfo(s);
	}

	public int queryId() {
		return studentDao.queryId();
	}
}

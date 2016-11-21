package com.po;

import java.util.Map;

public class Teacher {
	private int id;
	private String name;
	private Map<Integer, Student> map;

	public Teacher(int id, String name, Map<Integer, Student> map) {
		super();
		this.id = id;
		this.name = name;
		this.map = map;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, Student> getMap() {
		return map;
	}

	public void setMap(Map<Integer, Student> map) {
		this.map = map;
	}

}

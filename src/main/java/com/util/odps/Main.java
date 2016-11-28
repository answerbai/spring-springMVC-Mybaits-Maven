package com.util.odps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Main {

	private static String driverName = "com.aliyun.odps.jdbc.OdpsDriver";

	public static void main(String[] args) throws SQLException {
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}

		Properties config = new Properties();
		config.put("access_id", "LTAIFrUDfktMojoH");
		config.put("access_key", "yhSRmoHWZRxbCxr1oQ17jVpJjoS5Ul");
		config.put("project_name", "ytad");
		config.put("charset", "utf-8");
		Connection conn = DriverManager.getConnection("jdbc:odps:https://service.odps.aliyun.com/api", config);
		ResultSet rs;

		// create a table
		Statement stmt = conn.createStatement();
		String tableName = "ods_atm_test";
		stmt.execute("drop table if exists " + tableName);
		stmt.execute("create table " + tableName + " (key int, value string)");

//		// get meta data
//		DatabaseMetaData metaData = conn.getMetaData();
//		System.out.println("product = " + metaData.getDatabaseProductName());
//		System.out.println("jdbc version = " + metaData.getDriverMajorVersion() + ", " + metaData.getDriverMinorVersion());
//		rs = metaData.getTables(null, null, tableName, null);
//		while (rs.next()) {
//			String name = rs.getString(3);
//			System.out.println("inspecting table: " + name);
//			ResultSet rs2 = metaData.getColumns(null, null, name, null);
//			while (rs2.next()) {
//				System.out.println(rs2.getString("COLUMN_NAME") + "\t" + rs2.getString("TYPE_NAME") + "(" + rs2.getInt("DATA_TYPE") + ")");
//			}
//		}
//
//		// run sql
//		String sql;
//
//		// insert a record
//		sql = String.format("insert into table %s select 24 key, 'hours' value from (select count(1) from %s) a", tableName, tableName);
//		System.out.println("Running: " + sql);
//		int count = stmt.executeUpdate(sql);
//		System.out.println("updated records: " + count);
//
//		// select * query
//		sql = "select * from " + tableName;
//		System.out.println("Running: " + sql);
//		rs = stmt.executeQuery(sql);
//		while (rs.next()) {
//			System.out.println(String.valueOf(rs.getInt(1)) + "\t" + rs.getString(2));
//		}

	}
}
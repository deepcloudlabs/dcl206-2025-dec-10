package com.example;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFacade {

	public static List<String> getTableNames(String jdbcUrl, String username, String password) throws SQLException {
		Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
		System.out.println(connection.getClass());
		DatabaseMetaData metadata = connection.getMetaData();
		System.out.println(metadata.getClass());
		ResultSet rs = metadata.getTables(null, null, null, null);
		System.out.println(rs.getClass());
		List<String> tableNames = new ArrayList<>();
		while (rs.next()) {
			tableNames.add(rs.getString("TABLE_NAME"));
		}
		return tableNames;
	}
}

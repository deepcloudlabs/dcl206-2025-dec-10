package com.example.se11;

public class Exercise02 {

	public static void main(String[] args) {
		var sql = "select count(*) " + 
	              "from countries " + 
				  "where continent=\"Asia\"";
		var SQL = """
				select count(*)
				from countries
				where continent = "Asia"
				""";
		var jack = """
				{
				"firstName"	: "jack",
				"lastName"	: "bauer",
				"identity"	: "11111111110",
				"salary": 120000
				}
				""";
	}

}

package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DataBase {

	@Autowired
    JdbcTemplate jdbcTemplate;
		
	public void createRecord(String body){
        jdbcTemplate.update("INSERT INTO messages(message_body) VALUES (?)",body);   
	}
	
	public void printRecords(){
		System.out.println(jdbcTemplate.queryForRowSet("SELECT * from customers"));
	}
}

package com.example.demo.dao;

import java.util.List;

import com.example.demo.entity.Todo;
import com.example.demo.form.TodoQuery;

public interface TodoDao {
	
	List<Todo> findByJPQL( TodoQuery todoQuery );
	
	List<Todo> findByCriteria( TodoQuery todoQuery );

}

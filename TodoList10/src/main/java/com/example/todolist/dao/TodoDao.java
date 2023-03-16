package com.example.todolist.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoQuery;

//データベースアクセス専用クラス
public interface TodoDao {
	
	//CriteriaAPIに対応
	Page<Todo> findByCriteria( TodoQuery todoQuery , Pageable pageable );
	
}

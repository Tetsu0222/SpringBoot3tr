package com.example.todolist.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo , Integer >{
	
	//CRUD系のメソッドを一通り継承している。
	
	//追加で検索処理に対応する抽象メソッドを定義
	List<Todo> findByTitleLike ( String title       );
	List<Todo> findByImportance( Integer importance );
	List<Todo> findByUrgency   ( Integer urgency    );
	List<Todo> findByDeadlineBetweenOrderByDeadlineAsc         ( Date from , Date to );
	List<Todo> findByDeadlineGreaterThanEqualOrderByDeadlineAsc( Date from  );
	List<Todo> findByDeadlineLessThanEqualOrderByDeadlineAsc   ( Date to    );
	List<Todo> findByDone( String done );
	List<Todo> findAllByOrderById();
}
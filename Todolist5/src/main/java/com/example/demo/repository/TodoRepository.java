package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Todo;

@Repository	
public interface TodoRepository extends JpaRepository<Todo , Integer>{
	
	//各項目の検索処理と対で対応する。
	//SQL文の定型群メソッド
	List<Todo> findByTitleLike( String title );
	List<Todo> findByImportance( Integer Importance );
	List<Todo> findByUrgency( Integer urgency );
	List<Todo> findByDeadlineBetweenOrderByDeadlineAsc( Date from , Date to);
	List<Todo> findByDeadlineGreaterThanEqualOrderByDeadlineAsc( Date from );
	List<Todo> findByDeadlineLessThanEqualOrderByDeadlineAsc( Date to );
	List<Todo> findByDone( String done );

}

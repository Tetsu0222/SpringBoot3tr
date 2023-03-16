package com.example.todolist.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.todolist.common.Utils;
import com.example.todolist.entity.Todo;
import com.example.todolist.entity.Todo_;
import com.example.todolist.form.TodoQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class TodoDaoImpl implements TodoDao {
	
	//フィールド
	private final EntityManager entityManager;
	
	//CriteriaAPIによる検索
	@Override
	public Page<Todo> findByCriteria( TodoQuery todoQuery , Pageable pageable ) {
		CriteriaBuilder     builder    = entityManager.getCriteriaBuilder();
		CriteriaQuery<Todo> query      = builder.createQuery( Todo.class );
		Root<Todo>          root       = query.from( Todo.class );
		List<Predicate>     predicates = new ArrayList<>();
		String title = "";
		if ( todoQuery.getTitle().length() > 0 ) {		//件名
			title = "%" + todoQuery.getTitle() + "%";
		}else{
			title = "%";
		}
			predicates.add( builder.like( root.get( Todo_.TITLE ), title ));
		if( todoQuery.getImportance() != -1 ) {		    //重要度
			predicates.add( builder.and( builder.equal( root.get(Todo_.IMPORTANCE ), 
														todoQuery.getImportance() )));
		}
		if( todoQuery.getUrgency() != -1 ) {			//緊急度
			predicates.add( builder.and(builder.equal( root.get( Todo_.URGENCY ),
													   todoQuery.getUrgency() )));
		}
		if( !todoQuery.getDeadlineFrom().equals("")) {	//期限開始～
			predicates.add( builder.and( builder.greaterThanOrEqualTo( root.get( Todo_.DEADLINE ),
							Utils.str2date( todoQuery.getDeadlineFrom() ))));
		}
		if( !todoQuery.getDeadlineTo().equals("") ) {	//期限終了
			 predicates.add( builder.and( builder.lessThanOrEqualTo( root.get(Todo_.DEADLINE),
					 		 Utils.str2date( todoQuery.getDeadlineTo() ))));
		}
		if( todoQuery.getDone() != null && 
				todoQuery.getDone().equals("Y") ) {		//完了
			predicates.add( builder.and( builder.equal( root.get( Todo_.DONE ),
							todoQuery.getDone())));
		}

	    Predicate[] predArray = new Predicate[ predicates.size() ];
	    predicates.toArray( predArray );
	    query = query.select(root).where(predArray).orderBy( builder.asc( root.get(Todo_.id) ));
	    TypedQuery<Todo> typedQuery = entityManager.createQuery( query );
	    int totalRows = typedQuery.getResultList().size();
	    typedQuery.setFirstResult( pageable.getPageNumber() * pageable.getPageSize() );
	    typedQuery.setMaxResults ( pageable.getPageSize() );
	    Page<Todo> page = new PageImpl<Todo>( typedQuery.getResultList(), pageable, totalRows );
	    return page;
	}
	

}

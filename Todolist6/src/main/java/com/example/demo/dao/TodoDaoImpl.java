package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.demo.common.Utils;
import com.example.demo.entity.Todo;
import com.example.demo.entity.Todo_;
import com.example.demo.form.TodoQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TodoDaoImpl implements TodoDao {
	
	private final EntityManager entityManager;
	
	
	@Override
	public List<Todo> findByJPQL( TodoQuery todoQuery ) {
		StringBuilder sb = new StringBuilder( "select t from Todo t where 1 = 1" );
		List<Object> params = new ArrayList<>();					//複数のデータ型のパラメータがあるためObject型で総称
		int pos = 0;
		
		// 実行する JPQL の組み立て
		// 各要素の有無によってif文でクエリ文を追加
		// 件名
		if ( todoQuery.getTitle().length() > 0 ) {
			sb.append( " and t.title like ?" + ( ++pos ) );			//インクリメントされて?1になるとプレースホルダとして機能
			params.add( "%" + todoQuery.getTitle() + "%" );			//%で'をエスケープ
		}
		// 重要度
		if ( todoQuery.getImportance() != -1 ) {
			sb.append( " and t.importance = ?" + (++pos) );
			params.add( todoQuery.getImportance()        );
		}
		// 緊急度
		if ( todoQuery.getUrgency() != -1 ) {
			sb.append( " and t.urgency = ?" + ( ++pos ) );
			params.add( todoQuery.getUrgency()          );
		}
		// 期限：開始～
		if ( !todoQuery.getDeadlineFrom().equals("") ){
			sb.append( " and t.deadline >= ?" + ( ++pos )            );
			params.add( Utils.str2date( todoQuery.getDeadlineFrom() ));
		}
		// ～期限：終了で検索
		if ( !todoQuery.getDeadlineTo().equals("") ) {
			sb.append( " and t.deadline <= ?" + ( ++pos )           );
			params.add( Utils.str2date( todoQuery.getDeadlineTo() ));
		}
		// 完了
		if ( todoQuery.getDone() != null && todoQuery.getDone().equals( "Y" ) ) {
			sb.append( " and t.done = ?" +  ( ++pos ));
			params.add( todoQuery.getDone() );
		}
		
		// order
		sb.append( " order by id" );
		Query query = entityManager.createQuery( sb.toString() );		//EntityManagerでクエリ文を生成(Queryオブジェクトへ格納)
		for ( int i = 0 ; i < params.size() ; ++i ) { 					//if文に合致した数だけINパラメータを実行
			query = query.setParameter( i + 1 , params.get( i ) );		//parmsはコレクション,コレクションのインデックスとINパラメータは理論的に合致する。
		}
		@SuppressWarnings( "unchecked" )
		List<Todo> list = query.getResultList();
		
		return list;
	}

	
	
	// Criteria API による検索
	@Override
	public Page<Todo> findByCriteria(TodoQuery todoQuery, Pageable pageable) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Todo> query = builder.createQuery(Todo.class);
		Root<Todo> root = query.from(Todo.class);
		List<Predicate> predicates = new ArrayList<>();
		// 件名
		String title = "";
		if (todoQuery.getTitle().length() > 0) {
			title = "%" + todoQuery.getTitle() + "%";
		} else {
			title = "%";
		}
		predicates.add(builder.like(root.get(Todo_.TITLE), title));
		// 重要度
		if (todoQuery.getImportance() != -1) {
			predicates.add(builder.and(builder.equal(root.get(Todo_.IMPORTANCE), todoQuery.getImportance())));
		}
		// 緊急度
		if (todoQuery.getUrgency() != -1) {
			predicates.add(builder.and(builder.equal(root.get(Todo_.URGENCY), todoQuery.getUrgency())));
		}
		// 期限：開始～
		if (!todoQuery.getDeadlineFrom().equals("")) {
			predicates.add(builder.and(builder.greaterThanOrEqualTo(root.get(Todo_.DEADLINE),
					Utils.str2date(todoQuery.getDeadlineFrom()))));
		}
		// ～期限：終了で検索
		if (!todoQuery.getDeadlineTo().equals("")) {
			predicates.add(builder.and(
					builder.lessThanOrEqualTo(root.get(Todo_.DEADLINE), Utils.str2date(todoQuery.getDeadlineTo()))));
		}
		// 完了
		if (todoQuery.getDone() != null && todoQuery.getDone().equals("Y")) {
			predicates.add(builder.and(builder.equal(root.get(Todo_.DONE), todoQuery.getDone())));
		}
		// SELECT 作成
		Predicate[] predArray = new Predicate[predicates.size()];
		predicates.toArray(predArray);
		query = query.select(root).where(predArray).orderBy(builder.asc(root.get(Todo_.id)));
		// クエリ生成
		TypedQuery<Todo> typedQuery = entityManager.createQuery(query); // ①
		// 該当レコード数取得
		int totalRows = typedQuery.getResultList().size(); // ②
		// 先頭レコードの位置設定
		typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); // ③
		// 1 ページ当たりの件数
		typedQuery.setMaxResults(pageable.getPageSize()); // ④
		
		Page<Todo> page = new PageImpl<Todo>(typedQuery.getResultList(), pageable, totalRows); //⑤
		return page; 
	}

}




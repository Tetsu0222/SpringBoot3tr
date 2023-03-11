package com.example.demo.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.common.Utils;
import com.example.demo.entity.Todo;
import com.example.demo.form.TodoData;
import com.example.demo.form.TodoQuery;
import com.example.demo.repository.TodoRepository;

import lombok.AllArgsConstructor;

@Service	//コンストラクタインジェクションでインスタンスを取得させる。
@AllArgsConstructor
public class TodoService {
	
	private final TodoRepository todoRepository;
	
	
	public boolean isValid( TodoData todoData , BindingResult result ) {	//入力値がバインドされたtodoDataとチェック結果を格納するresult
		
		boolean ans = true;		
		String title = todoData.getTitle();
		if( title != null && !title.equals("") ) {
			boolean isAllDoubleSpace = true;			
			for( int i = 0 ; i < title.length() ; i++ ) {
				if( title.charAt( i ) != ' ' ) {
					isAllDoubleSpace = false;
					break;
				}
			}
			if( isAllDoubleSpace ) {
				result.addError( new FieldError( result.getObjectName() ,"title" , "件名がスペースです" ) );	//BindingResultにエラー内容を追加
				ans = false;
			}
		}
		
		String deadline = todoData.getDeadline();
		if ( !deadline.equals("") ) {
			LocalDate tody = LocalDate.now();
			LocalDate deadlineDate = null;			
			try {
				deadlineDate = LocalDate.parse( deadline );
				if ( deadlineDate.isBefore( tody )) {		//期限よりも今が前ならエラーメッセージを表示させる。isBefore()メソッドとtodyで判定
					result.addError( new FieldError( result.getObjectName() , "deadline" , "期限を設定するときは今日以降にしてください" ) );	//BindingResultにエラー内容を追加
					ans = false;
				}				
			}catch( DateTimeException e ) {
				result.addError( new FieldError( result.getObjectName() , "deadline" , "期限を設定するときは yyyy-mm-dd 形式で入力してください" ) );		//BindingResultにエラー内容を追加
				ans = false;
			}
		}		
		return ans;
		
	}
	
	
	public boolean isValid( TodoQuery todoQuery , BindingResult result ) {
		
		boolean ans = true;
		String date = todoQuery.getDeadlineFrom();
		if( !date.equals("") ) {
			try {
				LocalDate.parse( date );				
			}catch( DateTimeException e ){
				result.addError( new FieldError( result.getObjectName() , "deadlineFrom" , "期限：開始を入力する時はyyyy-mm--dd形式で入力してください" ) );
				ans = false;
			}
		}
		
		date = todoQuery.getDeadlineTo();
		if( !date.equals("") ) {
			try {
				LocalDate.parse( date );				
			}catch( DateTimeException e ){
				result.addError( new FieldError( result.getObjectName() , "deadlineTo" , "期限：終了を入力する時はyyyy-mm--dd形式で入力してください" ) );
				ans = false;
			}
		}
		return ans;
	}
	
	
	public List<Todo> doQuery(TodoQuery todoQuery) {
		List<Todo> todoList = null;
		if (todoQuery.getTitle().length() > 0) {
			// タイトルで検索
			todoList = todoRepository.findByTitleLike("%" + todoQuery.getTitle() + "%");
		} else if (todoQuery.getImportance() != null && todoQuery.getImportance() != -1) {
			// 重要度で検索
			todoList = todoRepository.findByImportance(todoQuery.getImportance());
		} else if (todoQuery.getUrgency() != null && todoQuery.getUrgency() != -1) {
			// 緊急度で検索
			todoList = todoRepository.findByUrgency(todoQuery.getUrgency());
		} else if (!todoQuery.getDeadlineFrom().equals("") && todoQuery.getDeadlineTo().equals("")) {
			// 期限 開始～
			todoList = todoRepository
					.findByDeadlineGreaterThanEqualOrderByDeadlineAsc(Utils.str2date(todoQuery.getDeadlineFrom()));
		} else if (todoQuery.getDeadlineFrom().equals("") && !todoQuery.getDeadlineTo().equals("")) {
			// 期限 ～終了
			todoList = todoRepository
					.findByDeadlineLessThanEqualOrderByDeadlineAsc(Utils.str2date(todoQuery.getDeadlineTo()));
		} else if (!todoQuery.getDeadlineFrom().equals("") && !todoQuery.getDeadlineTo().equals("")) {
			// 期限 開始～終了
			todoList = todoRepository.findByDeadlineBetweenOrderByDeadlineAsc(
					Utils.str2date(todoQuery.getDeadlineFrom()), Utils.str2date(todoQuery.getDeadlineTo()));
		} else if (todoQuery.getDone() != null && todoQuery.getDone().equals("Y")) {
			// 完了で検索
			todoList = todoRepository.findByDone("Y");
		} else {
			// 入力条件が無ければ全件検索
			todoList = todoRepository.findAll();
		}
		return todoList;
	}
	
	

}

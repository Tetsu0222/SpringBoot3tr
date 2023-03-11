package com.example.demo.service;

import java.time.DateTimeException;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.form.TodoData;

@Service	//コンストラクタインジェクションでインスタンスを取得させる。
public class TodoService {
	
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
				FieldError fieldError = new FieldError( result.getObjectName() ,	//FieldErrorを生成,コンストラクタへFormクラス名,クラス名はBindingResultのgetObjectName()で取り出せる。
														"title" ,					//エラーとするフィールド名
														"件名がスペースです" );		//エラーメッセージ
				result.addError( fieldError );	//BindingResultにエラー内容を追加
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
					FieldError fieldError = new FieldError( result.getObjectName() , "deadline" , "期限を設定するときは今日以降にしてください" );
					result.addError( fieldError );	//BindingResultにエラー内容を追加
					ans = false;
				}
				
			} catch  ( DateTimeException e ) {
				FieldError fieldError = new FieldError( result.getObjectName() , "deadline" , "期限を設定するときは yyyy-mm-dd 形式で入力してください" );
				result.addError( fieldError );		//BindingResultにエラー内容を追加
				ans = false;
			}
		}
		
		return ans;
		
	}

}

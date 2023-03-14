package com.example.todolist.service;

import java.time.DateTimeException;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.todolist.form.TodoData;


//デフォルトのバリデでは対応できない入力値チェックに対応するクラス
@Service
public class TodoService {
	
	public boolean isValid( TodoData todoData , BindingResult result ) {
		
		boolean ans = true;
		
		//件名がスペースだけかチェック
		String title = todoData.getTitle();
		if( title != null && !title.equals("") ) {				//無や空白はデフォルトのバリデでチェック済
			boolean isAllDoubleSpace = true;					//独自チェック開始
			for( int i = 0 ; i < title.length() ; i++ ) {		//スペースだけで埋められていないか確認
				if( title.charAt(i) != ' '){
					isAllDoubleSpace = false;					//スペース以外にもあればOK
					break;
				}
			}
			if( isAllDoubleSpace ) {							//スペースだけで埋められているとエラーを格納,バリデもエラーで返す。
				FieldError fieldError = new FieldError( result.getObjectName() , "title" , "件名が全角スペースです" );		//独自エラーを定義
				result.addError( fieldError );					//エラーをBindingResultへ格納
				ans = false;
			}
		}
		
		//期限が過去日かチェック
		String deadline = todoData.getDeadline();
		if( !deadline.equals("") ) {							//期限が空白かはデフォルトのバリデでチェック済 → 独自チェック開始
			LocalDate tody         = LocalDate.now();			//今日の日時を取得
			LocalDate deadlineDate = null;
			try {
				deadlineDate = LocalDate.parse( deadline );		//入力値がLocalDate型へ変換できるかチェック → 不可 → 例外スロー
				if( deadlineDate.isBefore( tody ) ) {			//今日以降の日付かチェック
					FieldError fieldError = new FieldError( result.getObjectName() , "deadline" , "期限を設定する時は、今日以降で設定してください" );
					result.addError( fieldError );
					ans = false;
				}
			}catch( DateTimeException e ) {
				FieldError fieldError = new FieldError( result.getObjectName() , "deadline" , "yyyy-mm-dd形式で入力してください" );
				result.addError( fieldError );
				ans = false;
			}	
		}
		return ans;
	}
	
}

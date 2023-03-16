package com.example.todolist.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;

import lombok.AllArgsConstructor;


//デフォルトのバリデでは対応できない入力値チェックに対応するクラス
@Service
@AllArgsConstructor
public class TodoService {
	
	private final MessageSource messageSource;
	
	//入力データに対応
	public boolean isValid( TodoData todoData 	 ,
							BindingResult result ,
							boolean isCreate 	 ,
							Locale locale ) {		
		boolean ans = true;
		//件名がスペースだけかチェック getMessage()でエラーメッセージを取得,DoubleSpace.todoData.titleはキーコード
		String title = todoData.getTitle();
		if( title != null && !title.equals("") ) {			//無や空白はデフォルトのバリデでチェック済
			boolean isAllDoubleSpace = true;				//独自チェック開始
			for( int i = 0 ; i < title.length() ; i++ ) {	//スペースだけで埋められていないか確認
				if( title.charAt(i) != ' '){
					isAllDoubleSpace = false;				//スペース以外にもあればOK
					break;
				}
			}
			if( isAllDoubleSpace ) {						//スペースだけで埋められているとエラーを格納,バリデもエラーで返す。
				FieldError fieldError = new FieldError( result.getObjectName() ,
														"title" ,
														messageSource.getMessage( "DoubleSpace.todoData.title" , null , locale ) );
				result.addError( fieldError );				//エラーをBindingResultへ格納
				ans = false;
			}
		}
		
		//期限が過去日かチェック
		String deadline = todoData.getDeadline();
		if( !deadline.equals("") ) {										//期限が空白かはデフォルトのバリデでチェック済 → 独自チェック開始
			LocalDate deadlineDate = null;
			try {
				deadlineDate = LocalDate.parse( deadline );					//入力値がLocalDate型へ変換できるかチェック → 不可 → 例外スロー
					if( isCreate ) {										//過去日チェックは更新時に行いわない。新規登録→false,更新→trueでメソッド呼び出し。
						if( deadlineDate.isBefore( LocalDate.now() ) ) {	//今日以降の日付かチェック
							result.addError( new FieldError( result.getObjectName() ,
															 "deadline" ,
															 messageSource.getMessage( "InvalidFormat.todoData.deadline" , null , locale ) ));
							ans = false;
						}
					}
			}catch( DateTimeException e ) {
				result.addError( new FieldError( result.getObjectName() ,
												 "deadline" ,
												 messageSource.getMessage( "Past.todoData.deadline" , null , locale ) ) );
				ans = false;
			}	
		}
		return ans;
	}
	
	//検索データに対応
	public boolean isValid( TodoQuery todoQuery , BindingResult result , Locale locale ) {
		
		boolean ans = true;
		
		//開始の入力値を検証
		if( !todoQuery.getDeadlineFrom().equals("") ) {
			try {
				LocalDate.parse( todoQuery.getDeadlineFrom() );	
			}catch( DateTimeException e ) {
				result.addError( new FieldError( result.getObjectName() ,
												 "deadlineFrom" ,
												 messageSource.getMessage( "InvalidFormat.todoQuery.deadlineFrom" , null , locale ) ) );
				ans = false;
			}	
		}
		
		//終了の入力値を検証
		if( !todoQuery.getDeadlineTo().equals("") ) {
			try {
				LocalDate.parse( todoQuery.getDeadlineTo() );	
			}catch( DateTimeException e ) {
				result.addError( new FieldError( result.getObjectName() ,
												 "deadlineTo" ,
												 messageSource.getMessage( "InvalidFormat.todoQuery.deadlineTo" , null , locale )  ));
				ans = false;
			}	
		}
		return ans;
	}
	
}

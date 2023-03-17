package com.example.demo.form;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.example.demo.entity.Todo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TodoData {
	
	//---フィールド---
	
	private Integer id;
	
	@NotBlank( message = "件名を入力してください")		//値が空かどうかバリデ
	private String title;
	
	@NotNull( message = "重要度を選択してください")		//値がnullかどうかバリデ
	private Integer importance;
	
	@Min( value = 0 , message = "重要度を選択してください")		//値の最小値を規定するバリデ
	private Integer urgency;
	
	private String deadline;
	private String done;
	
	
	//--- 入力データからEntityを生成して返す。
	
	public Todo toEntity() {
		
		Todo todo = new Todo();
		todo.setId        ( id         );
		todo.setTitle     ( title      );
		todo.setImportance( importance );
		todo.setUrgency   ( urgency    );
		todo.setDone      ( done       );
		
		SimpleDateFormat sdFormat = new SimpleDateFormat( "yyyy-MM-dd" );
		long ms;
		
		try {
			ms = sdFormat.parse( deadline ).getTime();
			todo.setDeadline( new Date( ms ) );
			
		}catch( ParseException e ){
			todo.setDeadline( null );
		}
		
		return todo;
	}
}

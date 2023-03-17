package com.example.todolist.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//メッセージテーブル操作用のクラス
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {
	
	private Integer id;
	
	@NotBlank
	private String message;
	
	private String time;

}

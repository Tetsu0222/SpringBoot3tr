package com.example.todolist.common;

import lombok.AllArgsConstructor;
import lombok.Data;

//メッセージの種類と内容を保持するクラス
@Data
@AllArgsConstructor
public class OpMsg {
	private String msgType;	//メッセージの種類
	private String msgText;	//メッセージの内容
}

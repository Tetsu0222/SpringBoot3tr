package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor		//Lombokのアノテーション 全フィールドへ値をセットするコンストラクタ
@Getter					//同上、フィールドに対するgetterメソッド
public class History {
	private int seq;
	private int yourAnswer;
	private String result;
}
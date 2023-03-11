package com.example.demo;

import lombok.Data;

@Data	//getter/setter,デフォルトコンストラクタ,toStringなども付与
public class RegistData {			//フォームクラスを定義
	private String name    ;
	private String password;
	private int    gender  ;
	private int    area    ;
	private int[]  interest;
	private String remarks ;

}
package com.example.todolist.common;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//ヘルパーメソッドを保持するクラス

public class Utils {

	//フィールド
	private static final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );

	//文字列をDate型へ変換
	public static Date str2date(String s) {
		long ms = 0;
		try {
			ms = sdf.parse( s ).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date( ms );
	}
}

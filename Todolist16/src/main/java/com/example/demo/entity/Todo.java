package com.example.demo.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity						//テーブルのレコードを示すクラスに付与
@Table( name = "todo" )		//エンティティに関連付けるテーブルを指定する。
@Data
public class Todo {
	
	@Id						//主キーであることを示す。
	@GeneratedValue( strategy = GenerationType.IDENTITY )		//主キーの自動採番を示す。SQLの種類に応じて指定する列挙型が変わるかも。
	@Column( name = "id" )		//対応する列を指定、プロパティ名と列名が同一なら省略OK、書いた方が無難？
	private Integer id;
	
	@Column( name = "title" )
	private String title;
	
	@Column( name = "importance" )
	private Integer importance;
	
	@Column( name = "urgency" )
	private Integer urgency;
	
	@Column( name = "deadline" )
	private Date deadline;
	
	@Column( name = "done" )
	private String done;

}

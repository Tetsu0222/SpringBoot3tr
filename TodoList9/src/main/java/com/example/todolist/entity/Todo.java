package com.example.todolist.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity						//Entityクラスであることを示す。SQL発行結果のデータを格納のためバリデしない。
@Table( name = "todo" )		//テーブルと関連付け
@Data						//lombokでgetter/setter機能付与
public class Todo {
	
	@Id						//主キーであることを示す。
	@GeneratedValue( strategy = GenerationType.IDENTITY )		
	@Column( name = "id" )
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

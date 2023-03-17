package com.example.todolist.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity								//Entityクラスであることを示す。SQL発行結果のデータを格納のためバリデしない。
@Table( name = "todo" )				//テーブルと関連付け
@Data								//lombokでgetter/setter機能付与
@ToString( exclude = "taskList" )	//Taskオブジェクトが自Todoオブジェクトを参照しているため、LombokからtaskListを除外
public class Todo {
	
	@Id														//主キーであることを示す。
	@GeneratedValue( strategy = GenerationType.IDENTITY )	//自動生成オプション
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
	
	@OneToMany( mappedBy = "todo" , cascade = CascadeType.ALL )		//1対nの1側 mappedByにはTaskクラス(n側)の対応する変数を指定 ALLですべての処理を対象化
	@OrderBy( "id asc" )
	private List<Task> taskList = new ArrayList<>();
	
	@OneToMany( mappedBy = "todo" , cascade = CascadeType.ALL )		//1対nの1側 mappedByにはmessageクラス(n側)の対応する変数を指定 ALLですべての処理を対象化
	@OrderBy( "id asc" )
	private List<Message> messageList = new ArrayList<>();
	
	
	public void addTask( Task task ) {
		task.setTodo( this );
		taskList.add( task );
	}
	
	
	public void addMessage( Message message ) {
		message.setTodo( this );
		messageList.add( message );
	}

	
}

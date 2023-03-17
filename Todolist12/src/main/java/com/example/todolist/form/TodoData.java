package com.example.todolist.form;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.todolist.common.Utils;
import com.example.todolist.entity.AttachedFile;
import com.example.todolist.entity.Task;
import com.example.todolist.entity.Todo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;


//フォーム入力されたデータを格納するクラス バリデを設ける。
@Data
@NoArgsConstructor
public class TodoData {
	
	private Integer id;
	
	@NotBlank
	private String title;
	
	@NotNull
	private Integer importance;
	
	@Min( value = 0 )
	private Integer urgency;
	
	private String deadline;
	private String done;
	
	@Valid
	private List<TaskData> taskList;
	
	private List<AttachedFileData> attachedFileList;	//添付ファイルに対応
	private TaskData 		   newTask;					//新規タスク入力に対応
	

	
	//入力されたデータからEntityクラスを生成
	public Todo toEntity() {
		Todo todo = new Todo();
		todo.setId	 ( id );
		todo.setTitle( title );
		todo.setImportance( importance );
		todo.setUrgency   ( urgency );
		todo.setDone      ( done );
		
		SimpleDateFormat sdFormat = new SimpleDateFormat( "yyyy-MM-dd" );
		long ms;
		
		try{
			ms = sdFormat.parse( deadline ).getTime();
			todo.setDeadline( new Date( ms ));
		} catch( ParseException e ) {
			todo.setDeadline( null );
		}
		
		Date date;
		Task task;
		
		if( taskList != null ) {
			for( TaskData taskData : taskList ) {
				date = Utils.str2dateOrNull( taskData.getDeadline() );
				task = new Task( taskData.getId() , null , taskData.getTitle() , date , taskData.getDone() );
				todo.addTask( task );
			}
		}
		
		return todo;
		
	}
	
	//TodoとAttachedFileから入力画面へデータを渡すオブジェクトを生成
    public TodoData( Todo todo , List<AttachedFile> attachedFiles ) {
    	
    	//taskの対応
        this.id         = todo.getId();
        this.title      = todo.getTitle();
        this.importance = todo.getImportance();
        this.urgency    = todo.getUrgency();
        this.deadline   = Utils.date2str( todo.getDeadline() );
        this.done       = todo.getDone();

        this.taskList = new ArrayList<>();
        String dt;
        for( Task task : todo.getTaskList() ) {
            dt = Utils.date2str( task.getDeadline() );
            this.taskList.add( new TaskData( task.getId() , task.getTitle() , dt , task.getDone() ));
        }
        newTask = new TaskData();
        
        
        //添付ファイルの対応
        attachedFileList = new ArrayList<>();
        String  fileName;
        String  fext;
        String  contentType;
        boolean isOpenNewWindow;
        for( AttachedFile af : attachedFiles ) {
            fileName = af.getFileName();									//ファイル名
            fext = fileName.substring( fileName.lastIndexOf( "." ) + 1 );	//拡張子
            contentType = Utils.ext2contentType( fext );					//Content-Type
            isOpenNewWindow = contentType.equals("") ? false : true;		//別のWindowで開く？
            attachedFileList.add( new AttachedFileData( af.getId() , fileName , af.getNote() , isOpenNewWindow ));
        }
    }
    
    public Task toTaskEntity() {
        Task task = new Task();
        task.setId   ( newTask.getId()   );
        task.setTitle( newTask.getTitle());
        task.setDone ( newTask.getDone() );
        task.setDeadline( Utils.str2date( newTask.getDeadline() ));
        return task;
    }
    
}
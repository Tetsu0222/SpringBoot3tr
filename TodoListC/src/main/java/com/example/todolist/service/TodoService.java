package com.example.todolist.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import com.example.todolist.common.Utils;
import com.example.todolist.entity.AttachedFile;
import com.example.todolist.form.MessageData;
import com.example.todolist.form.TaskData;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.AttachedFileRepository;

import lombok.RequiredArgsConstructor;

//デフォルトのバリデでは対応できない入力値チェックに対応するクラス
@Service
@RequiredArgsConstructor
public class TodoService {
	
	private final MessageSource          messageSource         ;	
	private final AttachedFileRepository attachedFileRepository;	//ファイル操作用のレポジトリ（独自定義インターフェース）
	
    @Value( "${attached.file.path}" )								//プロパティファイルの値を変数に代入
    private String ATTACHED_FILE_PATH;								//ファイルの保存先のパス
	
	//入力データに対応
	public boolean isValid( TodoData todoData 	 ,
							BindingResult result ,
							boolean isCreate 	 ,
							Locale locale ) {		
		boolean ans = true;
		//件名がスペースだけかチェック getMessage()でエラーメッセージを取得,DoubleSpace.todoData.titleはキーコード
		String title = todoData.getTitle();
		if( title != null && !title.equals("") ) {			//無や空白はデフォルトのバリデでチェック済
			boolean isAllDoubleSpace = true;				//独自チェック開始
			for( int i = 0 ; i < title.length() ; i++ ) {	//スペースだけで埋められていないか確認
				if( title.charAt(i) != ' '){
					isAllDoubleSpace = false;				//スペース以外にもあればOK
					break;
				}
			}
			if( isAllDoubleSpace ) {						//スペースだけで埋められているとエラーを格納,バリデもエラーで返す。
				FieldError fieldError = new FieldError( result.getObjectName() ,
														"title" ,
														messageSource.getMessage( "DoubleSpace.todoData.title" , null , locale ) );
				result.addError( fieldError );				//エラーをBindingResultへ格納
				ans = false;
			}
		}
		
		//期限が過去日かチェック
		String deadline = todoData.getDeadline();
		if( !deadline.equals("") ) {										//期限が空白かはデフォルトのバリデでチェック済 → 独自チェック開始
			LocalDate deadlineDate = null;
			try {
				deadlineDate = LocalDate.parse( deadline );					//入力値がLocalDate型へ変換できるかチェック → 不可 → 例外スロー
					if( isCreate ) {										//過去日チェックは更新時に行いわない。新規登録→false,更新→trueでメソッド呼び出し。
						if( deadlineDate.isBefore( LocalDate.now() ) ) {	//今日以降の日付かチェック
							result.addError( new FieldError( result.getObjectName() ,
															 "deadline" ,
															 messageSource.getMessage( "InvalidFormat.todoData.deadline" , null , locale ) ));
							ans = false;
						}
					}
			}catch( DateTimeException e ) {
				result.addError( new FieldError( result.getObjectName() ,
												 "deadline" ,
												 messageSource.getMessage( "Past.todoData.deadline" , null , locale ) ) );
				ans = false;
			}	
		}		
        List<TaskData> taskList = todoData.getTaskList();
        if( taskList != null ) {
            for( int n = 0; n < taskList.size(); n++ ){
                TaskData taskData = taskList.get( n );
                if( !Utils.isBlank( taskData.getTitle() )){
                    if( Utils.isAllDoubleSpace( taskData.getTitle() )) {
                        result.addError( new FieldError( result.getObjectName(),
														 "taskList[" + n + "].title",
														 messageSource.getMessage( "DoubleSpace.todoData.title" , null , locale ) ));
                        ans = false;
                    }
                }
                String taskDeadline = taskData.getDeadline();
                if ( !taskDeadline.equals("") && !Utils.isValidDateFormat( taskDeadline )) {
                    result.addError(new FieldError( result.getObjectName(),
                            						"taskList[" + n + "].deadline",
                            						messageSource.getMessage( "InvalidFormat.todoData.deadline" , null , locale ) ));
                    ans = false;
                }
            }
        }
		return ans;
	}
	
	//検索データに対応
	public boolean isValid( TodoQuery todoQuery , BindingResult result , Locale locale ) {
		
		boolean ans = true;
		
		//開始の入力値を検証
		if( !todoQuery.getDeadlineFrom().equals("") ) {
			try {
				LocalDate.parse( todoQuery.getDeadlineFrom() );	
			}catch( DateTimeException e ) {
				result.addError( new FieldError( result.getObjectName() ,
												 "deadlineFrom" ,
												 messageSource.getMessage( "InvalidFormat.todoQuery.deadlineFrom" , null , locale ) ) );
				ans = false;
			}	
		}
		
		//終了の入力値を検証
		if( !todoQuery.getDeadlineTo().equals("") ) {
			try {
				LocalDate.parse( todoQuery.getDeadlineTo() );	
			}catch( DateTimeException e ) {
				result.addError( new FieldError( result.getObjectName() ,
												 "deadlineTo" ,
												 messageSource.getMessage( "InvalidFormat.todoQuery.deadlineTo" , null , locale )  ));
				ans = false;
			}	
		}
		return ans;
	}
	
	//新規タスクの登録に対応
	public boolean isValid( TaskData taskData , BindingResult result , Locale locale ) {
		boolean ans = true;
        if( Utils.isBlank(taskData.getTitle() )) {
            FieldError fieldError = new FieldError( result.getObjectName() ,
            										"newTask.title",
            										messageSource.getMessage( "NotBlank.taskData.title" , null , locale ));
            result.addError( fieldError );
            ans = false;
        }else if( Utils.isAllDoubleSpace( taskData.getTitle() )) {
            FieldError fieldError = new FieldError( result.getObjectName(),
               										"newTask.title",
               										messageSource.getMessage( "DoubleSpace.todoData.title" , null , locale ));
            result.addError( fieldError );
            ans = false;
        }
        
        String deadline = taskData.getDeadline();
        
        if( deadline.equals("") ) {
        	return ans;
        }
        
        if( !Utils.isValidDateFormat( deadline ) ) {
        	FieldError fieldError = new FieldError( result.getObjectName(),
        											"newTask.deadline",
        											messageSource.getMessage( "InvalidFormat.todoData.deadline" , null , locale ));
        	result.addError( fieldError );
        	ans = false;
        	
        }else if( !Utils.isTodayOrFurtureDate( deadline )) {
        	FieldError fieldError = new FieldError( result.getObjectName(),
            		  								"newTask.deadline",
            		  								messageSource.getMessage( "Past.todoData.deadline" , null , locale ));
        	result.addError( fieldError );
        	ans = false;
        }
        
        return ans;
	}
	
	
	//新規メッセージの登録に対応
	public boolean isValid( MessageData messageData , BindingResult result , Locale locale ) {
		boolean ans = true;
        if( Utils.isBlank( messageData.getMessage() )) {
            FieldError fieldError = new FieldError( result.getObjectName() ,
            										"newMessage.message",
            										messageSource.getMessage( "NotBlank.messageData.title" , null , locale ));
            result.addError( fieldError );
            ans = false;
        }else if( Utils.isAllDoubleSpace( messageData.getMessage() )) {
            FieldError fieldError = new FieldError( result.getObjectName(),
               										"newMessage.message",
               										messageSource.getMessage( "DoubleSpace.todoData.title" , null , locale ));
            result.addError( fieldError );
            ans = false;
        }
        return ans;
	}
	
	
	
	//ファイルのアップロードに対応
    public void saveAttachedFile( int todoId , String note , 
    							  MultipartFile fileContents ) {
    	String fileName = fileContents.getOriginalFilename();		//アップロード元のファイル名を取得
        File uploadDir = new File( ATTACHED_FILE_PATH );			//Flieオブジェクトを生成(パスはプロパティファイルに記述)
        if( !uploadDir.exists() ) {									//格納先フォルダの確認
        	uploadDir.mkdirs();										//フォルダがなければ作成
        }

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmssSSS" );	//フォーマット作成→アップロード日時を取得
        String createTime = sdf.format( new Date() );						//現在日時を文字列にし、フォーマットにはめ込む。

        AttachedFile af = new AttachedFile();	//DBに格納するオブジェクトを生成(Entityクラス)
        af.setTodoId    ( todoId     );
        af.setFileName  ( fileName   );
        af.setCreateTime( createTime );
        af.setNote      ( note       );
        
        try( BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( Utils.makeAttahcedFilePath( ATTACHED_FILE_PATH , af )))) {
        	 byte[] contents = fileContents.getBytes();
        	 bos.write( contents );
        	 attachedFileRepository.saveAndFlush( af );
        }catch( IOException e ) {
            e.printStackTrace();
        }
    }
    
    
    //添付ファイルの削除
    public void deleteAttachedFile( int afId ) {
        AttachedFile af = attachedFileRepository.findById( afId ).get();
        File file       = new File( Utils.makeAttahcedFilePath( ATTACHED_FILE_PATH , af ));
        file.delete();
    }
    
    
    //DBからファイル情報を削除
    public void deleteAttachedFiles( Integer todoId ) {
        File file;
        List<AttachedFile> attachedFiles = attachedFileRepository.findByTodoIdOrderById( todoId );
        for( AttachedFile af : attachedFiles ) {
        	 file = new File( Utils.makeAttahcedFilePath( ATTACHED_FILE_PATH , af ));
             file.delete();
        }
    }
    

}
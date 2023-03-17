package com.example.todolist.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.todolist.common.OpMsg;
import com.example.todolist.dao.TodoDaoImpl;
import com.example.todolist.entity.AttachedFile;
import com.example.todolist.entity.Message;
import com.example.todolist.entity.Task;
import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.form.TodoQuery;
import com.example.todolist.repository.AttachedFileRepository;
import com.example.todolist.repository.MessageRepository;
import com.example.todolist.repository.TaskRepository;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor								//3段階でインスタンス取得(まずfinalのみ取得)
public class TodoListController {
	
	private final TodoRepository todoRepository;		//1段目のインスタンス取得
	private final TodoService    todoService   ;
	private final HttpSession    session       ;
	private final MessageSource  messageSource ;
	private final TaskRepository taskRepository;
	private final AttachedFileRepository attachedFileRepository;
	private final MessageRepository messageRepository;
	
	@PersistenceContext									//2段目のインスタンス取得
	private EntityManager entityManager;
	        TodoDaoImpl   todoDaoImpl  ;
	
	@PostConstruct										//3段目のインスタンス取得
	public void init() {								//初期化終了後に実行させるメソッド
		todoDaoImpl = new TodoDaoImpl( entityManager );	//EntityManagerでDao実装クラスをインスタンス化
	}
	
	
	@GetMapping( "/todo" )			//TOP画面に対応
	public ModelAndView showTodoList( ModelAndView mv , 
									  @PageableDefault( page = 0 , size = 5 , sort = "id" ) Pageable pageable ) {
		TodoQuery todoQuery = ( TodoQuery )session.getAttribute( "todoQuery" );
		if( todoQuery == null ) {
			todoQuery = new TodoQuery();
			session.setAttribute( "todoQuery" , todoQuery );
		}
		Pageable prevPageable = ( Pageable )session.getAttribute( "prevPageable" );
		if( prevPageable == null ) {
			prevPageable = pageable;
			session.setAttribute( "prevPageable" , prevPageable );
		}
		mv.setViewName( "todoList" );							//転送先のhtmlのファイル名（拡張子抜き)
		Page<Todo> todoPage = todoDaoImpl.findByCriteria( todoQuery , prevPageable );
		mv.addObject( "todoQuery" , todoQuery 		 );			//初期状態の検索状態で表示させる。
		mv.addObject( "todoPage"  , todoPage         );			//検索結果を内包したPageオブジェクトを画面に渡す。
		mv.addObject( "todoList"  , todoPage.getContent() );	//次に表示するページ単位のデータをgetContent()で取得して渡す。
		return mv;
	}
	
	
	@GetMapping( "/todo/{id}" )		//個別タスク画面へ遷移			
	public ModelAndView todoById( @PathVariable( name = "id" ) int id ,	//押下されたタスクのidを取得
								  ModelAndView mv ) {
			mv.setViewName( "todoForm" );
			Todo todo = todoRepository.findById( id ).orElseThrow( IllegalArgumentException::new );
			List<AttachedFile> attachedFiles = attachedFileRepository.findByTodoIdOrderById( id );	//添付ファイルの情報を取得
			mv.addObject( "todoData" , new TodoData( todo , attachedFiles ));
			session.setAttribute( "mode" , "update" );					//セッションへ入力画面のボタンを指定するデータを渡す。
			return mv;
	}
	
	
	@PostMapping( "/todo/query" )	//検索に対応
	public ModelAndView queryTodo( @ModelAttribute TodoQuery todoQuery ,
									BindingResult result ,
									@PageableDefault( page = 0 , size = 5 , sort = "id" ) Pageable pageable ,
									ModelAndView mv ,
									Locale locale ) {
		mv.setViewName( "todoList" );
		Page<Todo> todoPage = null;
		if( todoService.isValid( todoQuery , result , locale ) ) {
			todoPage = todoDaoImpl.findByCriteria( todoQuery , pageable );
			session.setAttribute( "todoQuery" , todoQuery );
			mv.addObject( "todoPage" , todoPage 			 );
			mv.addObject( "todoList" , todoPage.getContent() );
            if ( todoPage.getContent().size() == 0 ){
            	 mv.addObject( "msg" ,
            			 	   new OpMsg( "W" ,  messageSource.getMessage( "msg.w.todo_not_found" , null , locale ) ));	//Opmsgは@Dataクラス
            }
		}else{ 
            mv.addObject( "msg" , new OpMsg( "E" , messageSource.getMessage( "msg.e.input_something_wrong" , null , locale ) ));
			mv.addObject( "todoPage" , null );
			mv.addObject( "todoList" , null );
		}
		return mv;
	}
	
	
	@GetMapping( "/todo/query" )	//ページリンクに対応
	public ModelAndView queryTodo( @PageableDefault( page = 0 , size = 5 , sort="id" ) Pageable pageable , 
								   ModelAndView mv) {
		session.setAttribute( "prevPageable" , pageable );
		mv.setViewName( "todoList" );
		TodoQuery todoQuery = ( TodoQuery )session.getAttribute( "todoQuery" );
		Page<Todo> todoPage = todoDaoImpl.findByCriteria( todoQuery , pageable );
		mv.addObject( "todoQuery", todoQuery );
		mv.addObject( "todoPage" , todoPage  );
		mv.addObject( "todoList" , todoPage.getContent() );
		return mv;
	}
	
	
	@PostMapping( "/todo/create/form" )	//新規登録に対応,入力画面を表示
	public ModelAndView createTodo( ModelAndView mv ) {
		mv.setViewName( "todoForm" );
		mv.addObject  ( "todoData" , new TodoData() );
		session.setAttribute( "mode" , "create" );
		return mv;
	}
	
	
	@PostMapping( "/todo/create/do" )	//新規登録→登録ボタンに対応,データを格納処理
	public String createTodo( @ModelAttribute @Validated TodoData todoData ,		//@ModelAttributeで画面入力の値を取得,@Validatedでバリデ
									BindingResult result ,							//バリデ結果を格納（エラーメッセージを含めて）
									Model model 		 ,
									RedirectAttributes redirectAttributes,			//フラッシュスコープにメッセージを保存
									Locale locale ) {
		//エラーチェック(独自バリデ)
		boolean isValid = todoService.isValid( todoData , result , true , locale );	//サービスクラスのバリデ結果を確認
		if( !result.hasErrors() && isValid ) {										//エラーがなければ処理開始 hasErrors()はエラーがあるとtrueが返る。
            Todo todo = todoData.toEntity();
            todoRepository.saveAndFlush( todo );									//Todoオブジェクトからインサート文を生成して発行
            redirectAttributes.addFlashAttribute( "msg" , new OpMsg( "I" , messageSource.getMessage( "msg.i.todo_created" , null , locale ) ));
			return "redirect:/todo/" + todo.getId();
		}else{																		//プロパティファイルからエラーメッセージを取得
			model.addAttribute( "msg" , new OpMsg( "E" , messageSource.getMessage( "msg.e.input_something_wrong" , null , locale ) ));
			return "todoForm" ;
		}
	}
	
	
	@PostMapping( "/todo/update" )	//タスク詳細→更新に対応
	public String updateTodo( @ModelAttribute @Validated TodoData todoData ,
							  BindingResult result,
							  Model model,
							  RedirectAttributes redirectAttributes,
							  Locale locale ) {
		if( !result.hasErrors() && todoService.isValid( todoData , result , false , locale ) ) {
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush( todo );	
            redirectAttributes.addFlashAttribute( "msg" , new OpMsg( "I" , messageSource.getMessage( "msg.i.todo_updated" , null , locale )));
			return "redirect:/todo/" + todo.getId();
		}else{
            model.addAttribute( "msg" , new OpMsg( "E", messageSource.getMessage( "msg.e.input_something_wrong" , null , locale ) ));
			return "todoForm";
		}
	}	
	
	
	@GetMapping( "/task/delete")	//タスク→削除に対応
	public String deleteTask( @RequestParam( name = "task_id" ) int taskId ,
						  	  @RequestParam( name = "todo_id" ) int todoId ,
						  	  RedirectAttributes redirectAttributes,
						  	  Locale locale ) {
		taskRepository.deleteById( taskId );
        redirectAttributes.addFlashAttribute( "msg" , new OpMsg( "I" , messageSource.getMessage( "msg.i.task_deleted" , null , locale ) ));
        return "redirect:/todo/" + todoId;
	}
	
	
	@PostMapping( "/todo/delete" )
	@Transactional
    public String deleteTodo( @ModelAttribute TodoData todoData,
    						  RedirectAttributes redirectAttributes, 
    						  Locale locale) {
        Integer todoId = todoData.getId();
        todoService.deleteAttachedFiles( todoId );
        List<AttachedFile> attachedFiles = attachedFileRepository.findByTodoIdOrderById( todoId );
        attachedFileRepository.deleteAllInBatch( attachedFiles );
        todoRepository.deleteById( todoData.getId() );
        redirectAttributes.addFlashAttribute( "msg", new OpMsg( "I" , messageSource.getMessage( "msg.i.todo_deleted" , null , locale ) ));
        return "redirect:/todo";
    }
	
	
	@PostMapping( "/task/create" )
	public String createTask( @ModelAttribute TodoData todoData ,
							  BindingResult result,
							  Model model,
							  RedirectAttributes redirectAttributes,
							  Locale locale ) {
        boolean isValid = todoService.isValid( todoData.getNewTask() , result , locale );
        if( isValid ) {
            Todo todo = todoData.toEntity();
            Task task = todoData.toTaskEntity();
            task.setTodo( todo );
            taskRepository.saveAndFlush( task );
            redirectAttributes.addFlashAttribute( "msg" , new OpMsg( "I" , messageSource.getMessage( "msg.i.task_created" , null , locale ) ));
            return "redirect:/todo/" + todo.getId();
        }else{
            model.addAttribute( "msg", new OpMsg( "E", messageSource.getMessage( "msg.e.input_something_wrong" , null , locale ) ));
            return "todoForm";
        }
	}
							  

	@PostMapping( "/todo/cancel" )	//新規登録→戻るに対応,TOP画面へ遷移
	public String cancel() {
		return "redirect:/todo";	//Getリクエストが発生(リダイレクト)
	}
	
	
	@PostMapping( "/todo/af/upload" )	//ファイルのアップロードに対応
	public String uploadAttachedFile( @RequestParam( "todo_id" ) int todoId ,
									  @RequestParam( "note" ) String note ,
									  @RequestParam( "file_contents" ) MultipartFile fileContents ,
									  RedirectAttributes redirectAttributes,
									  Locale locale ) {
		if( fileContents.isEmpty() ) {
            redirectAttributes.addFlashAttribute( "msg" , new OpMsg( "W" , messageSource.getMessage( "msg.w.attachedfile_empty"    , null , locale )));
		}else{	//処理実行はこちら
			todoService.saveAttachedFile( todoId , note , fileContents ); 
			redirectAttributes.addFlashAttribute( "msg" , new OpMsg( "I" , messageSource.getMessage( "msg.i.attachedfile_uploaded" , null , locale )));
		}
		return "redirect:/todo/" + todoId;
	}
	
	
	@GetMapping( "/todo/af/delete" )
	public String deleteAttachedFile( @RequestParam( name = "af_id" ) int afId ,
									  @RequestParam( name = "todo_id") int todoId ,
									  RedirectAttributes redirectAttributes,
									  Locale locale ) {		
        todoService.deleteAttachedFile( afId );
        attachedFileRepository.deleteById( afId );
        redirectAttributes.addFlashAttribute( "msg" , new OpMsg( "I" , messageSource.getMessage( "msg.i.attachedfile_deleted" , null , locale) ));
        return "redirect:/todo/" + todoId;
	}
	
	
	@PostMapping( "/message/create" )
	public String createMessage( @ModelAttribute TodoData todoData ,
							  	 BindingResult result,
							  	 Model model,
							  	 RedirectAttributes redirectAttributes,
							  	 Locale locale ) {
        boolean isValid = todoService.isValid( todoData.getNewMessage() , result , locale );
        if( isValid ) {
            Todo todo       = todoData.toEntity();
            Message message = todoData.toMessageEntity();
            message.setTodo( todo );
            messageRepository.saveAndFlush( message );
            redirectAttributes.addFlashAttribute( "msg" , new OpMsg( "I" , messageSource.getMessage( "msg.i.message_created" , null , locale ) ));
            return "redirect:/todo/" + todo.getId();
        }else{
            model.addAttribute( "msg", new OpMsg( "E", messageSource.getMessage( "msg.e.input_something_wrong" , null , locale ) ));
            return "todoForm";
        }
	}
	
	
	@GetMapping( "/message/delete")	//タスク→削除に対応
	public String deleteMessage( @RequestParam( name = "message_id" ) int messageId ,
						  		 @RequestParam( name = "todo_id" ) int todoId ,
						  		 RedirectAttributes redirectAttributes,
						  		 Locale locale ) {
		messageRepository.deleteById( messageId );
        redirectAttributes.addFlashAttribute( "msg" , new OpMsg( "I" , messageSource.getMessage( "msg.i.message_deleted" , null , locale ) ));
        return "redirect:/todo/" + todoId;
	}
	
	
}

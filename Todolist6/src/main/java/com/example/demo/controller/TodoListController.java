package com.example.demo.controller;

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
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.TodoDaoImpl;
import com.example.demo.entity.Todo;
import com.example.demo.form.TodoData;
import com.example.demo.form.TodoQuery;
import com.example.demo.repository.TodoRepository;
import com.example.demo.service.TodoService;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor		//@RequiredArgsConstructorでfinalが付けられたフィールドだけをインジェクションの対象とする。
public class TodoListController {
	private final TodoRepository todoRepository;
	private final TodoService todoService;
	private final HttpSession session;
	
	
	@PersistenceContext
	private EntityManager entityManager;		//作成タイミングが違う？@PersistenceContextで修飾すればOK
	TodoDaoImpl todoDaoImpl;
	
	@PostConstruct								//コンストラクタの初期化後,EntityManagerをTodoDaoImpl渡すための修飾,このメソッド実行後に他のメソッドが呼び出される。
	public void init() {
		todoDaoImpl = new TodoDaoImpl( entityManager );
	}
	
	
	@GetMapping("/todo")
	public ModelAndView showTodoList( ModelAndView mv ,
			@PageableDefault( page = 0 , size = 5 , sort = "id" ) Pageable pageable ) {		//pageは表示するページ番号 sizeはページ当たりの件数 sortはORDER BY句と同等
		mv.setViewName( "todoList" );
		Page<Todo> todoPage = todoRepository.findAll( pageable );	//Pageableは@PageableDefaultでデフォルト値が規定されている。
		mv.addObject( "todoQuery" , new TodoQuery()       );
		mv.addObject( "todoPage"  , todoPage              ); 		//Page<Todo>オブジェクトを画面に渡す。画面側でページリンクを作成
		mv.addObject( "todoList"  , todoPage.getContent() ); 		//Pageオブジェクトはpage情報の他に、次に表示すべきコンテンツも保持,getContent()で取得
		session.setAttribute( "todoQuery", new TodoQuery()); 		//セッションへ検索条件を格納する。
		return mv;
	}
	
	
	@GetMapping( "/todo/create" )		//新規追加のリンクに対応
	public ModelAndView createTodo( ModelAndView mv ) {
		mv.setViewName( "todoForm" );						//呼び出された場合、入力画面へ遷移させる。
		mv.addObject( "todoData" , new TodoData() );		//TodoDataオブジェクトを生成して画面へ送る。
		session.setAttribute( "mode" , "create" );			//セッションのmode値をcreateへ変更
		return mv;
	}

	
	@PostMapping( "/todo/create" )		//入力画面の新規登録ボタンに対応,PostとGetでMappingを分けている。
	public String createTodo( @ModelAttribute @Validated TodoData todoData ,	//@ValidatedでtodoDataに格納されてバインドしている値をチェック
									BindingResult result ,							//上記の結果を格納するオブジェクト
									Model model ) {
		
		boolean isValid = todoService.isValid( todoData , result );		//アノテーションでチェックできないエラーをチェック
		
		if( !result.hasErrors() && isValid ) {			//hasErrors()でエラーの有無を確認、エラーがあるとtrueになるため逆転させている。			
														//両方でtrueなら処理
			Todo todo = todoData.toEntity();			//フォーム入力の値を持つTodoDataからtoEntity()でEntityオブジェクトを返す。 TodoData=Todo
			todoRepository.saveAndFlush( todo );		//saveAndFlush()は定型化されたSQL分を発行,Todoクラスの値でバインドされた値をSQLへINSERT
			
			return "redirect:/todo";					
			
		}else{
											
			return "todoForm";
			
		}
		
	}
	
	
	@PostMapping( "/todo/cancel" )
	public String cancel() {
		return "redirect:/todo";
	}
	
	
	@GetMapping( "/todo/{id}" )
	public ModelAndView todoById( @PathVariable( name = "id" ) int id ,		//URIテンプレートの変数idを@PathVariableで受け取る。
								  ModelAndView mv ) {						// ～/todo/id ←ここの数値を@PathVariableで受け取る。
		mv.setViewName( "todoForm" );
		Todo todo = todoRepository.findById( id ).get();					//@PathVariableで受け取った値で検索 findById()はSQL文の定型メソッド(SELECT * FROM ○○ WHERE id = 引数のid
		mv.addObject( "todoData" , todo );									//get()メソッドで取得したEntityオブジェクト(Todoクラス）をセット findById()の戻り値はOptional
		session.setAttribute( "mode" , "update" );							//表示するボタンの切り替え,sessionのmode値をupdateへ変更
		
		return mv;
	}
	
	
	@PostMapping( "/todo/update" )
	public String updateTodo( @ModelAttribute @Validated TodoData todoData ,
							  BindingResult result ,
							  Model model) {
		
		boolean isValid = todoService.isValid( todoData , result );
		
		if( !result.hasErrors() && isValid ) {
			Todo todo = todoData.toEntity();
			todoRepository.saveAndFlush( todo );
			return "redirect:/todo";
			
		}else {
			return "todoForm";
		}
	}
	 
	@PostMapping( "/todo/delete" )
	public String deleteTodo( @ModelAttribute @Validated TodoData todoData ) {
		todoRepository.deleteById( todoData.getId() );		//定型SQL文を発行,DELETE　引数内にWHERE句の参照値
		
		return "redirect:/todo";
	}
	
	@PostMapping("/todo/query")
	public ModelAndView queryTodo( @ModelAttribute TodoQuery todoQuery , BindingResult result ,
			@PageableDefault( page = 0 , size = 5 ) Pageable pageable , // ①
			ModelAndView mv) {
		mv.setViewName( "todoList" );
		Page<Todo> todoPage = null; // ②
		if (todoService.isValid( todoQuery, result )) {
			// エラーがなければ検索
			todoPage = todoDaoImpl.findByCriteria( todoQuery , pageable ); // ③
			// 入力された検索条件を session に保存
			session.setAttribute( "todoQuery" , todoQuery            ); // ④
			mv.addObject        ( "todoPage"  , todoPage             ); // ⑤
			mv.addObject        ( "todoList"  , todoPage.getContent()); // ⑥
		} else {
			// エラーがあった場合検索
			mv.addObject("todoPage", null); // ⑤’
			mv.addObject("todoList", null); // ⑥’
		}
		return mv;
	}
	
	@GetMapping("/todo/query")
	public ModelAndView queryTodo(@PageableDefault(page = 0, size = 5) Pageable pageable, ModelAndView mv) {
		mv.setViewName("todoList");
		// session に保存されている条件で検索
		TodoQuery todoQuery = (TodoQuery) session.getAttribute("todoQuery");
		Page<Todo> todoPage = todoDaoImpl.findByCriteria(todoQuery, pageable);
		mv.addObject("todoQuery", todoQuery); // 検索条件表示用
		mv.addObject("todoPage", todoPage); // page 情報
		mv.addObject("todoList", todoPage.getContent()); // 検索結果
		return mv;
	}
	
}
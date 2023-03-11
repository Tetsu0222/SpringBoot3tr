package com.example.demo.controller;

import java.util.List;

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
	
	
	@GetMapping( "/todo" )
	public ModelAndView showTodoList( ModelAndView mv ) {		
		mv.setViewName( "todoList" );
		List<Todo> todoList = todoRepository.findAll();
		mv.addObject( "todoList" , todoList );
		mv.addObject( "todoQuery" , new TodoQuery() );
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
	
	@PostMapping( "/todo/query" )
	public ModelAndView queryTodo( @ModelAttribute TodoQuery todoQuery ,
								   BindingResult result ,
								   ModelAndView mv) {
		
		mv.setViewName( "todoList" );
		List<Todo> todoList = null;
		if( todoService.isValid( todoQuery , result )) {
			
			todoList = todoDaoImpl.findByJPQL( todoQuery );			//JPQLによる検索
			//todoList = todoDaoImpl.findByCriteria( todoQuery );	//CriteriaAPIでの検索
		}
		
		mv.addObject( "todoList" , todoList );
		
		return mv;
	}			
	
}
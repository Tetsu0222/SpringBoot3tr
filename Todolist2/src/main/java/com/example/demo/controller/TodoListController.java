package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Todo;
import com.example.demo.form.TodoData;
import com.example.demo.repository.TodoRepository;
import com.example.demo.service.TodoService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
	private final TodoRepository todoRepository;
	private final TodoService todoService;
	
	
	@GetMapping("/todo")
	public ModelAndView showTodoList( ModelAndView mv ) {		
		mv.setViewName( "todoList" );
		List<Todo> todoList = todoRepository.findAll();
		mv.addObject( "todoList" , todoList );
		return mv;
	}
	
	
	@GetMapping( "/todo/create" )		//新規追加のリンクに対応
	public ModelAndView createTodo( ModelAndView mv ) {
		mv.setViewName( "todoForm" );	//呼び出された場合、入力画面へ遷移させる。
		mv.addObject( "todoData" , new TodoData() );		//TodoDataオブジェクトを生成して画面へ送る。
		return mv;
	}

	
	@PostMapping( "/todo/create" )		//入力画面の新規登録ボタンに対応,PostとGetでMappingを分けている。
	public ModelAndView createTodo( @ModelAttribute @Validated TodoData todoData ,	//@ValidatedでtodoDataに格納されてバインドしている値をチェック
									BindingResult result ,							//上記の結果を格納するオブジェクト
									ModelAndView mv ) {
		
		boolean isValid = todoService.isValid( todoData , result );		//アノテーションでチェックできないエラーをチェック
		
		if( !result.hasErrors() && isValid ) {			//hasErrors()でエラーの有無を確認、エラーがあるとtrueになるため逆転させている。			
														//両方でtrueなら処理
			Todo todo = todoData.toEntity();			//フォーム入力の値を持つTodoDataからtoEntity()でEntityオブジェクトを返す。 TodoData=Todo
			todoRepository.saveAndFlush(todo);			//saveAndFlush()は定型化されたSQL分を発行,Todoクラスの値でバインドされた値をSQLへINSERT
			
			return showTodoList( mv );					//同クラス内の上に記述されている独自定義メソッドを起動,findAllで内容を一覧表示
			
		}else{
			mv.setViewName( "todoForm" );				//バリデでfalseが発生すると入力画面へ遷移させる。
														//@ModelAttributeが付与されているオブジェクトはaddObject()しなくても遷移先で利用できる。
			return mv;
			
		}
		
	}
	
	
	@PostMapping( "/todo/cancel" )		//キャンセルボタンに対応
	public String cancel() {
		return "redirect:todo";			//todoへリダイレクトさせている。定型
	}

	
}

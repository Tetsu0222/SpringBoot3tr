package com.example.todolist.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todolist.entity.Todo;
import com.example.todolist.form.TodoData;
import com.example.todolist.repository.TodoRepository;
import com.example.todolist.service.TodoService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TodoListController {
	
	//フィールド--- @AllArgsConstructorでインスタンス化
	private final TodoRepository todoRepository;	
	private final TodoService todoService;

	//TOP画面に対応
	@GetMapping( "/todo" )
	public ModelAndView showTodoList( ModelAndView mv ) {
		mv.setViewName( "todoList" );					//転送先のhtmlのファイル名（拡張子抜き)
		List<Todo> todoList = todoRepository.findAll();	//JpaRepositoryに備わっているメソッド select*と同等
		mv.addObject( "todoList" , todoList );			//フロントにオブジェクトを転送
		return mv;
	}
	
	//新規登録に対応,入力画面を表示
	@GetMapping( "/todo/create" )
	public ModelAndView createTodo( ModelAndView mv ) {
		mv.setViewName( "todoForm" );
		mv.addObject( "todoData" , new TodoData() );	//
		return mv;
	}
	
	//新規登録→登録ボタンに対応,データを格納処理
	@PostMapping( "/todo/create" )
	public ModelAndView createTodo( @ModelAttribute @Validated TodoData todoData ,	//@ModelAttributeで画面入力の値を取得,@Validatedでバリデ
									BindingResult result ,							//バリデ結果を格納（エラーメッセージを含めて）
									ModelAndView mv ) {
		
		//エラーチェック(独自バリデ)
		boolean isValid = todoService.isValid( todoData , result );		//サービスクラスのバリデ結果を確認
		if( !result.hasErrors() && isValid ) {							//エラーがなければ処理開始 hasErrors()はエラーがあるとtrueが返る。
			Todo todo = todoData.toEntity();							//入力値からTodoオブジェクトを生成（独自定義メソッド）
			todoRepository.saveAndFlush( todo );						//Todoオブジェクトからインサート文を生成して発行
			return showTodoList( mv );									//同クラス内のメソッドを呼び出してTOP画面(レコード一覧出力画面）へ遷移
		}else{															//エラーがあればそのまま戻す。
			mv.setViewName( "todoForm" );								//入力されているデータとエラーメッセージを含めて登録画面へ遷移させる。
			return mv;													//入力値は@ModelAttributeで保持している。
		}
	}

	//新規登録→戻るボタンに対応,TOP画面へ遷移
	@PostMapping( "/todo/cancel" )
	public String cancel() {
		return "redirect:/todo";		//Getリクエストが発生(リダイレクト)
	}
	
}

package com.example.hello5;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller		//Thymeleafが処理したテンプレートを返す場合は@RestControllerではなくこちら。											
public class Hello5Controller {
	
	@GetMapping("/hello5")
	public ModelAndView sayHello( @RequestParam( "name" ) String name //ModelAndViewは次に表示する画面の名前（ビュー）と使用するデータ（モデル）を保持
			, ModelAndView mv ) {
		
		mv.setViewName( "hello" );			//次に表示する画面の名前（htmlファイルの拡張子なしの名前
		mv.addObject( "name" , name );		//次のビューが使用するデータを渡す。第1引数が変数式の名前に対応、第2引数が値(今回はレクエストパラメータから取得）
		return mv;							//上記のデータを保持したModelAndViewを返す。
		
	}
}
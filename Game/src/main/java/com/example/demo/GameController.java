package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class GameController {
	
	//フィールドインジェクション
	@Autowired				//このアノテーションを付与するとコントローラー起動時に、自動的にインスタンス化される。
	HttpSession session;	//他にも@SessionAttributeや@Scopeがある。
	
	@GetMapping("/")		//初期表示ともう1度最初からに対応
	public String index() { //ハンドラメソッド
		
		session.invalidate();	//セッション情報を消去
		
		//答えをランダムに生成してセッションへ格納
		int answer = new Random().nextInt( 100 ) + 1;
		//セッションへ生成された答を格納
		session.setAttribute( "answer" , answer );
		System.out.println( "answer=" + answer );
		
		return "game";
		
	}
	
	@PostMapping("/challenge")	//トライボタンを押下した時に対応
	public ModelAndView challenge( @RequestParam( "number" ) int number ,
								   ModelAndView mv ) {
	
		//セッションから答えを取得
		int answer = (Integer)session.getAttribute( "answer" );
		
		//ユーザーの回答履歴を取得
		@SuppressWarnings("unchecked")
		List<History> histories = ( List<History> )session.getAttribute( "histories" );
		
		if( histories == null ) {
			histories = new ArrayList<>();
			session.setAttribute( "histories" , histories );
		}
		
		//判定→回答履歴追加
		if( answer < number ) {
			histories.add( new History( histories.size() + 1 , number , "もっと小さいでしょう" ) );
		}else if( answer == number ) {
			histories.add( new History( histories.size() + 1 , number , "正解です!!!!" ) );
		}else {
			histories.add( new History( histories.size() + 1 , number , "もっと大きいです" ) );
		}
		
		mv.setViewName( "game" );
		mv.addObject( "histories" , histories );
		
		return mv;
	}
}
package com.example.janken;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SampleJankenController {
	
	List <String> list  = Arrays.asList( "グー" , "チョキ" , "パー" );
	List <String> list2 = Arrays.asList( "あいこ" , "あなたの負け" , "あなたの勝ち" );
	int win  ;
	int lose ;
	String battleresult ;
	
  
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("msg"  , "最初は･･･"     );
		mav.addObject("msg1" , "グー!!!"       );
		mav.addObject("msg2" , "じゃんけん･･･" );
		mav.addObject("msg3" , "ポン!!!!"      );
		
		return mav;		
	}
	

	@RequestMapping(value="/", method=RequestMethod.POST)
	public ModelAndView form( @RequestParam( value="radio1" , required = false ) String radio1 , ModelAndView mav ){
		
		int playerhandno = list.indexOf( radio1 );
		int cpuhandno    = new Random().nextInt( 3 );
		int judge        = ( playerhandno - cpuhandno + 3 ) % 3;
		
		String cpuhand = list.get( cpuhandno );
		String result  = list2.get( judge );
		
		
		String player = "";
		String cpu    = "コンピューターの手は：" + cpuhand;
		String res    = "結果は" + result;
		
		
		if( judge == 1 ) {
			lose++;
		}else if( judge == 2 ) {
			win++;
		}
		
		battleresult = win + "勝：" + lose + "敗";
		
	  
		try {
			player = "あなたが選んだ手は：" + radio1;
			
		}catch( NullPointerException e ){}
		
		mav.addObject( "msg" , battleresult );
		mav.addObject( "msg1" , player );
		mav.addObject( "msg2" , cpu    );
		mav.addObject( "msg3" , res    );
		mav.setViewName( "index" );
		
		return mav;
	}
	
}
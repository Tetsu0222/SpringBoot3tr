package com.example.sample1app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
	
	@GetMapping("/")
	public ModelAndView index(ModelAndView mav) {
	  mav.addObject("msg","名前を書いて下さい。");
	  mav.setViewName("index");
	  return mav;
	}

	@PostMapping("/")
	public ModelAndView form(@RequestParam("text1") String str, 
	    ModelAndView mav) {
	  mav.addObject("msg","こんにちは、" + str + "さん！");
	  mav.addObject("value",str);
	  mav.setViewName("index");
	  return mav;
	}
}
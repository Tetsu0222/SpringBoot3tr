package com.example.sample1app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
	  mav.setViewName("index");
	  mav.addObject("msg","フォームを送信下さい。");
	return mav;
	}

	@RequestMapping(value="/", method=RequestMethod.POST)
	public ModelAndView form(
	    @RequestParam(value="check1",required=false)boolean check1,
	    @RequestParam(value="radio1",required=false)String radio1,
	    @RequestParam(value="select1",required=false)String select1,
	    @RequestParam(value="select2",required=false)String[] select2,
	    ModelAndView mav) {

	  String res = "";
	  try {
	    res = "check:" + check1 +
	    " radio:" + radio1 +
	    " select:" + select1 + 
	    "\nselect2:";
	  } catch (NullPointerException e) {}
	  try {
	    res += select2[0];
	    for(int i = 1;i < select2.length;i++) {
	      res += ", " + select2[i];
	    }
	  } catch (NullPointerException e) {
	    res += "null";
	  }
	  mav.addObject("msg",res);
	  mav.setViewName("index");
	  return mav;
	}
}
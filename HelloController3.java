package com.example.sample1app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index(ModelAndView mav) {
	  mav.setViewName("index");
	  mav.addObject("msg","HelloController/indexのページです。");
	return mav;
	}

	@RequestMapping("/other")
	public String other() {
	  return "redirect:/";
	}

	@RequestMapping("/home")
	public String home() {
	  return "forward:/";
	}
}
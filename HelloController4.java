package com.example.sample1app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
  private boolean flag = false;
  
  @RequestMapping("/")
  public ModelAndView index(ModelAndView mav) {
    flag = !flag;
    mav.setViewName("index");
    mav.addObject("flag", flag);
    mav.addObject("msg", "サンプルのメッセージです。");
    return mav;
  }
}
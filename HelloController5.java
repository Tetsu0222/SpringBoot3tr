package com.example.sample1app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
  
  @RequestMapping("/")
  public ModelAndView index(ModelAndView mav) {
    mav.setViewName("index");
    mav.addObject("msg", "データを表示します。");
    String[] data = new String[] {"One","Two","Three"};
    mav.addObject("data", data);
    return mav;
  }
}
package com.example.samplesecurityapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SampleSecurityController {
  
  @RequestMapping("/")
  public ModelAndView index(ModelAndView mav) {
    mav.setViewName("index");
    mav.addObject("title", "Index page");
    mav.addObject("msg", "This is top page.");
    return mav;
  }

  @RequestMapping("/secret")
  public ModelAndView secret(ModelAndView mav, HttpServletRequest request) {
    String user = request.getRemoteUser();
    String msg = "This is secret page. [login by \"" + user + "\"]";
    mav.setViewName("Secret");
    mav.addObject("title", "Secret page");
    mav.addObject("msg", msg);
    return mav;
  }
}
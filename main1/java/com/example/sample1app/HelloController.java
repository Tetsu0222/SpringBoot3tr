package com.example.sample1app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.sample1app.repositories.PersonRepository;

@Controller
public class HelloController {
  
  @Autowired
  PersonRepository repository;

  @RequestMapping("/")
  public ModelAndView index(ModelAndView mav) {
    mav.setViewName("index");
    mav.addObject("title", "Hello page");
    mav.addObject("msg","this is JPA sample data.");
   Iterable<Person> list = repository.findAll();
    mav.addObject("data",list);
    return mav;
  }

}
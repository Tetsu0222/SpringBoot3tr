package com.example.sample1app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.sample1app.repositories.PersonRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Controller
public class HelloController {
  
  @Autowired
  PersonRepository repository;

  @RequestMapping("/")
  public ModelAndView index(
      @ModelAttribute("formModel") Person Person,
      ModelAndView mav) {
    mav.setViewName("index");
    mav.addObject("title", "Hello page");
    mav.addObject("msg","this is JPA sample data.");
    List<Person> list = repository.findAll();
    mav.addObject("data",list);
    return mav;
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  @Transactional
  public ModelAndView form(
      @ModelAttribute("formModel") Person Person, 
      ModelAndView mav) {
    repository.saveAndFlush(Person);
    return new ModelAndView("redirect:/");
  }
  
  @PostConstruct
  public void init(){
    // １つ目のダミーデータ作成
    Person p1 = new Person();
    p1.setName("taro");
    p1.setAge(39);
    p1.setMail("taro@yamada");
    repository.saveAndFlush(p1);
    // ２つ目のダミーデータ作成
    Person p2 = new Person();
    p2.setName("hanako");
    p2.setAge(28);
    p2.setMail("hanako@flower");
    repository.saveAndFlush(p2);
    // ３つ目のダミーデータ作成
    Person p3 = new Person();
    p3.setName("sachiko");
    p3.setAge(17);
    p3.setMail("sachico@happy");
    repository.saveAndFlush(p3);
  }
}
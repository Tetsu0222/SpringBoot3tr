package com.example.sample1app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SampleBootApp1Application {
	
	DataObject[] data = {
			new DataObject( "noname"  , "no email address" , 0  ),
			new DataObject( "taro"    , "taro@yamada"      , 39 ),
			new DataObject( "hanako"  , "hanako@flower"    , 28 ),
			new DataObject( "sachiko" , "sachiko@happy"    , 17 ),
			new DataObject( "jiro"    , "jiro@change"      , 6  )
	};

	public static void main( String[] args ) {
		SpringApplication.run( SampleBootApp1Application.class , args );
	}
	
	@RequestMapping("/{num}")
	public DataObject index( @PathVariable int num ) {
		int n = num < 0 ? 0 : num >= data.length ? 0 : num;
		return data[n];		
	}
}

class DataObject{
	private String name;
	private String mail;
	private int    age ;
	
	public DataObject( String name , String mail , int age ) {
		super();
		this.name = name;
		this.mail = mail;
		this.age  = age ;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}	
}
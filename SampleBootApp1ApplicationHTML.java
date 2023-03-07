package com.example.sample1app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SpringBootApplication
@RestController
public class SampleBootApp1Application {
	
	public static void main( String[] args ) {
		SpringApplication.run( SampleBootApp1Application.class , args );
	}
	
	@RequestMapping("/")
	  public String index(
		      HttpServletRequest request, 
		      HttpServletResponse response) {
		    response.setContentType(MediaType.TEXT_HTML_VALUE);
		    String content = """
		      <html>
		        <head>
		        <title>Sample App</title>
		        </head>
		        <body>
		        <h1>Sample App</h1>
		        <p>This is sample app page!</p>
		      </html>
		      """;
		    return content;
		  }
}
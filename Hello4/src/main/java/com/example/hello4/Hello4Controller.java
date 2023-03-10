package com.example.hello4;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController														//定型,付けとく。
public class Hello4Controller {

	@PostMapping("/hello4")											//ポストマッピング
	public String sayHello(@RequestParam("name") String name ) {	//@RequestParamはリクエストデータから値を受け取る。変数名と属性が一致しているので割愛OK
		return "Hello world!" + "こんにちは" + name + "さん";
	}
}

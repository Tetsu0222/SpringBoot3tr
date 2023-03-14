package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Todo;

@Repository	//このインターフェースがレポジトリであることを示す。エンティティクラスと対になる。
public interface TodoRepository extends JpaRepository<Todo , Integer >{			//継承元のジェネリックにはエンティティクラスと主キーのデータ型を指定
	//CRUD系のメソッドを一通り継承している。

}

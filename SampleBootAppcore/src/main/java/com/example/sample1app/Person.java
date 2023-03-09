package com.example.sample1app;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="people")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  private long id;
  
  @Column(length = 50, nullable = false)
  private String name;

  @Column(length = 200, nullable = true)
  private String mail;

  @Column(nullable = true)
  private Integer age;
  
  @Column(nullable = true)
  @Phone
  private String memo;
  
  @OneToMany(mappedBy="Person")
  @Column(nullable = true)
  private List<Message> messages;

  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
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

  public Integer getAge() {
    return age;
  }
  public void setAge(Integer age) {
    this.age = age;
  }

  public String getMemo() {
    return memo;
  }
  public void setMemo(String memo) {
    this.memo = memo;
  }
  
  public List<Message> getMessages() {
	    return messages;
  }

  public void setMessages(List<Message> messages) {
	    this.messages = messages;
  }
  
}
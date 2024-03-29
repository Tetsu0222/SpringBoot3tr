package com.example.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todolist.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

}

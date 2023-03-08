package com.example.sample1app;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class PersonDAOPersonImpl implements PersonDAO<Person> {
  private static final long serialVersionUID = 1L;
  
  @PersistenceContext
  private EntityManager entityManager;
  
  public PersonDAOPersonImpl(){
    super();
  }

  @Override
  public List<Person> getAll() {
    @SuppressWarnings("unchecked")
    List<Person> list = entityManager.createQuery("from Person").getResultList();
    entityManager.close();
    return list;
  }

}

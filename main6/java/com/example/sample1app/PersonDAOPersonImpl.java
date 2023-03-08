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
  
  @Override
  public Person findById(long id) {
    return (Person)entityManager.createQuery("from Person where id = " + id).getSingleResult();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Person> findByName(String name) {
    return (List<Person>)entityManager.createQuery("from Person where name = '" + name + "'").getResultList();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Person> find(String fstr){
    List<Person> list = null;
    list = entityManager
        .createNamedQuery("findWithName")
        .setParameter("fname", "%" + fstr + "%").getResultList();
    return list;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<Person> findByAge(int min, int max) {
    return (List<Person>)entityManager
      .createNamedQuery("findByAge")
      .setParameter("min", min)
      .setParameter("max", max)
      .getResultList();
  }
  
}
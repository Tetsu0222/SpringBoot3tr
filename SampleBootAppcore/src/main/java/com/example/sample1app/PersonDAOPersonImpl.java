package com.example.sample1app;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

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
    Query query = entityManager.createQuery("from Person");
    @SuppressWarnings("unchecked")
    List<Person> list = query.getResultList();
    entityManager.close();
    return list;
  }
  
  @Override
  public Person findById(long id) {
    return (Person)entityManager.createQuery("from Person where id = " 
      + id).getSingleResult();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Person> findByName(String name) {
    return (List<Person>)entityManager.createQuery("from Person where name = '" 
      + name + "'").getResultList();
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
  
  @Override
  public List<Person> getPage(int page, int limit) {
    int offset = page * limit; // 取り出す位置
    CriteriaBuilder builder = 
        entityManager.getCriteriaBuilder();
    CriteriaQuery<Person> query = 
        builder.createQuery(Person.class);
    Root<Person> root = 
        query.from(Person.class);
    query.select(root);
    return (List<Person>)entityManager
        .createQuery(query)
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultList();
  }
  
  @Override
  public List<Person> find(String fstr){
    List<Person> list = null;
    String qstr = "from Person where id = :fstr";
    Query query = entityManager.createQuery(qstr)
      .setParameter("fstr", Long.parseLong(fstr));
    list = query.getResultList();
    return list;
  }

}

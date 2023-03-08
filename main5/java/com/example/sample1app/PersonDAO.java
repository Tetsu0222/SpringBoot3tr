package com.example.sample1app;


import java.io.Serializable;
import java.util.List;

public interface PersonDAO <T> extends Serializable {

  public List<T> getAll();

}

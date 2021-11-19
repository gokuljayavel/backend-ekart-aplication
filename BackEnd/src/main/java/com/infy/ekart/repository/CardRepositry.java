package com.infy.ekart.repository;

import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.entity.Card;

public interface CardRepositry extends CrudRepository<Card, Integer>{}

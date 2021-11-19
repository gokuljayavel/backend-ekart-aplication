package com.infy.ekart.repository;

import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.entity.CustomerCart;

public interface CustomerCartRepository extends CrudRepository<CustomerCart, Integer> {

}

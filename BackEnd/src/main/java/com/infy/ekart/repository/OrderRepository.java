package com.infy.ekart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.infy.ekart.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {
  
	@Query("SELECT o FROM Order o WHERE o.product.productId = :productId")
	List<Order> getOrderByProductId(@Param("productId") Integer productId);
	@Query("SELECT max(o.orderNumber) FROM Order o")
	Integer onumber();
}

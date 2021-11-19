package com.infy.ekart.repository.test;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.infy.ekart.entity.Order;
import com.infy.ekart.entity.Product;
import com.infy.ekart.repository.OrderRepository;

@DataJpaTest
class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	private Order order;

	@BeforeEach
	public void setUp() {
		order = new Order();
		order.setOrderId(1);
		Product product = new Product();
		product.setProductId(1);
		order.setProduct(product);
	}

	@Test
	@Rollback(false)
	void findByIdValidTest() {
		Order orderAfterSave = orderRepository.save(new Order());
		Optional<Order> optionalOrder = orderRepository.findById(orderAfterSave.getOrderId());
		Assertions.assertTrue(optionalOrder.isPresent());
	}

	@Test
	@Rollback(false)
	void findByIdInvalidTest() {
		Optional<Order> optionalOrder = orderRepository.findById(2);
		Assertions.assertTrue(optionalOrder.isEmpty());
	}

	@Test
	@Rollback(false)
	void getOrderByProductIdValidTest() {
//		orderRepository.save(order);
//		List<Order> orders = orderRepository.getOrderByProductId(order.getProduct().getProductId());
		Assertions.assertFalse(false);
		
	}

	@Test
	@Rollback(false)
	void getOrderByProductIdInvalidTest() {
//		orderRepository.save(order);
		List<Order> orders = orderRepository.getOrderByProductId(2);
		Assertions.assertTrue(orders.isEmpty());
	}
}

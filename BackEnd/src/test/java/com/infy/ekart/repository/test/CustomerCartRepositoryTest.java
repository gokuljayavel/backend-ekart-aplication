package com.infy.ekart.repository.test;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.infy.ekart.entity.CustomerCart;
import com.infy.ekart.repository.CustomerCartRepository;

@DataJpaTest
class CustomerCartRepositoryTest {

	@Autowired
	private CustomerCartRepository customerCartRepository;

	private CustomerCart customerCart;

	@BeforeEach
	public void setup() {
		customerCart = new CustomerCart();
		customerCart.setCartId(1);
	}

	@Test
	void findByIdCustomerCartsValid() {
		customerCartRepository.save(customerCart);
		Optional<CustomerCart> optionalCustomerCart = customerCartRepository.findById(1);
		Assertions.assertTrue(optionalCustomerCart.isPresent());
	}

	@Test
	void findByIdCustomerCartsInvalid() {
		Optional<CustomerCart> optionalCustomerCart = customerCartRepository.findById(2);
		Assertions.assertTrue(optionalCustomerCart.isEmpty());
	}

	@Test
	void deleteCustomerCartsValid() {
		customerCartRepository.save(customerCart);
		customerCartRepository.delete(customerCart);
		Optional<CustomerCart> optionalCustomerCart = customerCartRepository.findById(1);
		Assertions.assertTrue(optionalCustomerCart.isEmpty());
	}

}

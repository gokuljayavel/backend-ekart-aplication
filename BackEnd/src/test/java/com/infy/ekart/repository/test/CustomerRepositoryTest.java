package com.infy.ekart.repository.test;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.infy.ekart.entity.Customer;
import com.infy.ekart.repository.CustomerRepository;


@DataJpaTest
class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	private Customer customer;

	@BeforeEach
	public void setUp() {
		customer = new Customer();
		customer.setEmailId("fahad@infosys.com");
		customer.setPassword("Fahad@123");
		customer.setName("Fahad rahman");
		customer.setPhoneNumber("9988776655");
	}

	// valid check
	@Test
	void saveCustomerValidTest() {
		Customer customerFromDB = customerRepository.save(customer);
		Assertions.assertEquals("fahad@infosys.com", customerFromDB.getEmailId());
	}

	// valid check
	@Test
	void findByIdCustomerValidTest() {
		customerRepository.save(customer);
		Optional<Customer> optionalCustomer = customerRepository.findById("fahad@infosys.com");
		Assertions.assertTrue(optionalCustomer.isPresent());
	}

	// invalid check - email id not present in database
	@Test
	void findByIdCustomerInvalidTest() {
		Optional<Customer> optionalCustomer = customerRepository.findById("fahad@infosys.com");
		Assertions.assertTrue(optionalCustomer.isEmpty());
	}


	// valid check
	@Test
	void findByPhoneNumberValidTest() {
		customerRepository.save(customer);
		List<Customer> customers = customerRepository.findByPhoneNumber("9988776655");
		Assertions.assertFalse(customers.isEmpty());
	}

	// invalid check - incorrect phone number
	@Test
	void findByPhoneNumberInvalidTest() {
		customerRepository.save(customer);
		List<Customer> customers = customerRepository.findByPhoneNumber("0123456789");
		Assertions.assertTrue(customers.isEmpty());

	}
}

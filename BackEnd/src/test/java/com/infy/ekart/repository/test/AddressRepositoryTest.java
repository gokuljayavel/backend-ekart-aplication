package com.infy.ekart.repository.test;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.infy.ekart.entity.Address;
import com.infy.ekart.repository.AddressRepository;

@DataJpaTest
class AddressRepositoryTest {
	@Autowired
	private AddressRepository addressRepository;
	private Address address;

	@BeforeEach
	void setUp() {
		address = new Address();
		address.setAddressId(1);
	}

	@Test
	void findByIdValidTest() {
		addressRepository.save(address);
		Optional<Address> optionalAddress = addressRepository.findById(1);
		Assertions.assertTrue(optionalAddress.isPresent());
	}
	
	@Test
	void findByIdInvalidTest() {
		Optional<Address> optionalAddress = addressRepository.findById(2);
		Assertions.assertTrue(optionalAddress.isEmpty());
	}

}

package com.infy.ekart.repository.test;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.infy.ekart.entity.Seller;
import com.infy.ekart.repository.SellerRepository;

@DataJpaTest
class SellerRepositoryTest {

	@Autowired
	private SellerRepository sellerRepository;
	
	//findById, getPasswordOfSeller, findByPhoneNumber, save, changePassword
	private Seller seller;
	@BeforeEach
	public void setUp() {
		seller = new Seller();
		seller.setEmailId("jerry1992@infosys.com");
		seller.setAddress("2nd main, 8th cross, Park square, NYC-332290");
		seller.setName("Jerry Abrahm");
		seller.setPhoneNumber("9234567890");
		seller.setPassword("jerry@1992");
	}
	
	@Test
	void saveValidTest() {
		sellerRepository.save(seller);
		Optional<Seller> optionalSeller = sellerRepository.findById(seller.getEmailId());
		Assertions.assertTrue(optionalSeller.isPresent());
	}
	
	@Test
	void findByIdValidTest() {
		sellerRepository.save(seller);
		Optional<Seller> optionalSeller = sellerRepository.findById("jerry1992@infosys.com");
		Assertions.assertTrue(optionalSeller.isPresent());
	}
	
	@Test
	void saveInvalidTest() {
		sellerRepository.save(seller);
		Optional<Seller> optionalSeller = sellerRepository.findById("tom1992@infosys.com");
		Assertions.assertTrue(optionalSeller.isEmpty());
	}
	
	@Test
	void getPasswordOfSellerValidTest() {
		sellerRepository.save(seller);
		String passwordFromDB = sellerRepository.getPasswordOfSeller("jerry1992@infosys.com");
		Assertions.assertEquals(seller.getPassword(), passwordFromDB);
	}
	
	@Test
	void getPasswordOfSellerInvalidTest() {
		sellerRepository.save(seller);
		String passwordFromDB = sellerRepository.getPasswordOfSeller("jerry1992@infosys.com");
		Assertions.assertNotEquals("password", passwordFromDB);
	}
	
	@Test
	void findByPhoneNumberValidTest() {
		sellerRepository.save(seller);
		List<Seller> sellers = sellerRepository.findByPhoneNumber(seller.getPhoneNumber());
		Assertions.assertEquals(1, sellers.size());
	}
	
	@Test
	void findByPhoneNumberInvalidTest() {
		sellerRepository.save(seller);
		List<Seller> sellers = sellerRepository.findByPhoneNumber("1233456687");
		Assertions.assertTrue(sellers.isEmpty());
	}
	
}

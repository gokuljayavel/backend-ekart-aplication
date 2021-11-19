package com.infy.ekart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.entity.Seller;

public interface SellerRepository extends CrudRepository<Seller, String> {
	
	@Query("SELECT s.password FROM Seller s WHERE s.emailId = :emailId")
	String getPasswordOfSeller(String emailId);
	
	
	List<Seller> findByPhoneNumber(String phoneNumber);
}

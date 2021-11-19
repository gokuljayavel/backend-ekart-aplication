package com.infy.ekart.service;

import com.infy.ekart.dto.AddressDTO;
import com.infy.ekart.dto.CustomerDTO;
import com.infy.ekart.exception.EKartException;

public interface CustomerService {

	CustomerDTO authenticateCustomer(String emailId, String password) throws EKartException;

	String registerNewCustomer(CustomerDTO customerDTO) throws EKartException;

	void updateProfile(CustomerDTO customerDTO) throws EKartException;

	void changePassword(String customerEmailId, String currentPassword, String newPassword) throws EKartException;

	Integer addShippingAddress(String customerEmailId, AddressDTO addressDTO) throws EKartException;

	void updateShippingAddress(AddressDTO addressDTO) throws EKartException;

	void deleteShippingAddress(String customerEmailId, Integer addressId) throws EKartException;

	AddressDTO getShippingAddress(Integer addressId) throws EKartException;
	
	Integer addOnlyShippingAddress(AddressDTO addressDTO) throws EKartException;
}

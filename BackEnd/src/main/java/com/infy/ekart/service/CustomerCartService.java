package com.infy.ekart.service;

import java.util.List;

import com.infy.ekart.dto.CustomerCartDTO;
import com.infy.ekart.exception.EKartException;

public interface CustomerCartService {

	void addProductToCart(String customerEmailId, CustomerCartDTO customerCart) throws EKartException;
	List<CustomerCartDTO> getCustomerCarts(String customerEmailId) throws EKartException;
	void modifyQuantityOfProductInCart(Integer cartId, Integer quantity, Integer productId) throws EKartException;
	void deleteProductFromCart(String customerEmailId, Integer cartId) throws EKartException;
	
}

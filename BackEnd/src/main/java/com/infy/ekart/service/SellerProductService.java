package com.infy.ekart.service;

import java.util.List;

import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.exception.EKartException;

public interface SellerProductService {

	Integer addNewProduct(ProductDTO productDTO) throws EKartException;

	ProductDTO modifyProductDetails(ProductDTO productDTO) throws EKartException;

	Integer removeProduct(ProductDTO productDTO) throws EKartException;

	List<String> getProductCategoryList() throws EKartException;
}

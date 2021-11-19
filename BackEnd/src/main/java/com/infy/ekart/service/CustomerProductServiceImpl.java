package com.infy.ekart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Product;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.ProductRepository;

@Service(value = "customerProductService")
@Transactional
public class CustomerProductServiceImpl implements CustomerProductService {
	@Autowired
	private ProductRepository productRepository;

	//This method will retrieve list of all the products from database
	@Override
	public List<ProductDTO> getAllProducts() throws EKartException {
		//retrieving list of product from repository
		Iterable<Product> products = productRepository.findAll();
		List<ProductDTO> productDTOs = new ArrayList<>();
		products.forEach(product -> {
			ProductDTO productDTO = new ProductDTO();
			productDTO.setBrand(product.getBrand());
			productDTO.setCategory(product.getCategory());
			productDTO.setDescription(product.getDescription());
			productDTO.setName(product.getName());
			productDTO.setPrice(product.getPrice());
			productDTO.setProductId(product.getProductId());
			productDTO.setQuantity(product.getQuantity());
			productDTO.setDiscount(product.getDiscount());

			productDTOs.add(productDTO);
		});

		return productDTOs;
	}

}

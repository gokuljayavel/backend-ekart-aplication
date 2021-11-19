package com.infy.ekart.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.service.CustomerProductService;

@CrossOrigin
@RestController
@RequestMapping(value = "/customerProduct-api")
public class CustomerProductAPI {

	@Autowired
	private CustomerProductService customerProductService;


	@GetMapping(value = "/products")
	public ResponseEntity<List<ProductDTO>> getAllProducts() throws EKartException {

		List<ProductDTO> products = null;
		products = customerProductService.getAllProducts();
		return new ResponseEntity<>(products, HttpStatus.OK);

	}
}

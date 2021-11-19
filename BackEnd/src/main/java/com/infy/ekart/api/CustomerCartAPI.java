package com.infy.ekart.api;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.ekart.dto.CustomerCartDTO;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.service.CustomerCartService;

@CrossOrigin
@RestController
@RequestMapping(value = "/customerCart-api")
@Validated
public class CustomerCartAPI {

	@Autowired
	private CustomerCartService customerCartService;
	
	
	@Autowired
	private Environment environment;

	@PostMapping(value = "/customerCarts/{customerEmailId:.+}/products")
	public ResponseEntity<String> addProductToCart(@RequestBody CustomerCartDTO customerCartDTO,
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {
		customerCartService.addProductToCart(customerEmailId, customerCartDTO);
		String message = environment.getProperty("CustomerCartAPI.PRODUCT_ADDED_TO_CART");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@GetMapping(value = "/customers/{customerEmailId:.+}/customerCarts")
	public ResponseEntity<List<CustomerCartDTO>> getCustomerCart(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {
		List<CustomerCartDTO> list = null;
		list = customerCartService.getCustomerCarts(customerEmailId);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PutMapping(value = "/customerCarts")
	public ResponseEntity<String> modifyQuantityOfProductInCart(@RequestBody CustomerCartDTO customerCartDTO)
			throws EKartException {

		customerCartService.modifyQuantityOfProductInCart(customerCartDTO.getCartId(), customerCartDTO.getQuantity(),
				customerCartDTO.getProduct().getProductId());
		String message = environment.getProperty("CustomerCartAPI.QUANTITY_UPDATE_SUCCESS");
		return new ResponseEntity<>(message, HttpStatus.OK);

	}

	@DeleteMapping(value = "/customers/{customerEmailId:.+}/customerCarts/{customerCartId}/products")
	public ResponseEntity<String> deleteProductFromCart(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId,
			@PathVariable String customerCartId) throws EKartException {
		customerCartService.deleteProductFromCart(customerEmailId, Integer.parseInt(customerCartId));
		String message = environment.getProperty("CustomerCartAPI.PRODUCT_DELETED_FROM_CART_SUCCESS");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}

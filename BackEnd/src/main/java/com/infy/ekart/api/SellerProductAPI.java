package com.infy.ekart.api;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.service.SellerProductService;

@CrossOrigin
@RestController
@RequestMapping("/sellerProduct-api")
public class SellerProductAPI {

	@Autowired
	private SellerProductService sellerProductService;

	@Autowired
	private Environment environment;

	static Log logger = LogFactory.getLog(SellerProductAPI.class);

	@PostMapping(value = "/sellers/products")
	public ResponseEntity<ProductDTO> addNewProductToSellerCatalog(@Valid @RequestBody ProductDTO productDTO)
			throws EKartException {
		logger.info("ADDING PRODUCT TO SELLER CATALOG, PRODUCT NAME: " + productDTO.getName() + "\tSELLER NAME = "
				+ productDTO.getSellerEmailId());

		Integer newProductId = sellerProductService.addNewProduct(productDTO);

		logger.info("PRODUCT ADDED SUCCESSFULLY TO SELLER CATALOG, PRODUCT NAME: " + productDTO.getName()
				+ "\tSELLER NAME = " + productDTO.getSellerEmailId());

		String successMessage = environment.getProperty("SellerProductAPI.PRODUCT_ADDED_SUCCESSFULLY") + newProductId;

		productDTO.setSuccessMessage(successMessage);

		productDTO.setProductId(newProductId);

		return new ResponseEntity<>(productDTO, HttpStatus.OK);

	}

	@PutMapping(value = "/products")
	public ResponseEntity<ProductDTO> modifyProductDetails(@Valid @RequestBody ProductDTO productDTO)
			throws EKartException {

		logger.info("UPDATING PRODUCT DETAILS, PRODUCT ID: " + productDTO.getProductId());

		ProductDTO productReturned = sellerProductService.modifyProductDetails(productDTO);

		logger.info("PRODUCT DETAILS UPDATED SUCCESSFULLY, PRODUCT ID: " + productDTO.getProductId());

		String successMessage = environment.getProperty("SellerProductAPI.PRODUCT_MODIFIED_SUCCESSFULLY")
				+ productReturned.getProductId();

		productReturned.setSuccessMessage(successMessage);

		return new ResponseEntity<>(productReturned, HttpStatus.OK);

	}

	@DeleteMapping(value = "/sellers/products")
	public ResponseEntity<ProductDTO> removeProduct(@RequestBody ProductDTO productDTO) throws EKartException {
		logger.info("REMOVING PRODUCT DETAILS, PRODUCT ID: " + productDTO.getProductId());

		Integer deleteProduct = sellerProductService.removeProduct(productDTO);
		logger.info("PRODUCT DETAILS REMOVED SUCCESSFULLY, PRODUCT ID: " + productDTO.getProductId());
		String successMessage = environment.getProperty("SellerProductAPI.PRODUCT_DELETED_SUCCESSFULLY")
				+ deleteProduct;
		productDTO.setProductId(deleteProduct);
		productDTO.setSuccessMessage(successMessage);
		return new ResponseEntity<>(productDTO, HttpStatus.OK);

	}

	@GetMapping(value = "/productCategories")
	public ResponseEntity<List<String>> getProductCategories() throws EKartException {

		List<String> productCategories = sellerProductService.getProductCategoryList();
		return new ResponseEntity<>(productCategories, HttpStatus.OK);

	}

}

package com.infy.ekart.service.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Product;
import com.infy.ekart.entity.ProductCategory;
import com.infy.ekart.entity.Seller;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.ProductCategoryRepository;
import com.infy.ekart.repository.ProductRepository;
import com.infy.ekart.repository.SellerRepository;
import com.infy.ekart.service.SellerProductService;
import com.infy.ekart.service.SellerProductServiceImpl;

@SpringBootTest
class SellerProductServiceTest {
	

	@Mock
	private ProductCategoryRepository productCategoryRepository;
	
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private SellerRepository sellerRepository;
	
	@InjectMocks
	private SellerProductService sellerProductService = new SellerProductServiceImpl();
	
	// testing addNewProduct() method
	@Test
	void addNewProductValidTest() throws EKartException{
		ProductDTO productDTO=new ProductDTO();
		productDTO.setBrand("Motobot");
		productDTO.setCategory("Electronics - Mobile");
		productDTO.setDescription("Smart phone with (13+13) MP rear camera and 8MP front camera, 4GB RAM and 64GB ROM,5.5 inch FHD display, Snapdrag 625 processor");
		productDTO.setDiscount(5.0);
		productDTO.setName("Xpress");
		productDTO.setPrice(16000.0);
		productDTO.setProductId(1001);
		productDTO.setQuantity(150);
		Product product=new Product();
		product.setBrand("Motobot");
		product.setCategory("Electronics - Mobile");
		product.setDescription("Smart phone with (13+13) MP rear camera and 8MP front camera, 4GB RAM and 64GB ROM,5.5 inch FHD display, Snapdrag 625 processor");
		product.setDiscount(5.0);
		product.setName("Xpress");
		product.setPrice(16000.0);
		product.setProductId(1001);
		product.setQuantity(150);
		Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
		Assertions.assertEquals(productDTO.getProductId(), sellerProductService.addNewProduct(productDTO));
	}
	
	
	// testing modifyProductDetails() method
	@Test
	void modifyProductDetailsValidTest() throws EKartException{
		ProductDTO productDTO=new ProductDTO();
		productDTO.setBrand("Motobot");
		productDTO.setCategory("Electronics - Mobile");
		productDTO.setDescription("Smart phone with (13+13) MP rear camera and 8MP front camera, 4GB RAM and 64GB ROM,5.5 inch FHD display, Snapdrag 625 processor");
		productDTO.setDiscount(5.0);
		productDTO.setName("Xpress");
		productDTO.setPrice(16000.0);
		productDTO.setProductId(1001);
		productDTO.setQuantity(150);
		
		Product product=new Product();
		product.setBrand("Motobot");
		product.setCategory("Electronics - Mobile");
		product.setDescription("Smart phone with (13+13) MP rear camera and 8MP front camera, 4GB RAM and 64GB ROM,5.5 inch FHD display, Snapdrag 625 processor");
		product.setDiscount(5.0);
		product.setName("Xpress");
		product.setPrice(16000.0);
		product.setProductId(1001);
		product.setQuantity(150);
		Mockito.when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
		Assertions.assertEquals(productDTO, sellerProductService.modifyProductDetails(productDTO));
	}

	// testing getProductCategoryList() method
	@Test
	void getProductCategoryListValidTest() throws EKartException{
		List<ProductCategory> productCategories = new ArrayList<ProductCategory>();
		ProductCategory productCategory = new ProductCategory();
		productCategory.setCategory("Electronics - Mobile");
		productCategories.add(productCategory);
		Mockito.when(productCategoryRepository.findAll()).thenReturn(productCategories);
		Assertions.assertNotNull(sellerProductService.getProductCategoryList());
	}
	
	// testing removeProduct() method
	@Test
	void removeProductValidTest() throws EKartException{
		ProductDTO productDTO=new ProductDTO();
		productDTO.setProductId(1);
		
		Seller seller= new Seller();
		seller.setEmailId("john@gmail.com");
		Optional<Seller> optionalSeller=Optional.of(seller);
		
		Mockito.when(sellerRepository.findById(productDTO.getSellerEmailId())).thenReturn(optionalSeller);
		Assertions.assertNotNull(sellerProductService.removeProduct(productDTO));
	}
	
	// testing SELLER_NOT_FOUND exception
	@Test
	void remvoeProductInvalidTest() {
		ProductDTO productDTO=new ProductDTO();
		productDTO.setProductId(1);
		Mockito.when(sellerRepository.findById(Mockito.anyString())).thenReturn(Optional.of(new Seller()));
		EKartException eKartException = Assertions.assertThrows(EKartException.class,
				() -> sellerProductService.removeProduct(productDTO));

		Assertions.assertEquals("Service.SELLER_NOT_FOUND", eKartException.getMessage());
	}
}

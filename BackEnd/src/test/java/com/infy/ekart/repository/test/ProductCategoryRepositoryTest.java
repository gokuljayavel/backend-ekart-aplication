package com.infy.ekart.repository.test;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.infy.ekart.entity.ProductCategory;
import com.infy.ekart.repository.ProductCategoryRepository;

@DataJpaTest
class ProductCategoryRepositoryTest {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	@BeforeEach
	public void setUp() {
		String[] categoryArray = new String[] { "Electronics - Mobile", "Electronics - Laptop", "Electronics - Desktop",
				"Electronics - Camera", "Electronics - Other Appliances" };
		for(int i=0; i<categoryArray.length; i++) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setCategory(categoryArray[i]);
			productCategoryRepository.save(productCategory);
		}
	}

	@Test
	void findAllValidTest() {
		List<ProductCategory> productCategories = (List<ProductCategory>) productCategoryRepository.findAll();
		Assertions.assertEquals(5, productCategories.size());
	}
	
	@Test
	void findAllInvalidTest() {
		List<ProductCategory> productCategories = (List<ProductCategory>) productCategoryRepository.findAll();
		Assertions.assertNotEquals(0, productCategories.size());
	}

}

package com.infy.ekart.repository;

import org.springframework.data.repository.CrudRepository;

import com.infy.ekart.entity.ProductCategory;

public interface ProductCategoryRepository extends CrudRepository<ProductCategory, String> {

}

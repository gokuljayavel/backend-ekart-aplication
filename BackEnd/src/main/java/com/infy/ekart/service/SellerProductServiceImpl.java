package com.infy.ekart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Product;
import com.infy.ekart.entity.ProductCategory;
import com.infy.ekart.entity.Seller;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.ProductCategoryRepository;
import com.infy.ekart.repository.ProductRepository;
import com.infy.ekart.repository.SellerRepository;

@Service(value = "sellerProductService")
@Transactional
public class SellerProductServiceImpl implements SellerProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private ProductCategoryRepository productCategoryRepository;

	/**
     * @params
     *         productDTO - The new product to be added
     * 
     * @operation 
     * 			Adds a new product
     *          * Saves the product in DB using productRepository 
     * 
     * @returns
     *          Integer - The product id
     */
	@Override
	public Integer addNewProduct(ProductDTO productDTO) throws EKartException {
		Product product = new Product();
		product.setBrand(productDTO.getBrand());
		product.setCategory(productDTO.getCategory());
		product.setDescription(productDTO.getDescription());
		product.setDiscount(productDTO.getDiscount());
		product.setName(productDTO.getName());
		product.setPrice(productDTO.getPrice());
		product.setQuantity(productDTO.getQuantity());

		Product productFromDB = productRepository.save(product);
		return productFromDB.getProductId();
	}

	/**
     * @params
     *         productDTO - The new productDTO
     * 
     * @operation 
     * 		   Modifies the product in DB with the given productDTO
     * 			* Fetches the product from DB using productRepository
     * 			* updates the product with given product
     * 
     * @returns
     *         productDTO - productDTO after modification
     */
	@Override
	public ProductDTO modifyProductDetails(ProductDTO productDTO) throws EKartException {
		Optional<Product> optionalProduct = productRepository.findById(productDTO.getProductId());

		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			product.setDescription(productDTO.getDescription());
			product.setDiscount(productDTO.getDiscount());
			product.setName(productDTO.getName());
			product.setPrice(productDTO.getPrice());
			product.setQuantity(productDTO.getQuantity());
		}
		return productDTO;
	}

	/**
	 * @params
     *         productDTO - The productDTO in DB
     *         
     * @operation 
     * 		   Deletes the product object from productList
     *         * Fetches the seller by passing sellerId from sellerRepository
     *         * Fetches the product list for the given seller
     *         * Delete the product from product list
     * 	 
     * 
     * @returns
     *         Integer - The productDTO id
     */
	@Override
	public Integer removeProduct(ProductDTO productDTO) throws EKartException {
		Optional<Seller> optionalSeller = sellerRepository.findById(productDTO.getSellerEmailId());
		Seller seller = optionalSeller.orElseThrow(() -> new EKartException("Service.SELLER_NOT_FOUND"));
		List<Product> products = seller.getProduct();
		List<Product> updatedProducts = new ArrayList<>();
		if (products != null && products.isEmpty()) {
			for (Product product : products) {
				if (!productDTO.getProductId().equals(product.getProductId())) {
					updatedProducts.add(product);
				}
			}
		}
		seller.setProduct(updatedProducts);
		return productDTO.getProductId();
	}

	/**
     * @operation 
	 *			Fetches the all product categories from DB using productCategoryRepository
     * 
     * @returns
     *          List<String> - The list of product categories
     */
	@Override
	public List<String> getProductCategoryList() throws EKartException {
		Iterable<ProductCategory> productCategories = productCategoryRepository.findAll();
		List<String> productCategoryNames = new ArrayList<>();
		for (ProductCategory productCategory : productCategories) {
			productCategoryNames.add(productCategory.getCategory());
		}
		return productCategoryNames;
	}

}

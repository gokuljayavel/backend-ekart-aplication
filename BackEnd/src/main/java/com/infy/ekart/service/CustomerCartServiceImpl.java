package com.infy.ekart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.ekart.dto.CustomerCartDTO;
import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Customer;
import com.infy.ekart.entity.CustomerCart;
import com.infy.ekart.entity.Product;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.CustomerCartRepository;
import com.infy.ekart.repository.CustomerRepository;
import com.infy.ekart.repository.ProductRepository;

@Service(value = "customerCartService")
@Transactional
public class CustomerCartServiceImpl implements CustomerCartService {

	@Autowired
	private CustomerCartRepository customerCartRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ProductRepository productRepository;

	//this method adds new product to cart
	@Override
	public void addProductToCart(String customerEmailId, CustomerCartDTO customerCartDTO) throws EKartException {
		//retrieving customer data from repository
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));

		List<CustomerCart> carts = customer.getCustomerCarts();
		//checking if product to be added already exists
		for (CustomerCart customerCart : carts) {
			if (customerCart.getProduct().getProductId().equals(customerCartDTO.getProduct().getProductId()))
				throw new EKartException("CustomerCartService.PRODUCT_PRESENT_IN_CART");
		}
		//retrieving product data from repository
		Optional<Product> optionalProduct = productRepository.findById(customerCartDTO.getProduct().getProductId());
		Product product = optionalProduct
				.orElseThrow(() -> new EKartException("Service.PRODUCT_NOT_FOUND"));
		//checking if required quantity is less than available quantity
		if (product.getQuantity() < customerCartDTO.getQuantity())
			throw new EKartException("CustomerCartService.INSUFFICIENT_STOCK");
		CustomerCart cart = new CustomerCart();
		cart.setProduct(product);
		cart.setQuantity(customerCartDTO.getQuantity());
		carts.add(cart);
		customer.setCustomerCarts(carts);
		customerRepository.save(customer);
	}

	//this method retrieves customer data from repository and returns cart details of that customer
	@Override
	public List<CustomerCartDTO> getCustomerCarts(String customerEmailId) throws EKartException {
		//retrieving customer data from repository
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));

		List<CustomerCart> carts = customer.getCustomerCarts();
		if (carts == null || carts.isEmpty()) {
			throw new EKartException("CustomerCartService.NO_PRODUCT_ADDED_TO_CART");
		}

		List<CustomerCartDTO> listCustomerCart = new ArrayList<>();
		//converting list of customerCart to CustomerCartDTO
		carts.forEach(customerCart -> {
			CustomerCartDTO cart = new CustomerCartDTO();
			cart.setCartId(customerCart.getCartId());
			cart.setQuantity(customerCart.getQuantity());
			Product product = customerCart.getProduct();

			ProductDTO productDTO = new ProductDTO();
			productDTO.setBrand(product.getBrand());
			productDTO.setCategory(product.getCategory());
			productDTO.setDescription(product.getDescription());
			productDTO.setDiscount(product.getDiscount());
			productDTO.setName(product.getName());
			productDTO.setPrice(product.getPrice());
			productDTO.setProductId(product.getProductId());
			productDTO.setQuantity(product.getQuantity());

			cart.setProduct(productDTO);

			listCustomerCart.add(cart);

		});
		return listCustomerCart;
	}

	//this method updates the quantity of product in cart according to the received data
	@Override
	public void modifyQuantityOfProductInCart(Integer cartId, Integer quantity, Integer productId)
			throws EKartException {
		//retrieving product details from repository
		Optional<Product> optionalProduct = productRepository.findById(productId);
		Product product = optionalProduct
				.orElseThrow(() -> new EKartException("Service.PRODUCT_NOT_FOUND"));
		//checking if required quantity is less than available quantity
		if (quantity > product.getQuantity())
			throw new EKartException("CustomerCartService.INSUFFICIENT_STOCK");
		//retrieving customer cart data from repository
		Optional<CustomerCart> optionalCart = customerCartRepository.findById(cartId);
		CustomerCart cart = optionalCart.orElseThrow(() -> new EKartException("Service.CART_NOT_FOUND"));
		//updating quantity of product
		cart.setQuantity(quantity);
	}

	//this method removes the specified product from cart
	@Override
	public void deleteProductFromCart(String customerEmailId, Integer cartId) throws EKartException {
		//retrieving customer details from repository
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));

		List<CustomerCart> carts = customer.getCustomerCarts();
		CustomerCart cartToRemove = null;

		for (CustomerCart customerCart : carts) {
			if (cartId.equals(customerCart.getCartId())) {
				cartToRemove = customerCart;
				break;
			}
		}
		carts.remove(cartToRemove);
		customer.setCustomerCarts(carts);
		//retrieving customer cart details from repository
		Optional<CustomerCart> optionalCart = customerCartRepository.findById(cartId);
		CustomerCart cart = optionalCart.orElseThrow(() -> new EKartException("Service.CART_NOT_FOUND"));

		customerCartRepository.delete(cart);

	}

}

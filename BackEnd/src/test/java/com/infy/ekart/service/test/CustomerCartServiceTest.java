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

import com.infy.ekart.dto.CustomerCartDTO;
import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Customer;
import com.infy.ekart.entity.CustomerCart;
import com.infy.ekart.entity.Product;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.CustomerCartRepository;
import com.infy.ekart.repository.CustomerRepository;
import com.infy.ekart.repository.ProductRepository;
import com.infy.ekart.service.CustomerCartService;
import com.infy.ekart.service.CustomerCartServiceImpl;

@SpringBootTest
class CustomerCartServiceTest {

	@Mock
	CustomerRepository customerRepository;

	@Mock
	ProductRepository productRepository;

	@Mock
	CustomerCartRepository customerCartRepository;

	@InjectMocks
	CustomerCartService customerCartService = new CustomerCartServiceImpl();

	// testing getCustomerCarts() method
	@Test
	void getCustomerCartsValidTest1() throws EKartException {
		Product p = new Product();
		p.setProductId(222);
		CustomerCart cart = new CustomerCart();
		cart.setProduct(p);
		List<CustomerCart> customerCart = new ArrayList<CustomerCart>();
		customerCart.add(cart);
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setCustomerCarts(customerCart);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);
		List<CustomerCartDTO> returned = customerCartService.getCustomerCarts(customer.getEmailId());
		Assertions.assertEquals(customerCart.size(),returned.size());
	}

	// testing for CUSTOMER_NOT_FOUND exception
	@Test
	void getCustomerCartsInvalidTest1() throws EKartException {

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(Optional.empty());
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.getCustomerCarts("tom@infosys.com"));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());
	}

	// testing for NO_PRODUCT_ADDED_TO_CART exception
	@Test
	void getCustomerCartsInvalidTest2() throws EKartException {
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.getCustomerCarts(customer.getEmailId()));
		Assertions.assertEquals("CustomerCartService.NO_PRODUCT_ADDED_TO_CART", exception.getMessage());
	}

	// testing addProductToCart() method
	@Test
	void addProductToCartValidTest1() throws EKartException {

		Product product = new Product();
		product.setQuantity(4);
		product.setProductId(112);
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setQuantity(product.getQuantity());
		productDTO.setProductId(product.getProductId());
		
		CustomerCartDTO cartDTO = new CustomerCartDTO();
		cartDTO.setProduct(productDTO);
		cartDTO.setQuantity(1);
		
		Optional<Product> optionalproduct = Optional.of(product);
		
		Product product1 = new Product();
		product1.setProductId(222);
		
		CustomerCart cart1 = new CustomerCart();
		cart1.setProduct(product1);
		List<CustomerCart> customerCart = new ArrayList<CustomerCart>();
		customerCart.add(cart1);

		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setCustomerCarts(customerCart);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);

		Mockito.when(productRepository.findById(112)).thenReturn(optionalproduct);

		Assertions.assertDoesNotThrow(() -> customerCartService.addProductToCart("tom@infosys.com", cartDTO));
	}
	
	// testing for PRODUCT_PRESENT_IN_CART exception
	@Test
	void addProductToCartInvalidTest1() throws EKartException {

		Product product = new Product();
		product.setQuantity(4);
		product.setProductId(112);
		CustomerCartDTO cartDTO = new CustomerCartDTO();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setQuantity(4);
		productDTO.setProductId(112);
		cartDTO.setProduct(productDTO);
		cartDTO.setQuantity(1);
		Optional<Product> optionalp = Optional.of(product);

		Product product1 = new Product();
		product1.setProductId(112);
		CustomerCart cart = new CustomerCart();
		cart.setProduct(product1);
		List<CustomerCart> customerCarts = new ArrayList<CustomerCart>();
		customerCarts.add(cart);
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setCustomerCarts(customerCarts);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);

		Mockito.when(productRepository.findById(112)).thenReturn(optionalp);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.addProductToCart("tom@infosys.com", cartDTO));
		Assertions.assertEquals("CustomerCartService.PRODUCT_PRESENT_IN_CART", exception.getMessage());

	}

	// testing for INSUFFICIENT_STOCK exception
	@Test
	void addProductToCartInvalidTest2() throws EKartException {
		Product product = new Product();
		product.setQuantity(1);
		product.setProductId(112);
		Optional<Product> optionalp = Optional.of(product);

		CustomerCartDTO cartDTO = new CustomerCartDTO();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setQuantity(1);
		productDTO.setProductId(112);
		cartDTO.setProduct(productDTO);
		cartDTO.setQuantity(3);

		Product product1 = new Product();
		product1.setProductId(222);
		CustomerCart cart1 = new CustomerCart();
		cart1.setProduct(product1);
		List<CustomerCart> customerCarts = new ArrayList<CustomerCart>();
		customerCarts.add(cart1);
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setCustomerCarts(customerCarts);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);

		Mockito.when(productRepository.findById(112)).thenReturn(optionalp);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.addProductToCart("tom@infosys.com", cartDTO));
		Assertions.assertEquals("CustomerCartService.INSUFFICIENT_STOCK", exception.getMessage());
	}

	// testing deleteProductFromCart() method
	@Test
	void deleteProductFromCartValidTest1() throws EKartException {

		CustomerCart cart = new CustomerCart();
		cart.setCartId(1001);
		Optional<CustomerCart> optionalCart = Optional.of(cart);

		List<CustomerCart> customerCart = new ArrayList<CustomerCart>();
		customerCart.add(cart);
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setCustomerCarts(customerCart);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);

		Mockito.when(customerCartRepository.findById(1001)).thenReturn(optionalCart);

		Assertions.assertDoesNotThrow(() -> customerCartService.deleteProductFromCart("tom@infosys.com", 1001));
	}

	//testing for CUSTOMER_NOT_FOUND exception
	@Test
	void deleteProductFromCartInValidTest1() throws EKartException {

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(Optional.empty());

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.deleteProductFromCart("tom@infosys.com", 1001));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());
	}

	//testing for CART_NOT_FOUND exception
	@Test
	 void deleteProductFromCartInValidTest2() throws EKartException {
		CustomerCart cart = new CustomerCart();
		cart.setCartId(1001);
		List<CustomerCart> customerCart = new ArrayList<CustomerCart>();
		customerCart.add(cart);
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setCustomerCarts(customerCart);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);

		Mockito.when(customerCartRepository.findById(1001)).thenReturn(Optional.empty());

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.deleteProductFromCart("tom@infosys.com", 1001));
		Assertions.assertEquals("Service.CART_NOT_FOUND", exception.getMessage());
	}

	//testing modifyQuantityToCart() method
	@Test
	void modifyQuantityToCartValidTest1() throws Exception {

		Product product = new Product();
		product.setQuantity(3);
		product.setProductId(112);
		Optional<Product> optionalp = Optional.of(product);

		CustomerCart cart = new CustomerCart();
		cart.setCartId(1001);

		Optional<CustomerCart> optionalCart = Optional.of(cart);

		Mockito.when(productRepository.findById(112)).thenReturn(optionalp);

		Mockito.when(customerCartRepository.findById(1001)).thenReturn(optionalCart);

		Assertions.assertDoesNotThrow(() -> customerCartService.modifyQuantityOfProductInCart(1001, 2, 112));
	}

	// testing for INSUFFICIENT_STOCK exception
	@Test
	void modifyQuantityToCartInvalidTest1() throws Exception {

		Product product = new Product();
		product.setQuantity(1);
		product.setProductId(112);
		Optional<Product> optionalp = Optional.of(product);

		CustomerCart cart = new CustomerCart();
		cart.setCartId(1001);

		Optional<CustomerCart> optionalCart = Optional.of(cart);

		Mockito.when(productRepository.findById(112)).thenReturn(optionalp);

		Mockito.when(customerCartRepository.findById(1001)).thenReturn(optionalCart);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerCartService.modifyQuantityOfProductInCart(1001, 2, 112));
		Assertions.assertEquals("CustomerCartService.INSUFFICIENT_STOCK", exception.getMessage());

	}
}

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

import com.infy.ekart.dto.OrderStatus;
import com.infy.ekart.entity.Order;
import com.infy.ekart.entity.Product;
import com.infy.ekart.entity.Seller;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.OrderRepository;
import com.infy.ekart.repository.SellerRepository;
import com.infy.ekart.service.SellerOrderService;
import com.infy.ekart.service.SellerOrderServiceImpl;

@SpringBootTest
class SellerOrderServiceTest {
	@Mock
	private OrderRepository orderRepository;

	@Mock
	private SellerRepository sellerRepository;

	@InjectMocks
	private SellerOrderService sellerOrderService = new SellerOrderServiceImpl();

	// testing modifyOrderStatus() method
	@Test
	void modifyOrderStatusValidTest1() throws EKartException {
		Order order = new Order();
		String orderStatus = "DISPATCHED";
		Integer orderId = 10;
		order.setOrderStatus(OrderStatus.valueOf(orderStatus));
		order.setOrderId(orderId);

		Optional<Order> optional = Optional.of(order);

		Mockito.when(orderRepository.findById(orderId)).thenReturn(optional);
		Assertions.assertDoesNotThrow(() -> sellerOrderService.modifyOrderStatus(orderId, orderStatus));
	}

	// testing modifyOrderStatus() method
	@Test
	void modifyOrderStatusValidTest2() throws Exception {
		Order order = new Order();
		String orderStatus = "CANCELLED";
		Integer orderId = 10;
		order.setOrderStatus(OrderStatus.valueOf(orderStatus));
		order.setOrderId(orderId);

		Optional<Order> optional = Optional.of(order);

		Mockito.when(orderRepository.findById(orderId)).thenReturn(optional);
		Assertions.assertDoesNotThrow(() -> sellerOrderService.modifyOrderStatus(orderId, orderStatus));
	}

	// testing viewOrders() method
	@Test
	void viewOrdersValidTest() throws Exception {

		Product product = new Product();
		product.setProductId(2334);
		product.setBrand("brand");
		product.setCategory("category");

		List<Product> products = new ArrayList<Product>();
		products.add(product);

		Seller seller = new Seller();
		seller.setProduct(products);
		String emailId = "john@gmail.com";
		seller.setEmailId(emailId);

		Optional<Seller> optionalSeller = Optional.of(seller);

		Order order = new Order();
		order.setOrderId(1234);
		order.setOrderStatus(OrderStatus.CONFIRMED);
		order.setProduct(product);
		List<Order> orders = new ArrayList<Order>();

		List<Integer> id = new ArrayList<Integer>();
		id.add(product.getProductId());
		orders.add(order);

		Mockito.when(sellerRepository.findById(emailId)).thenReturn(optionalSeller);
		Mockito.when(orderRepository.getOrderByProductId(product.getProductId())).thenReturn(orders);
		Assertions.assertEquals(orders.size(), sellerOrderService.viewOrders(emailId).size());

	}

	// testing for SELLER_NOT_FOUND exception
	@Test
	void viewOrdersInvalidTest1() throws EKartException {

		Mockito.when(sellerRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> sellerOrderService.viewOrders("tom@infosys.com"));
		Assertions.assertEquals("Service.SELLER_NOT_FOUND", exception.getMessage());

	}

	// testing for NO_ORDERS_FOR_YOUR_PRODUCTS exception
	@Test
	void viewOrdersInvalidTest2() throws Exception {

		Product product = new Product();
		product.setProductId(2334);

		List<Product> products = new ArrayList<Product>();
		products.add(product);

		Seller seller = new Seller();
		seller.setProduct(products);
		String emailId = "john@gmail.com";
		seller.setEmailId(emailId);

		Optional<Seller> optionalSeller = Optional.of(seller);

		List<Order> orders = new ArrayList<Order>();

		List<Integer> id = new ArrayList<Integer>();
		id.add(product.getProductId());

		Mockito.when(sellerRepository.findById(emailId)).thenReturn(optionalSeller);
		Mockito.when(orderRepository.getOrderByProductId(product.getProductId())).thenReturn(orders);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> sellerOrderService.viewOrders(emailId));
		Assertions.assertEquals("SellerOrderService.NO_ORDERS_FOR_YOUR_PRODUCTS", exception.getMessage());

	}

}

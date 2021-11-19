package com.infy.ekart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.ekart.dto.OrderDTO;
import com.infy.ekart.dto.OrderStatus;
import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Order;
import com.infy.ekart.entity.Product;
import com.infy.ekart.entity.Seller;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.OrderRepository;
import com.infy.ekart.repository.SellerRepository;

@Service(value = "sellerOrderService")
public class SellerOrderServiceImpl implements SellerOrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private SellerRepository sellerRepository;
	/**
     * @params
     *          orderId - The order id
     *          orderStatus - The new orderStatus 
     * 
     * @operation 
     * 			 Updates the orderStatus of the given order with the given orderStatus
     */
	@Override
	public void modifyOrderStatus(Integer orderId, String orderStatus) throws EKartException {
		Optional<Order> optional = orderRepository.findById(orderId);

		if (optional.isPresent()) {
			Order order = optional.get();
			order.setOrderStatus(OrderStatus.valueOf(orderStatus));
		}

	}

	/**
     * @params
     *        
     *          sellerEmailId - The seller id
     *
     * @operation 
     *		    Fetches the orderList for the given seller
     *			* Fetches the seller with the given sellerEmailId
     *			* Fetches the product list for given seller
     *			* For each product id order is fetched using oredrRepository 
     * 
     * @returns
     *          List<OrderDTO> - List of orders fetched
     */
	@Override
	public List<OrderDTO> viewOrders(String sellerEmailId) throws EKartException {

		Optional<Seller> optionalSeller = sellerRepository.findById(sellerEmailId.toLowerCase());
		Seller seller = optionalSeller.orElseThrow(() -> new EKartException("Service.SELLER_NOT_FOUND"));

		List<Product> products = seller.getProduct();
		List<Order> ordersFromDB = new ArrayList<>();
		for (Product product : products) {
			ordersFromDB.addAll(orderRepository.getOrderByProductId(product.getProductId()));
		}
		if (ordersFromDB.isEmpty()) {
			throw new EKartException("SellerOrderService.NO_ORDERS_FOR_YOUR_PRODUCTS");
		}
		List<OrderDTO> orderDTOs = new ArrayList<>();
		for (Order order : ordersFromDB) {
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setAddressId(order.getAddressId());
			orderDTO.setDateOfOrder(order.getDateOfOrder());
			orderDTO.setOrderId(order.getOrderId());
			orderDTO.setOrderNumber(order.getOrderNumber());
			orderDTO.setOrderStatus(order.getOrderStatus().toString());
			Product product = order.getProduct();
			ProductDTO productDTO = new ProductDTO();
			productDTO.setBrand(product.getBrand());
			productDTO.setCategory(product.getCategory());
			productDTO.setDescription(product.getDescription());
			productDTO.setDiscount(product.getDiscount());
			productDTO.setName(product.getName());
			productDTO.setPrice(product.getPrice());
			productDTO.setProductId(product.getProductId());
			productDTO.setQuantity(product.getQuantity());
			orderDTO.setProduct(productDTO);
			orderDTO.setQuantity(order.getQuantity());
			orderDTO.setTotalPrice(order.getTotalPrice());
			orderDTO.setPaymentThrough(order.getPaymentThrough());
			orderDTO.setDataOfDelivery(order.getDataOfDelivery());
			orderDTOs.add(orderDTO);
		}

		return orderDTOs;
	}
}

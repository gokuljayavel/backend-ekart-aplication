package com.infy.ekart.service;

import java.util.List;

import com.infy.ekart.dto.OrderDTO;
import com.infy.ekart.exception.EKartException;

public interface SellerOrderService {

	void modifyOrderStatus(Integer orderId, String orderStatus) throws EKartException;

	List<OrderDTO> viewOrders(String sellerEmailId) throws EKartException;
}

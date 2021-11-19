package com.infy.ekart.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.ekart.dto.OrderDTO;
import com.infy.ekart.dto.OrderStatus;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.service.SellerOrderService;

@CrossOrigin
@RestController
@RequestMapping(value = "/sellerOrder-api")
@Validated
public class SellerOrderAPI {

	@Autowired
	private SellerOrderService sellerOrderService;

	@Autowired
	private Environment environment;

	static Log logger = LogFactory.getLog(SellerOrderAPI.class);

	@GetMapping(value = "/sellers/{sellerEmailId:.+}")
	public ResponseEntity<List<OrderDTO>> viewOrders(
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+") @PathVariable String sellerEmailId)
			throws EKartException {

		logger.info("FETCHING ALL ORDERS MADE FOR PRODUCTS SOLD BY SELLER : " + sellerEmailId);

		List<OrderDTO> orderDTOs = sellerOrderService.viewOrders(sellerEmailId);

		return new ResponseEntity<>(orderDTOs, HttpStatus.OK);

	}

	@PutMapping(value = "/orders/{orderId}/orderStatus/{orderStatus}")
	public ResponseEntity<String> updateOrderStatus(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderStatus") String orderStatus) throws EKartException {

		sellerOrderService.modifyOrderStatus(orderId, orderStatus);
		String modificationSuccessMsg = environment.getProperty("SellerOrderAPI.ORDER_STATUS_UPDATE_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@GetMapping(value = "/orderStatus")
	public ResponseEntity<List<String>> getAllOrderStatus() {

		List<String> orderStatusList = new ArrayList<>();

		OrderStatus[] orderStatus = OrderStatus.values();
		for (OrderStatus orderStatus2 : orderStatus) {
			orderStatusList.add(orderStatus2.toString());
		}

		return new ResponseEntity<>(orderStatusList, HttpStatus.OK);

	}
}

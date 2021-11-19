package com.infy.ekart.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.infy.ekart.dto.OrderStatus;
import com.infy.ekart.dto.PaymentThrough;


@Entity
@Table(name="EK_ORDER")
public class Order {
	
	@Id
	@Column(name="ORDER_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer orderId;

	@Column(name="ORDER_NUMBER")
	private Integer orderNumber;
	
	@Column(name="DATE_OF_ORDER")
	private LocalDateTime dateOfOrder;
	
	@ManyToOne
	@JoinColumn(name="PRODUCT_ID")
	private Product product;
	
	@Column(name="QUANTITY")
	private Integer quantity;
	
	@Column(name="TOTAL_PRICE")
	private Double totalPrice;
	
	@Column(name="ADDRESS_ID")
	private Integer addressId;
	
	@Column(name="ORDER_STATUS")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(name="PAYMENT_THROUGH")
	@Enumerated(EnumType.STRING)
	private PaymentThrough paymentThrough;
	
	@Column(name="DATE_OF_DELIVERY")
	private LocalDateTime dataOfDelivery;
	
	@Column(name="CUSTOMER_EMAIL_ID")
	private String customerEmailId;
	
	public String getCustomerEmailId() {
		return customerEmailId;
	}

	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public LocalDateTime getDateOfOrder() {
		return dateOfOrder;
	}

	public void setDateOfOrder(LocalDateTime dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public PaymentThrough getPaymentThrough() {
		return paymentThrough;
	}

	public void setPaymentThrough(PaymentThrough paymentThrough) {
		this.paymentThrough = paymentThrough;
	}

	public LocalDateTime getDataOfDelivery() {
		return dataOfDelivery;
	}

	public void setDataOfDelivery(LocalDateTime dataOfDelivery) {
		this.dataOfDelivery = dataOfDelivery;
	}
	
}

package com.infy.ekart.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class CustomerDTO {
	
	@NotNull(message = "{email.absent}")
	@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}")
	private String emailId;
	@Pattern(regexp = "([A-Za-z])+(\\s[A-Za-z]+)*", message = "{customer.invalid.name}")
	private String name;
	@NotNull(message = "{password.absent}")
	@Pattern(regexp = ".*[A-Z]+.*", message = "{invalid.password.format.uppercase}")
	@Pattern(regexp = ".*[a-z]+.*", message = "{invalid.password.format.lowercase}")
	@Pattern(regexp = ".*[0-9]+.*", message = "{invalid.password.format.number}")
	@Pattern(regexp = ".*[^a-zA-Z-0-9].*", message = "{invalid.password.format.specialcharacter}")
	private String password;
	private String newPassword;
	@Size(max=10, min = 10, message = "{customer.invalid.phonenumber}")
	@Pattern(regexp = "[0-9]+", message = "{customer.invalid.phonenumber}")
	private String phoneNumber;
	private List<AddressDTO> addresses;
	private List<CardDTO> cards;
	private List<CustomerCartDTO> customerCarts;
	private List<OrderDTO> orders; 
	
	public List<OrderDTO> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderDTO> orders) {
		this.orders = orders;
	}
	private String errorMessage;
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public List<CardDTO> getCards() {
		return cards;
	}
	public void setCards(List<CardDTO> cards) {
		this.cards = cards;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public List<AddressDTO> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}
	public List<CustomerCartDTO> getCustomerCarts() {
		return customerCarts;
	}
	public void setCustomerCarts(List<CustomerCartDTO> customerCarts) {
		this.customerCarts = customerCarts;
	}
	
}

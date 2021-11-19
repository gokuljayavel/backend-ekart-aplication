package com.infy.ekart.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SellerDTO {

	@NotNull(message = "{email.absent}")
	@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}")
	private String emailId;
	@Pattern(regexp = "([A-Za-z])+(\\s[A-Za-z]+)*", message = "{seller.invalid.name}")
	private String name;
	@Pattern(regexp = ".*[A-Z]+.*", message = "{invalid.password.format.uppercase}")
	@Pattern(regexp = ".*[a-z]+.*", message = "{invalid.password.format.lowercase}")
	@Pattern(regexp = ".*[0-9]+.*", message = "{invalid.password.format.number}")
	@Pattern(regexp = ".*[^a-zA-Z-0-9].*", message = "{invalid.password.format.specialcharacter}")
	private String password;
	@Pattern(regexp = ".*[A-Z]+.*", message = "{seller.invalid.new.password.uppercase}")
	@Pattern(regexp = ".*[a-z]+.*", message = "{seller.invalid.new.password.lowercase}")
	@Pattern(regexp = ".*[0-9]+.*", message = "{seller.invalid.new.password.number}")
	@Pattern(regexp = ".*[^a-zA-Z-0-9].*", message = "{seller.invalid.new.password.specialcharacter}")
	private String newPassword;
	private String confirmNewPassword;
	@Pattern(regexp = "[0-9]+", message = "{seller.invalid.phonenumber}")
	@Size(min = 10, max = 10, message = "{seller.invalid.phonenumber}")
	private String phoneNumber;
	@Size(min = 10, message = "{seller.invalid.address}")
	private String address;
	private List<ProductDTO> products;
	private String errorMessage;
	
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<ProductDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}
	
	

}

package com.infy.ekart.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="EK_CUSTOMER")
public class Customer {
	@Id
	@Column(name="EMAIL_ID")
	private String emailId;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="PASSWORD")
	private String password;

	@Column(name="PHONE_NUMBER")
	private String phoneNumber;


	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="CUSTOMER_EMAIL_ID")
	private List<Address> address;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="CUSTOMER_EMAIL_ID")
	private List<CustomerCart> customerCarts;

	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="CUSTOMER_EMAIL_ID")
	private List<Order> orders;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="CUSTOMER_EMAIL_ID")
	private List<Card> cards;
	
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

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public List<CustomerCart> getCustomerCarts() {
		return customerCarts;
	}

	public void setCustomerCarts(List<CustomerCart> customerCarts) {
		this.customerCarts = customerCarts;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	public List<Card> getCards(){
		return cards;
	}
	
	public void setCards(List<Card> cards) {
		this.cards= cards;
	}
}

package com.infy.ekart.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="EK_CUSTOMER_CART")
public class CustomerCart {
	
	@Id
	@Column(name="CART_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer cartId;
	
	@OneToOne(cascade=CascadeType.DETACH)
	@JoinColumn(name="PRODUCT_ID")
	private Product product;
	
	@Column(name="QUANTITY")
	private Integer quantity;

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
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
	
	

}

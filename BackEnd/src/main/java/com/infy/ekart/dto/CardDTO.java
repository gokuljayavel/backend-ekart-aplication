package com.infy.ekart.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CardDTO {

	private Integer cardId;
	private String cardType;
	@Pattern(regexp="[0-9]{16}")
	private String cardNumber;
	private String cvv;
	private String expiryDate;
	@Pattern(regexp="[A-Za-z ]+")
	private String nameOnCard;
	@Email
	private String customerEmailId;
	
	
	public CardDTO(Integer cardId, String cardType, String cardNumber, String cvv, @NotBlank String expiryDate, String nameOnCard,
			String customerEmailId) {
		super();
		this.cardId = cardId;
		this.cardType = cardType;
		this.cardNumber = cardNumber;
		this.cvv = cvv;
		this.expiryDate = expiryDate;
		this.nameOnCard = nameOnCard;
		this.customerEmailId = customerEmailId;
	}
	
	public CardDTO() {}


	public Integer getCardId() {
		return cardId;
	}


	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public String getCardNumber() {
		return cardNumber;
	}


	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}


	public String getCvv() {
		return cvv;
	}


	public void setCvv(String cvv) {
		this.cvv = cvv;
	}


	public @NotBlank String getExpiryDate() {
		return expiryDate;
	}


	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}


	public String getNameOnCard() {
		return nameOnCard;
	}


	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}


	public String getCustomerEmailId() {
		return customerEmailId;
	}


	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}


	@Override
	public String toString() {
		return "CardDTO [cardId=" + cardId + ", cardType=" + cardType + ", cardNumber=" + cardNumber + ", cvv=" + cvv
				+ ", expiryDate=" + expiryDate + ", nameOnCard=" + nameOnCard + ", customerEmailId=" + customerEmailId
				+ "]\n";
	}
	
	
	
	
}

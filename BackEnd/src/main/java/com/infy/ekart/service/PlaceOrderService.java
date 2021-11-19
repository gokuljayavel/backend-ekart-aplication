package com.infy.ekart.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.infy.ekart.dto.CardDTO;
import com.infy.ekart.dto.OrderDTO;
import com.infy.ekart.exception.EKartException;

public interface PlaceOrderService {

	Integer addCard(String customerEmailId,CardDTO carddto) throws EKartException, NoSuchAlgorithmException;
	void deleteCard(String customerEmailId,Integer cardId) throws EKartException;
	List<CardDTO> showSavedCards(String customerEmailId) throws EKartException;
	Integer addOrder(String customerEmailId,OrderDTO orddto) throws EKartException;
	public boolean cardMatcher (Integer cardId,String cvv) throws NoSuchAlgorithmException;
	List<OrderDTO> showAllOrders(String customerEmailId) throws EKartException;
	List<OrderDTO> currentOrder(String customerEmailId, Integer onumber) throws EKartException;
	Integer getLatestOrderNumber();

}

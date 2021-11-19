package com.infy.ekart.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.ekart.dto.CardDTO;
import com.infy.ekart.dto.CardType;
import com.infy.ekart.dto.OrderDTO;
import com.infy.ekart.dto.OrderStatus;
import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Card;
import com.infy.ekart.entity.Customer;
import com.infy.ekart.entity.Order;
import com.infy.ekart.entity.Product;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.CardRepositry;
import com.infy.ekart.repository.CustomerRepository;
import com.infy.ekart.repository.OrderRepository;
import com.infy.ekart.utility.HashingUtility;

@Service(value = "placeOrderService")
@Transactional
public class PlaceOrderServiceImpl implements PlaceOrderService{
	

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CardRepositry cardrepo;
	
	@Autowired
	private OrderRepository ordrepo;
	
	public Integer addCard(String customerEmailId,CardDTO carddto) throws EKartException, NoSuchAlgorithmException{
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		
		List<Card> cards = customer.getCards();
		for(Card cc : cards) {
			if(cc.getCardNumber().equals(carddto.getCardNumber())){
				throw new EKartException("Card.CARD_PRESENT");
			}
		}
		Card c = new Card();
		c.setCardNumber(carddto.getCardNumber());
		c.setCardType(CardType.valueOf(carddto.getCardType()));
		c.setExpiryDate(carddto.getExpiryDate());
		c.setNameOnCard(carddto.getNameOnCard());
		c.setCustomerEmailId(carddto.getCustomerEmailId());
		//For card Hash
		c.setCvv(HashingUtility.getHashValue(carddto.getCvv()));
		Card ret = cardrepo.save(c);
		cards.add(c);
		customer.setCards(cards);
		customerRepository.save(customer);
		return ret.getCardId();
	}
	
	public void deleteCard(String customerEmailId,Integer cardId) throws EKartException{
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		
		List<Card> cards = customer.getCards();
		Optional<Card> opcard = cardrepo.findById(cardId);
		Card car = opcard.orElseThrow(() -> new EKartException("Card.CARD_NOT_FOUND"));

		cards.remove(car);
		customer.setCards(cards);
	}
	

	public List<CardDTO> showSavedCards(String customerEmailId) throws EKartException{
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		
		List<Card> cards = customer.getCards();
		List<CardDTO> cardDTOs = new ArrayList<>();
		 for (Card card : cards) {
			CardDTO cd = new CardDTO(card.getCardId(), card.getCardType().toString(), card.getCardNumber(), card.getCvv(), card.getExpiryDate(), card.getNameOnCard(), card.getCustomerEmailId());
			cardDTOs.add(cd);
		}
		 return cardDTOs;
		
	}
	
	public boolean cardMatcher (Integer cardId,String cvv) throws NoSuchAlgorithmException {
		return HashingUtility.getHashValue(cvv).equals(cardrepo.findById(cardId).get().getCvv());
	}
	
	public Integer addOrder(String customerEmailId,OrderDTO orddto) throws EKartException{
		
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		
		List<Order> o = customer.getOrders();
		Order ord = new Order();
		ord.setAddressId(orddto.getAddressId());
		ord.setDataOfDelivery(orddto.getDataOfDelivery());
		ord.setDateOfOrder(orddto.getDateOfOrder());
		ord.setOrderNumber(orddto.getOrderNumber());
		ord.setOrderStatus(OrderStatus.valueOf(orddto.getOrderStatus()));
		ord.setQuantity(orddto.getQuantity());
		ord.setTotalPrice(orddto.getTotalPrice());
		ord.setCustomerEmailId(orddto.getCustomerEmailId());
		Product pro = new Product();
		ProductDTO p = orddto.getProduct();
		pro.setBrand(p.getBrand());
		pro.setCategory(p.getCategory());
		pro.setDescription(p.getDescription());
		pro.setDiscount(p.getDiscount());
		pro.setName(p.getName());
		pro.setPrice(p.getPrice());
		pro.setProductId(p.getProductId());
		pro.setQuantity(p.getQuantity());
		ord.setProduct(pro);
		ord.setPaymentThrough(orddto.getPaymentThrough());
		o.add(ord);
		customer.setOrders(o);
		customerRepository.save(customer);
		//Order ret = ordrepo.save(ord);
		//return ret.getOrderId();
		return 1;
	}
	
	public List<OrderDTO> showAllOrders(String customerEmailId) throws EKartException{
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		List<Order> orders = customer.getOrders();
		List<OrderDTO> orderDTOs = new ArrayList<>();
		for (Order order : orders) {
			OrderDTO od = new OrderDTO();
			od.setAddressId(order.getAddressId());
			od.setCustomerEmailId(order.getCustomerEmailId());
			od.setDataOfDelivery(order.getDataOfDelivery());
			od.setDateOfOrder(order.getDateOfOrder());
			od.setOrderId(order.getOrderId());
			od.setOrderNumber(order.getOrderNumber());
			od.setOrderStatus(order.getOrderStatus().toString());
			od.setQuantity(order.getQuantity());
			od.setPaymentThrough(order.getPaymentThrough());
			od.setTotalPrice(order.getTotalPrice());
			ProductDTO pro = new ProductDTO();
			pro.setBrand(order.getProduct().getBrand());
			pro.setCategory(order.getProduct().getCategory());
			pro.setDescription(order.getProduct().getDescription());
			pro.setDiscount(order.getProduct().getDiscount());
			pro.setName(order.getProduct().getName());
			pro.setPrice(order.getProduct().getPrice());
			pro.setProductId(order.getProduct().getProductId());
			pro.setQuantity(order.getProduct().getQuantity());
			od.setProduct(pro);
			orderDTOs.add(od);
		}
		return orderDTOs;
	}
	
	public List<OrderDTO> currentOrder(String customerEmailId,Integer onumber) throws EKartException{
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer
				.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		List<Order> orders = customer.getOrders();
		List<OrderDTO> orderDTOs = new ArrayList<>();
		for (Order order : orders) {
			if(order.getOrderNumber().equals(onumber)) {
				OrderDTO od = new OrderDTO();
				od.setAddressId(order.getAddressId());
				od.setCustomerEmailId(order.getCustomerEmailId());
				od.setDataOfDelivery(order.getDataOfDelivery());
				od.setDateOfOrder(order.getDateOfOrder());
				od.setOrderId(order.getOrderId());
				od.setOrderNumber(order.getOrderNumber());
				od.setOrderStatus(order.getOrderStatus().toString());
				od.setQuantity(order.getQuantity());
				od.setPaymentThrough(order.getPaymentThrough());
				od.setTotalPrice(order.getTotalPrice());
				ProductDTO pro = new ProductDTO();
				pro.setBrand(order.getProduct().getBrand());
				pro.setCategory(order.getProduct().getCategory());
				pro.setDescription(order.getProduct().getDescription());
				pro.setDiscount(order.getProduct().getDiscount());
				pro.setName(order.getProduct().getName());
				pro.setPrice(order.getProduct().getPrice());
				pro.setProductId(order.getProduct().getProductId());
				pro.setQuantity(order.getProduct().getQuantity());
				od.setProduct(pro);
				orderDTOs.add(od);

			}			
		}
		return orderDTOs;
	}
	
	public Integer getLatestOrderNumber() {
		return ordrepo.onumber()+1;
	}
	
}

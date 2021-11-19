package com.infy.ekart.service.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//import com.infy.ekart.dto.CustomerCartDTO;
import com.infy.ekart.dto.CardDTO;
import com.infy.ekart.dto.CardType;
import com.infy.ekart.dto.CustomerCartDTO;
import com.infy.ekart.dto.OrderDTO;
import com.infy.ekart.dto.OrderStatus;
import com.infy.ekart.dto.PaymentThrough;
import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Card;
import com.infy.ekart.entity.Product;
import com.infy.ekart.entity.Customer;
import com.infy.ekart.entity.CustomerCart;
import com.infy.ekart.entity.Order;
import com.infy.ekart.entity.Product;
import com.infy.ekart.exception.EKartException;

import com.infy.ekart.repository.CardRepositry;
import com.infy.ekart.repository.CustomerRepository;
import com.infy.ekart.repository.OrderRepository;
import com.infy.ekart.service.PlaceOrderService;
import com.infy.ekart.service.PlaceOrderServiceImpl;
import com.infy.ekart.utility.HashingUtility;

@SpringBootTest
class PlaceOrderServiceTest {
	
	@Mock
	CustomerRepository customerRepository;

	@Mock
	private CardRepositry cardRepository;

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private PlaceOrderService pService = new PlaceOrderServiceImpl();

	// Valid test for showSavedCards
	@Test
	 void showSavedCardsValidTest() throws EKartException {
		List<Card> cards = new ArrayList<Card>();
		Card c1 = new Card();
		c1.setCardId(1);
		c1.setCardNumber("1231231234561230");
		c1.setCardType(CardType.DEBIT_CARD);
		c1.setCustomerEmailId("tom@infosys.com");
		c1.setCvv("122");
		c1.setExpiryDate("12/02/2020");
		c1.setNameOnCard("tom");
		cards.add(c1);
//		Card c2 = new Card();
//		c2.setCardId(2);
//		cards.add(c2);
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setCards(cards);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);
		List<CardDTO> c = pService.showSavedCards(customer.getEmailId());
		//Mockito.when(cardRepository.findAll()).thenReturn(cards);
		Assertions.assertEquals(c.size(), cards.size());

	}
	
	//invalid test for showSavedCards
	@Test
	void showSavedCardsInValidTest2() throws EKartException {

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(Optional.empty());
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> pService.showSavedCards("tom@infosys.com"));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}
	
	// valid test for addCard
	@Test
	void addCardValidTest1() throws EKartException {

		Card c1 = new Card();
		c1.setCardId(1);
		c1.setCardNumber("1231231234561230");
		c1.setCardType(CardType.DEBIT_CARD);
		c1.setCustomerEmailId("tom@infosys.com");
		c1.setCvv("122");
		c1.setExpiryDate("12/02/2020");
		c1.setNameOnCard("tom");
		//Optional<Card> optionalCard = Optional.of(c1);

		List<Card> cards = new ArrayList<Card>();
		cards.add(c1);

		CardDTO cd = new CardDTO();
		cd.setCardId(2);
		cd.setCardNumber("1231231234561255");
		cd.setCardType("DEBIT_CARD");
		cd.setCustomerEmailId("tom@infosys.com");
		cd.setCvv("122");
		cd.setExpiryDate("12/02/2020");
		cd.setNameOnCard("tom");
		
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setCards(cards);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);
		Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(c1);
		//Mockito.when(cardRepository.findById(1)).thenReturn(optionalCard);

		Assertions.assertDoesNotThrow(() -> pService.addCard("tom@infosys.com", cd));
	}

	
	//CARD_PRESENT invalid test for card
	
	@Test
	void addCardInValidTest1() throws EKartException {

		Card c1 = new Card();
		c1.setCardId(1);
		c1.setCardNumber("1231231234561230");
		c1.setCardType(CardType.DEBIT_CARD);
		c1.setCustomerEmailId("tom@infosys.com");
		c1.setCvv("122");
		c1.setExpiryDate("12/02/2020");
		c1.setNameOnCard("tom");
		Optional<Card> optionalCard = Optional.of(c1);

		List<Card> cards = new ArrayList<Card>();
		cards.add(c1);

		CardDTO cd = new CardDTO();
		cd.setCardId(1);
		cd.setCardNumber("1231231234561230");
		cd.setCardType("DEBIT_CARD");
		cd.setCustomerEmailId("tom@infosys.com");
		cd.setCvv("122");
		cd.setExpiryDate("12/02/2020");
		cd.setNameOnCard("tom");
		
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setCards(cards);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);
		
		Mockito.when(cardRepository.findById(1)).thenReturn(optionalCard);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> pService.addCard("tom@infosys.com", cd));
		Assertions.assertEquals("Card.CARD_PRESENT", exception.getMessage());

	}
	
	//invalid test for addCard

	@Test
	void addCardInValidTest2() throws EKartException {

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(Optional.empty());
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> pService.addCard("tom@infosys.com", null));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}
	
	
	//valid test for showAllOrders
	@Test
	void showAllOrdersValidTest1() throws EKartException{
		
		List<Order> os = new ArrayList<Order>();
		Order c1 = new Order();
		c1.setOrderId(1);
		c1.setAddressId(1);
		c1.setDataOfDelivery(LocalDateTime.now());
		c1.setDateOfOrder(null);
		c1.setOrderNumber(122);
		c1.setOrderStatus(OrderStatus.DELIVERED);
		c1.setPaymentThrough(PaymentThrough.CASH_ON_DELIVERY);
		Product p =new Product();
		p.setBrand("bb");
		p.setCategory("cc");
		p.setDescription("dd");
		p.setDiscount(5.2d);
		p.setName("nn");
		p.setPrice(550.0d);
		p.setProductId(1220);
		p.setQuantity(3);
		c1.setProduct(p);
		c1.setQuantity(2);
		c1.setTotalPrice(12356.0d);
		c1.setCustomerEmailId("tom@infosys.com");
		
		os.add(c1);
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setOrders(os);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);
		List<OrderDTO> c = pService.showAllOrders(customer.getEmailId());
		//Mockito.when(cardRepository.findAll()).thenReturn(cards);
		Assertions.assertEquals(c.size(), os.size());

		
	}
	
	//invalid test for showAllOrders
	@Test
	void showAllOrdersInValidTest2() throws EKartException {

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(Optional.empty());
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> pService.showAllOrders("tom@infosys.com"));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}
	
	
	//valid test for add_order
	
	@Test
	void addOrderValidTest1() throws EKartException{
		
		Product p =new Product();
		p.setBrand("bb");
		p.setCategory("cc");
		p.setDescription("dd");
		p.setDiscount(5.2d);
		p.setName("nn");
		p.setPrice(550.0d);
		p.setProductId(1220);
		p.setQuantity(3);
		
		Order o = new Order();
		o.setCustomerEmailId("tom@infosys.com");
		o.setOrderId(1);
		o.setProduct(p);
		o.setAddressId(12);
		o.setDataOfDelivery(null);
		o.setDateOfOrder(null);
		o.setOrderStatus(OrderStatus.CANCELLED);
		o.setQuantity(5);
		o.setTotalPrice(123654.0d);
		List<Order> os = new ArrayList<>();
		os.add(o);
		
		ProductDTO pd =new ProductDTO();
		pd.setBrand("bb");
		pd.setCategory("cc");
		pd.setDescription("dd");
		pd.setDiscount(5.2d);
		pd.setName("nn");
		pd.setPrice(550.0d);
		pd.setProductId(1220);
		pd.setQuantity(3);
		
		OrderDTO od = new OrderDTO();
		od.setOrderId(2);
		od.setCustomerEmailId("tom@infosys.com");
		od.setProduct(pd);
		od.setAddressId(12);
		od.setDataOfDelivery(null);
		od.setDateOfOrder(null);
		od.setOrderStatus("CANCELLED");
		od.setQuantity(5);
		od.setTotalPrice(123654.0d);
		
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setOrders(os);
		Optional<Customer> optionalCustomer = Optional.of(customer);
		
		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);
		Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(o);
		//Mockito.when(cardRepository.findById(1)).thenReturn(optionalCard);

		Assertions.assertDoesNotThrow(() -> pService.addOrder("tom@infosys.com", od));
		
	}
	
	//invalid test for addOrder
	@Test
	void addOrderInValidTest2() throws EKartException {

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(Optional.empty());
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> pService.addOrder("tom@infosys.com", null));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}
	
	
	//valid test for currentOrder
	@Test
	void currentOrderValidTest1() throws EKartException{
		List<Order> os = new ArrayList<Order>();
		Order c1 = new Order();
		c1.setOrderId(1);
		c1.setAddressId(1);
		c1.setDataOfDelivery(LocalDateTime.now());
		c1.setDateOfOrder(null);
		c1.setOrderNumber(122);
		c1.setOrderStatus(OrderStatus.DELIVERED);
		c1.setPaymentThrough(PaymentThrough.CASH_ON_DELIVERY);
		Product p =new Product();
		p.setBrand("bb");
		p.setCategory("cc");
		p.setDescription("dd");
		p.setDiscount(5.2d);
		p.setName("nn");
		p.setPrice(550.0d);
		p.setProductId(1220);
		p.setQuantity(3);
		c1.setProduct(p);
		c1.setQuantity(2);
		c1.setTotalPrice(12356.0d);
		c1.setCustomerEmailId("tom@infosys.com");
		
		os.add(c1);
		Customer customer = new Customer();
		customer.setEmailId("tom@infosys.com");
		customer.setOrders(os);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(optionalCustomer);
		List<OrderDTO> c = pService.currentOrder(customer.getEmailId(),c1.getOrderNumber());
		//Mockito.when(cardRepository.findAll()).thenReturn(cards);
		Assertions.assertEquals(c.size(), os.size());
		Assertions.assertEquals(c.get(0).getOrderNumber(), os.get(0).getOrderNumber());
		
	}
	
	//invalid test for currentOrder
	@Test
	void currentOrderInValidTest2() throws EKartException {

		Mockito.when(customerRepository.findById("tom@infosys.com")).thenReturn(Optional.empty());
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> pService.currentOrder("tom@infosys.com", null));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}
	
}

package com.infy.ekart.api;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.spi.scripting.ScriptEvaluatorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.ekart.dto.AddressDTO;
import com.infy.ekart.dto.CardDTO;
import com.infy.ekart.dto.CustomerDTO;
import com.infy.ekart.dto.OrderDTO;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.service.CustomerService;
import com.infy.ekart.service.PlaceOrderService;
import com.infy.ekart.service.PlaceOrderServiceImpl;

@CrossOrigin
@RestController
@RequestMapping(value = "/customer-api")
@Validated
public class CustomerAPI {

	@Autowired
	private CustomerService customerService;
	@Autowired
	private PlaceOrderService placeOrderService;

	@Autowired
	private Environment environment;

	static Log logger = LogFactory.getLog(CustomerAPI.class);

	@PostMapping(value = "/login")
	public ResponseEntity<CustomerDTO> authenticateCustomer(@Valid @RequestBody CustomerDTO customerDTO)
			throws EKartException {

		logger.info("CUSTOMER TRYING TO LOGIN, VALIDATING CREDENTIALS. CUSTOMER EMAIL ID: " + customerDTO.getEmailId());
		CustomerDTO customerDTOFromDB = customerService.authenticateCustomer(customerDTO.getEmailId(),
				customerDTO.getPassword());
		logger.info("CUSTOMER LOGIN SUCCESS, CUSTOMER EMAIL : " + customerDTOFromDB.getEmailId());
		return new ResponseEntity<>(customerDTOFromDB, HttpStatus.OK);
	}

	@PostMapping(value = "/customers")
	public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws EKartException {

		logger.info("CUSTOMER TRYING TO REGISTER. CUSTOMER EMAIL ID: " + customerDTO.getEmailId());
		String registeredWithEmailID = customerService.registerNewCustomer(customerDTO);
		registeredWithEmailID = environment.getProperty("CustomerAPI.CUSTOMER_REGISTRATION_SUCCESS")
				+ registeredWithEmailID;
		return new ResponseEntity<>(registeredWithEmailID, HttpStatus.OK);
	}

	@PutMapping(value = "/customers")
	public ResponseEntity<String> updateCustomerProfile(@Valid @RequestBody CustomerDTO customerDTO)
			throws EKartException {
		customerService.updateProfile(customerDTO);
		String modificationSuccessMsg = environment.getProperty("CustomerAPI.CUSTOMER_DETAILS_UPDATION_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@PutMapping(value = "/customers/password")
	public ResponseEntity<String> changePassword(@Valid @RequestBody CustomerDTO customerDTO) throws EKartException {

		customerService.changePassword(customerDTO.getEmailId(), customerDTO.getPassword(),
				customerDTO.getNewPassword());
		String modificationSuccessMsg = environment.getProperty("CustomerAPI.CUSTOMER_PASSWORD_CHANGE_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@PostMapping(value = "/customers/{customerEmailId:.+}/addresses")
	public ResponseEntity<String> addNewShippingAddress(@Valid @RequestBody AddressDTO addressDTO,
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {
		int addressId;
		addressId = customerService.addShippingAddress(customerEmailId, addressDTO);
		String message = environment.getProperty("CustomerAPI.NEW_SHIPPING_ADDRESS_ADDED_SUCCESS");
		String toReturn = message + addressId;
		toReturn = toReturn.trim();
		return new ResponseEntity<>(toReturn, HttpStatus.OK);

	}

	@PutMapping(value = "/addresses")
	public ResponseEntity<String> updateShippingAddress(@Valid @RequestBody AddressDTO addressDTO)
			throws EKartException {

		customerService.updateShippingAddress(addressDTO);
		String modificationSuccessMsg = environment.getProperty("CustomerAPI.UPDATE_ADDRESS_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@DeleteMapping(value = "/customers/{customerEmailId:.+}/address/{addressId}")
	public ResponseEntity<String> deleteShippingAddress(@PathVariable("addressId") Integer addressId,
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {

		customerService.deleteShippingAddress(customerEmailId, addressId);
		String modificationSuccessMsg = environment.getProperty("CustomerAPI.CUSTOMER_ADDRESS_DELETED_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@GetMapping(value = "/addresses/{addressId}")
	public ResponseEntity<AddressDTO> getShippingAddress(@PathVariable("addressId") Integer addressId)
			throws EKartException {

		AddressDTO addressDTO = customerService.getShippingAddress(addressId);
		return new ResponseEntity<>(addressDTO, HttpStatus.OK);
	}
	
	@PostMapping(value = "/customers/{customerEmailId:.+}/cards")
	public ResponseEntity<String> addCard(@Valid @RequestBody CardDTO cardDTO, @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException, NoSuchAlgorithmException{
		int cardId;
		cardId = placeOrderService.addCard(customerEmailId, cardDTO);
		String message = environment.getProperty("CustomerAPI.NEW_CARD_ADDED_SUCCESS");
		String toReturn = message + cardId;
		toReturn = toReturn.trim();
		return new ResponseEntity<>(toReturn, HttpStatus.OK);
		
	}
	@DeleteMapping(value = "/customers/{customerEmailId:.+}/card/{cardId}")
	public ResponseEntity<String> deleteCard(@PathVariable("cardId") Integer cardId,
			@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {

		placeOrderService.deleteCard(customerEmailId, cardId);
		String modificationSuccessMsg = environment.getProperty("CustomerAPI.CUSTOMER_CARD_DELETED_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);
}
	@PostMapping(value = "/customers/{customerEmailId:.+}/orders")
	public ResponseEntity<String> addorder(@RequestBody OrderDTO orddto, @Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException{
		int ordid = placeOrderService.addOrder(customerEmailId, orddto);
		String mess = environment.getProperty("CustomerAPI.ORDER_CREATED_SUCCESS");
		String toReturn = mess;
		toReturn = toReturn.trim();
		return new ResponseEntity<>(toReturn, HttpStatus.OK);
	}
	
	@GetMapping(value="/customers/{customerEmailId:.+}/savedcards")
	public ResponseEntity<List<CardDTO>> showCards(@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {
		
		return new ResponseEntity<List<CardDTO>>(placeOrderService.showSavedCards(customerEmailId),HttpStatus.OK);}
	
	@GetMapping(value = "/customer/card/{cardId}/{cvv}")
	public ResponseEntity<Boolean> checkCard(@PathVariable("cardId") Integer cardId,@PathVariable("cvv") String cvv) throws NoSuchAlgorithmException{
		return new ResponseEntity<Boolean>(placeOrderService.cardMatcher(cardId, cvv),HttpStatus.OK);
	}
	
	@PostMapping(value = "/customers/address/onlyaddress")
	public ResponseEntity<String> addOnlyShippingAddress(@Valid @RequestBody AddressDTO addressDTO)
			throws EKartException {
		int addressId;
		addressId = customerService.addOnlyShippingAddress(addressDTO);
		String message = environment.getProperty("CustomerAPI.ONLY_ADDRESS_ADDED_SUCCESS");
		String toReturn = message + addressId;
		toReturn = toReturn.trim();
		return new ResponseEntity<>(toReturn, HttpStatus.OK);

	}
	
	@GetMapping(value="/customers/{customerEmailId:.+}/showorders")
	public ResponseEntity<List<OrderDTO>> showOrders(@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+", message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId)
			throws EKartException {
		
		return new ResponseEntity<List<OrderDTO>>(placeOrderService.showAllOrders(customerEmailId),HttpStatus.OK);}
	
	@GetMapping(value="/customers/{customerEmailId:.+}/{onumber}")
	public ResponseEntity<List<OrderDTO>> currentOrder(@Pattern(regexp = "[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+",
	message = "{invalid.email.format}") @PathVariable("customerEmailId") String customerEmailId,
			@PathVariable("onumber") Integer on)
			throws EKartException {
		
		return new ResponseEntity<List<OrderDTO>>(placeOrderService.currentOrder(customerEmailId,on),HttpStatus.OK);}
	
	
	@GetMapping(value="/customers/onumber")
	public ResponseEntity<Integer>getOrderNumber(){
		return new ResponseEntity<Integer>(placeOrderService.getLatestOrderNumber(),HttpStatus.OK);
	}
	
	}
	


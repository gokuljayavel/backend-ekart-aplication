package com.infy.ekart.service.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.ekart.dto.AddressDTO;
import com.infy.ekart.dto.CustomerDTO;
import com.infy.ekart.entity.Address;
import com.infy.ekart.entity.Customer;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.AddressRepository;
import com.infy.ekart.repository.CustomerRepository;
import com.infy.ekart.service.CustomerService;
import com.infy.ekart.service.CustomerServiceImpl;

@SpringBootTest
class CustomerServiceTest {
	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AddressRepository addressRepository;

	@InjectMocks
	private CustomerService customerService = new CustomerServiceImpl();

	//Testing authenticateCustomer
	@Test
	void authenticateCustomerValidTest() throws EKartException {

		Customer customer = new Customer();
		String emailIdFromDB = "brian@infosys.com";
		String passwordFromDB = "Brian@123";
		customer.setEmailId(emailIdFromDB);
		customer.setPassword(passwordFromDB);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		String emailId = "brian@infosys.com";
		String password = "Brian@123";

		Mockito.when(customerRepository.findById(emailId.toLowerCase())).thenReturn(optionalCustomer);
		Assertions.assertEquals(emailIdFromDB, customerService.authenticateCustomer(emailId, password).getEmailId());

	}

	//testing for CUSTOMER_NOT_FOUND exception
	@Test
	void authenticateCustomerInvalidTest1() throws EKartException {
		Optional<Customer> optionalCustomer = Optional.empty();

		String emailId = "brian@infosys.com";
		String password = "Brian@123";

		Mockito.when(customerRepository.findById(emailId.toLowerCase())).thenReturn(optionalCustomer);
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.authenticateCustomer(emailId, password));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}

	//testing for INVALID_CREDENTIALS exception
	@Test
	void authenticateCustomerInvalidTest2() throws EKartException {
		Customer customer = new Customer();
		String emailIdFromDB = "brian@infosys.com";
		String passwordFromDB = "Brian@123";
		customer.setEmailId(emailIdFromDB);
		customer.setPassword(passwordFromDB);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		String emailId = "brian@infosys.com";
		String password = "Brian@321";

		Mockito.when(customerRepository.findById(emailId.toLowerCase())).thenReturn(optionalCustomer);
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.authenticateCustomer(emailId, password));
		Assertions.assertEquals("CustomerService.INVALID_CREDENTIALS", exception.getMessage());

	}

	//Testing registerNewCustomer
	@Test
	void registerNewCustomerValidTest() throws EKartException {
		Optional<Customer> optionalCustomer = Optional.empty();

		List<Customer> customerByPhoneNumber = new ArrayList<>();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("brian@infosys.com");
		customerDTO.setName("Brian");
		customerDTO.setPassword("Brian@123");
		customerDTO.setPhoneNumber("8103436348");

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);
		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(customerByPhoneNumber);

		Assertions.assertEquals("brian@infosys.com", customerService.registerNewCustomer(customerDTO));

	}

	// testing for EMAIL_ID_ALREADY_IN_USE exception
	@Test
	void registerNewCustomerInvalidTest1() throws EKartException {
		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		Optional<Customer> optionalCustomer = Optional.of(customer);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("brian@infosys.com");
		customerDTO.setName("Brian");
		customerDTO.setPassword("Brian@123");
		customerDTO.setPhoneNumber("8103436348");

		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.registerNewCustomer(customerDTO));
		Assertions.assertEquals("CustomerService.EMAIL_ID_ALREADY_IN_USE", exception.getMessage());

	}

	// testing for PHONE_NUMBER_ALREADY_IN_USE exception
	@Test
	void registerNewCustomerInalidTest2() throws EKartException {
		Optional<Customer> optionalCustomer = Optional.empty();

		List<Customer> customerByPhoneNumber = new ArrayList<>();
		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		customerByPhoneNumber.add(customer);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("brian@infosys.com");
		customerDTO.setName("Brian");
		customerDTO.setPassword("Brian@123");
		customerDTO.setPhoneNumber("8103436348");
		
		
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);

		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(customerByPhoneNumber);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.registerNewCustomer(customerDTO));
		Assertions.assertEquals("CustomerService.PHONE_NUMBER_ALREADY_IN_USE", exception.getMessage());

	}

	//Testing updateProfile
	@Test
	void updateProfileValidTest1() throws EKartException {
		List<Customer> customerByPhoneNumber = new ArrayList<>();
		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		customer.setPhoneNumber("8103436348");
		customerByPhoneNumber.add(customer);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("brian@infosys.com");
		customerDTO.setPhoneNumber("8103436348");
		customerDTO.setName("Brian");

		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(customerByPhoneNumber);

		Optional<Customer> optionalCustomer = Optional.of(customer);
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);
		Assertions.assertDoesNotThrow(() -> customerService.updateProfile(customerDTO));
	}

	//Testing updateProfile
	@Test
	void updateProfileValidTest2() throws EKartException {
		List<Customer> customerByPhoneNumber = new ArrayList<>();

		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		customer.setPhoneNumber("8103436348");

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("Brian@infosys.com");
		customerDTO.setPhoneNumber("8103436348");
		customerDTO.setName("Brian");

		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(customerByPhoneNumber);

		Optional<Customer> optionalCustomer = Optional.ofNullable(customer);
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);
		Assertions.assertDoesNotThrow(() -> customerService.updateProfile(customerDTO));

	}

	// testing for PHONE_NUMBER_ALREADY_IN_USE exception
	@Test
	void updateProfileInvalidTest1() throws EKartException {
		List<Customer> customerByPhoneNumber = new ArrayList<>();
		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		customer.setPhoneNumber("8103436348");
		customerByPhoneNumber.add(customer);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("Brian90@infosys.com");
		customerDTO.setPhoneNumber("8103436348");
		customerDTO.setName("Brian");

		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(customerByPhoneNumber);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.updateProfile(customerDTO));
		Assertions.assertEquals("CustomerService.PHONE_NUMBER_ALREADY_IN_USE", exception.getMessage());

	}

	// testing for CUSTOMER_NOT_FOUND exception
	@Test
	void updateProfileInvalidTest2() throws EKartException {
		List<Customer> customerByPhoneNumber = new ArrayList<>();
		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		customer.setPhoneNumber("8103436348");
		customerByPhoneNumber.add(customer);

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("Brian@infosys.com");
		customerDTO.setPhoneNumber("8103436348");
		customerDTO.setName("Brian");

		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(customerByPhoneNumber);

		Optional<Customer> optionalCustomer = Optional.empty();
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.updateProfile(customerDTO));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}

	// testing for CUSTOMER_NOT_FOUND exception
	@Test
	void updateProfileInvalidTest3() throws EKartException {
		List<Customer> customerByPhoneNumber = new ArrayList<>();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setEmailId("Brian@infosys.com");
		customerDTO.setPhoneNumber("8103436348");
		customerDTO.setName("Brian");

		Mockito.when(customerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(customerByPhoneNumber);

		Optional<Customer> optionalCustomer = Optional.empty();
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.updateProfile(customerDTO));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}

	//Testing changePassword
	@Test
	void changePasswordValidTest() throws EKartException {
		String emailId = "brian@infosys.com";
		String currentPassword = "Brian@123";
		String newPassword = "Brian@987";

		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		customer.setPassword(currentPassword);
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById(emailId.toLowerCase())).thenReturn(optionalCustomer);

		Assertions.assertDoesNotThrow(() -> customerService.changePassword(emailId, currentPassword, newPassword));

	}

	// testing for CUSTOMER_NOT_FOUND exception
	@Test
	void changePasswordInalidTest1() throws EKartException {
		String emailId = "brian@infosys.com";
		String currentPassword = "Brian@123";
		String newPassword = "Brian@321";

		Optional<Customer> optionalCustomer = Optional.empty();

		Mockito.when(customerRepository.findById(emailId.toLowerCase())).thenReturn(optionalCustomer);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.changePassword(emailId, currentPassword, newPassword));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());
	}

	// testing for INVALID_CURRENT_PASSWORD exception
	@Test
	void changePasswordInalidTest2() throws EKartException {
		String emailId = "brian@infosys.com";
		String currentPassword = "Brian@123";
		String newPassword = "Brian@321";

		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		customer.setPassword("brian@12");
		Optional<Customer> optionalCustomer = Optional.of(customer);
		;

		Mockito.when(customerRepository.findById(emailId.toLowerCase())).thenReturn(optionalCustomer);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.changePassword(emailId, currentPassword, newPassword));
		Assertions.assertEquals("CustomerService.INVALID_CURRENT_PASSWORD", exception.getMessage());
	}

	// testing for OLD_PASSWORD_NEW_PASSWORD_SAME exception
	@Test
	void changePasswordInalidTest3() throws EKartException {
		String emailId = "brian@infosys.com";
		String currentPassword = "Brian@123";
		String newPassword = "Brian@123";

		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		customer.setPassword("Brian@123");
		Optional<Customer> optionalCustomer = Optional.of(customer);
		;

		Mockito.when(customerRepository.findById(emailId.toLowerCase())).thenReturn(optionalCustomer);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.changePassword(emailId, currentPassword, newPassword));
		Assertions.assertEquals("CustomerService.OLD_PASSWORD_NEW_PASSWORD_SAME", exception.getMessage());
	}

	//Testing addShippingAddress
	@Test
	void addShippingAddressValidTest() throws EKartException {
		String emailId = "brian@infosys.com";

		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		customer.setAddress(new ArrayList<>());
		Optional<Customer> optionalCustomer = Optional.of(customer);

		Mockito.when(customerRepository.findById(emailId.toLowerCase())).thenReturn(optionalCustomer);
		
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setAddressId(1);
		Address address = new Address();
		address.setAddressId(addressDTO.getAddressId());
		List<Address> newCustomerAddress = new ArrayList<>();
		newCustomerAddress.add(address);
		Customer newCustomer = new Customer();
		newCustomer.setEmailId("brian@infosys.com");
		newCustomer.setAddress(newCustomerAddress);
		
		Mockito.when(customerRepository.save(Mockito.any())).thenReturn(newCustomer);
		Integer addressId = customerService.addShippingAddress(emailId, addressDTO);
		Assertions.assertEquals(addressDTO.getAddressId(), addressId);

	}

	// testing for CUSTOMER_NOT_FOUND exception
	@Test
	void addShippingAddressInvalidTest1() throws EKartException {
		String emailId = "brian@infosys.com";

		AddressDTO addressDTO = new AddressDTO();

		Optional<Customer> optionalCustomer = Optional.empty();
		;

		Mockito.when(customerRepository.findById(emailId.toLowerCase())).thenReturn(optionalCustomer);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.addShippingAddress(emailId, addressDTO));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}

	//Testing updateShippingAddress
	@Test
	void updateShippingAddressValidTest() throws EKartException {

		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setAddressId(121);

		Address address = new Address();
		address.setAddressId(121);
		Optional<Address> optionalAddress = Optional.of(address);

		Mockito.when(addressRepository.findById(Mockito.any(Integer.class))).thenReturn(optionalAddress);
		Assertions.assertDoesNotThrow(() -> customerService.updateShippingAddress(addressDTO));

	}

	// testing for ADDRESS_NOT_FOUND exception
	@Test
	void updateShippingAddressInvalidTest() throws EKartException {

		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setAddressId(121);

		Optional<Address> optionalAddress = Optional.empty();
		
		Mockito.when(addressRepository.findById(Mockito.any(Integer.class))).thenReturn(optionalAddress);
		//Assertions.assertDoesNotThrow(() -> customerService.updateShippingAddress(addressDTO));
		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.updateShippingAddress(addressDTO));
		Assertions.assertEquals("Service.ADDRESS_NOT_FOUND", exception.getMessage());

	}

	//Testing deleteShippingAddress
	@Test
	void deleteShippingAddressValidTest() throws EKartException {
		String emailId = "brian@infosys.com";
		Integer addressId = 123;

		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		List<Address> customerAddress = new ArrayList<>();
		Address custAddress = new Address();
		custAddress.setAddressId(121);
		customerAddress.add(custAddress);
		customer.setAddress(customerAddress);
		Optional<Customer> optionalCustomer = Optional.of(customer);
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);

		Address address = new Address();
		address.setAddressId(121);
		Optional<Address> optionalAddress = Optional.of(address);
		Mockito.when(addressRepository.findById(Mockito.any(Integer.class))).thenReturn(optionalAddress);

		Assertions.assertDoesNotThrow(() -> customerService.deleteShippingAddress(emailId, addressId));

	}

	// testing for CUSTOMER_NOT_FOUND exception
	@Test
	void deleteShippingAddressInvalidTest1() throws EKartException {
		String emailId = "brian@infosys.com";
		Integer addressId = 123;

		Optional<Customer> optionalCustomer = Optional.empty();
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.deleteShippingAddress(emailId, addressId));
		Assertions.assertEquals("Service.CUSTOMER_NOT_FOUND", exception.getMessage());

	}

	// testing for ADDRESS_NOT_FOUND exception
	@Test
	void deleteShippingAddressInvalidTest2() throws EKartException {
		String emailId = "brian@infosys.com";
		Integer addressId = 123;

		Customer customer = new Customer();
		customer.setEmailId("brian@infosys.com");
		List<Address> customerAddress = new ArrayList<>();
		customer.setAddress(customerAddress);
		Optional<Customer> optionalCustomer = Optional.of(customer);
		Mockito.when(customerRepository.findById(Mockito.anyString())).thenReturn(optionalCustomer);

		Optional<Address> optionalAddress = Optional.empty();
		Mockito.when(addressRepository.findById(Mockito.any(Integer.class))).thenReturn(optionalAddress);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.deleteShippingAddress(emailId, addressId));
		Assertions.assertEquals("Service.ADDRESS_NOT_FOUND", exception.getMessage());

	}

	//Testing getShippingAddress
	@Test
	void getShippingAddressValidTest() throws EKartException {
		Integer addressId = 123;
		Address address = new Address();
		address.setAddressId(addressId);
		Optional<Address> optionalAddress = Optional.of(address);
		Mockito.when(addressRepository.findById(Mockito.any(Integer.class))).thenReturn(optionalAddress);
		Assertions.assertEquals(addressId, customerService.getShippingAddress(addressId).getAddressId());
	}

	// testing for ADDRESS_NOT_FOUND exception
	@Test
	void getShippingAddressInvalidTest() throws EKartException {
		Integer addressId = 123;

		Optional<Address> optionalAddress = Optional.empty();
		Mockito.when(addressRepository.findById(Mockito.any(Integer.class))).thenReturn(optionalAddress);

		EKartException exception = Assertions.assertThrows(EKartException.class,
				() -> customerService.getShippingAddress(addressId));
		Assertions.assertEquals("Service.ADDRESS_NOT_FOUND", exception.getMessage());

	}
}

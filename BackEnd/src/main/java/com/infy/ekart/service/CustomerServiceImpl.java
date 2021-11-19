package com.infy.ekart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.ekart.dto.AddressDTO;
import com.infy.ekart.dto.CustomerCartDTO;
import com.infy.ekart.dto.CustomerDTO;
import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.entity.Address;
import com.infy.ekart.entity.Customer;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.AddressRepository;
import com.infy.ekart.repository.CustomerRepository;

@Service(value = "customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AddressRepository addressRepository;

	// This method will authenticate customer email id and password and return customer details
	@Override
	public CustomerDTO authenticateCustomer(String emailId, String password) throws EKartException {
		CustomerDTO customerDTO = null;
		List<AddressDTO> customerAddresses = new ArrayList<>();
		//retrieving customer data from repository
		Optional<Customer> optionalCustomer = customerRepository.findById(emailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		//comparing entered password with password stored in DB
		if (!password.equals(customer.getPassword()))
			throw new EKartException("CustomerService.INVALID_CREDENTIALS");

		customerDTO = new CustomerDTO();
		customerDTO.setEmailId(customer.getEmailId());
		customerDTO.setName(customer.getName());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		if (customer.getAddress() != null && !customer.getAddress().isEmpty()) {
		customer.getAddress().forEach(address -> {
			AddressDTO addressDTO = new AddressDTO();
			addressDTO.setAddressId(address.getAddressId());
			addressDTO.setAddressLine1(address.getAddressLine1());
			addressDTO.setAddressLine2(address.getAddressLine2());
			addressDTO.setCity(address.getCity());
			addressDTO.setContactNumber(address.getContactNumber());
			addressDTO.setPin(address.getPin());
			addressDTO.setState(address.getState());

			customerAddresses.add(addressDTO);
		});

		customerDTO.setAddresses(customerAddresses);
		}
		List<CustomerCartDTO> customerCartDTOs = new ArrayList<>();
		if(customer.getCustomerCarts() != null && !customer.getCustomerCarts().isEmpty()) {
		customer.getCustomerCarts().forEach(customerCart -> {
			CustomerCartDTO cart = new CustomerCartDTO();
			cart.setCartId(customerCart.getCartId());
			cart.setQuantity(customerCart.getQuantity());
			ProductDTO productDTO = new ProductDTO();
			productDTO.setBrand(customerCart.getProduct().getBrand());
			productDTO.setCategory(customerCart.getProduct().getCategory());
			productDTO.setDescription(customerCart.getProduct().getDescription());
			productDTO.setDiscount(customerCart.getProduct().getDiscount());
			productDTO.setName(customerCart.getProduct().getName());
			productDTO.setPrice(customerCart.getProduct().getPrice());
			productDTO.setProductId(customerCart.getProduct().getProductId());
			productDTO.setQuantity(customerCart.getProduct().getQuantity());

			cart.setProduct(productDTO);

			customerCartDTOs.add(cart);

		});

		customerDTO.setCustomerCarts(customerCartDTOs);
		}
		return customerDTO;

	}

	//This method will add a new customer
	@Override
	public String registerNewCustomer(CustomerDTO customerDTO) throws EKartException {
		String registeredWithEmailId = null;
		//check whether specified email id is already in use by other customer
		boolean isEmailNotAvailable = customerRepository.findById(customerDTO.getEmailId().toLowerCase()).isEmpty();
		//check whether specified phone no. is already in use by other customer
		boolean isPhoneNumberNotAvailable = customerRepository.findByPhoneNumber(customerDTO.getPhoneNumber()).isEmpty();
		if (isEmailNotAvailable) {
			if (isPhoneNumberNotAvailable) {
				Customer customer = new Customer();
				customer.setEmailId(customerDTO.getEmailId().toLowerCase());
				customer.setName(customerDTO.getName());
				customer.setPassword(customerDTO.getPassword());
				customer.setPhoneNumber(customerDTO.getPhoneNumber());
				customerRepository.save(customer);
				registeredWithEmailId = customer.getEmailId();
			} else {
				throw new EKartException("CustomerService.PHONE_NUMBER_ALREADY_IN_USE");
			}
		} else {
			throw new EKartException("CustomerService.EMAIL_ID_ALREADY_IN_USE");
		}
		return registeredWithEmailId;

	}

	// This method will update name and phone number of a customer
	@Override
	public void updateProfile(CustomerDTO customerDTO) throws EKartException {
		Customer newCustomer = null;
		//retrieving all list of customers from repository, whose phone number same as received phone number
		List<Customer> customers = customerRepository.findByPhoneNumber(customerDTO.getPhoneNumber());
		if (!customers.isEmpty()) {
			//take first customer object from the list if list is not-Empty
			newCustomer = customers.get(0);
		} else {
			//if list is empty
			//retrieve customer details by emailId
			Optional<Customer> optionalCustomer = customerRepository.findById(customerDTO.getEmailId().toLowerCase());
			Customer customer = optionalCustomer.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));

			customer.setName(customerDTO.getName());
			customer.setPhoneNumber(customerDTO.getPhoneNumber());
			return;
		}
		// if list is not-Empty
		// compare emailId of first customer object from list with emailLd of received customer  
		if (!newCustomer.getEmailId().equalsIgnoreCase(customerDTO.getEmailId())) {
			throw new EKartException("CustomerService.PHONE_NUMBER_ALREADY_IN_USE");
		} else {
			Optional<Customer> optionalCustomer = customerRepository.findById(customerDTO.getEmailId().toLowerCase());
			Customer customer = optionalCustomer.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
			customer.setName(customerDTO.getName());
			customer.setPhoneNumber(customerDTO.getPhoneNumber());
		}

	}

	//this method will update the password of customer
	@Override
	public void changePassword(String customerEmailId, String currentPassword, String newPassword)
			throws EKartException {
		//retrieving customer details from repository
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		String passwordFromDB = customer.getPassword();
		//comparing entered password with password stored in database
		if (!passwordFromDB.equals(currentPassword))
			throw new EKartException("CustomerService.INVALID_CURRENT_PASSWORD");
		//comparing current password with new password 
		if (currentPassword.equals(newPassword))
			throw new EKartException("CustomerService.OLD_PASSWORD_NEW_PASSWORD_SAME");
		customer.setPassword(newPassword);
	}

	//this method adds new address to customer details
	@Override
	public Integer addShippingAddress(String customerEmailId, AddressDTO addressDTO) throws EKartException {
		//retrieving customer data from repository
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		//retrieving stored List of Address for a customer
		List<Address> listOfCustomerAddress = customer.getAddress();
		Address newShippingAddress = new Address();
		newShippingAddress.setAddressLine1(addressDTO.getAddressLine1());
		newShippingAddress.setAddressLine2(addressDTO.getAddressLine2());
		newShippingAddress.setCity(addressDTO.getCity());
		newShippingAddress.setContactNumber(addressDTO.getContactNumber());
		newShippingAddress.setPin(addressDTO.getPin());
		newShippingAddress.setState(addressDTO.getState());
		// adding a new Address in the list
		listOfCustomerAddress.add(newShippingAddress);
		customer.setAddress(listOfCustomerAddress);
		Customer customerAfterSave = customerRepository.save(customer);
		List<Address> customerAddressEntitiesAfterAddition = customerAfterSave.getAddress();
		Address newAddress = customerAddressEntitiesAfterAddition.get(customerAddressEntitiesAfterAddition.size() - 1);
		return newAddress.getAddressId();
	}
	
	//this method will update the address
	@Override
	public void updateShippingAddress(AddressDTO addressDTO) throws EKartException {
		//retrieving address details from repository
		Optional<Address> optionalAddress = addressRepository.findById(addressDTO.getAddressId());
		Address address = optionalAddress.orElseThrow(() -> new EKartException("Service.ADDRESS_NOT_FOUND"));
		address.setAddressLine1(addressDTO.getAddressLine1());
		address.setAddressLine2(addressDTO.getAddressLine2());
		address.setCity(addressDTO.getCity());
		address.setContactNumber(addressDTO.getContactNumber());
		address.setPin(addressDTO.getPin());
		address.setState(addressDTO.getState());
	}

	//this method will remove address from a particular customer details
	@Override
	public void deleteShippingAddress(String customerEmailId, Integer addressId) throws EKartException {
		//retrieving customer details from repository
		Optional<Customer> optionalCustomer = customerRepository.findById(customerEmailId.toLowerCase());
		Customer customer = optionalCustomer.orElseThrow(() -> new EKartException("Service.CUSTOMER_NOT_FOUND"));
		List<Address> listOfCustomerAddress = customer.getAddress();
		
		//retrieving Address data from repository
		Optional<Address> optionalAddress = addressRepository.findById(addressId);
		Address address = optionalAddress.orElseThrow(() -> new EKartException("Service.ADDRESS_NOT_FOUND"));
		//removing the retrieved address from retrieved customer details
		listOfCustomerAddress.remove(address);
		customer.setAddress(listOfCustomerAddress);
	}

	//this method will receive adddressId and retrieve address details from database
	@Override
	public AddressDTO getShippingAddress(Integer addressId) throws EKartException {
		//retrieving address details from repository
		Optional<Address> optionalAddress = addressRepository.findById(addressId);
		Address address = optionalAddress.orElseThrow(() -> new EKartException("Service.ADDRESS_NOT_FOUND"));
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setAddressId(address.getAddressId());
		addressDTO.setAddressLine1(address.getAddressLine1());
		addressDTO.setAddressLine2(address.getAddressLine2());
		addressDTO.setCity(address.getCity());
		addressDTO.setContactNumber(address.getContactNumber());
		addressDTO.setPin(address.getPin());
		addressDTO.setState(address.getState());
		return addressDTO;
	}
	//this method add only shipping address in case the customer doesn't want to save the address for future use
	@Override
	public Integer addOnlyShippingAddress(AddressDTO addressDTO) throws EKartException{
		Address newShippingAddress = new Address();
		newShippingAddress.setAddressLine1(addressDTO.getAddressLine1());
		newShippingAddress.setAddressLine2(addressDTO.getAddressLine2());
		newShippingAddress.setCity(addressDTO.getCity());
		newShippingAddress.setContactNumber(addressDTO.getContactNumber());
		newShippingAddress.setPin(addressDTO.getPin());
		newShippingAddress.setState(addressDTO.getState());
		return addressRepository.save(newShippingAddress).getAddressId();}

}

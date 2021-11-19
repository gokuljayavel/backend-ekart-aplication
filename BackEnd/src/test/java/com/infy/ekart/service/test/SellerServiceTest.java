package com.infy.ekart.service.test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.ekart.dto.SellerDTO;
import com.infy.ekart.entity.Seller;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.SellerRepository;
import com.infy.ekart.service.SellerService;
import com.infy.ekart.service.SellerServiceImpl;
import com.infy.ekart.utility.HashingUtility;
import com.infy.ekart.validator.SellerValidator;

@SpringBootTest
class SellerServiceTest {

	@Mock
	private SellerRepository sellerRepository;
	@Mock
	private SellerValidator validator;

	@InjectMocks
	private SellerService sellerService = new SellerServiceImpl();

	// testing authenticateSeller() method
	@Test
	void authenticateSellerValid() throws EKartException, NoSuchAlgorithmException {
		String emailId = "jack@infosys.com";
		String password = "Jack@123";
		String hashedPassword = HashingUtility.getHashValue(password);
		Seller seller = new Seller();
		seller.setEmailId(emailId);
		seller.setPassword(hashedPassword);
		Optional<Seller> optionalSeller = Optional.of(seller);
		Mockito.when(sellerRepository.getPasswordOfSeller(Mockito.anyString())).thenReturn(hashedPassword);
		Mockito.when(sellerRepository.findById(emailId.toLowerCase())).thenReturn(optionalSeller);
		Assertions.assertEquals(seller.getEmailId(), sellerService.authenticateSeller(emailId, password).getEmailId());
	}

	// testing for SELLER_NOT_FOUND exception
	@Test
	void authenticateSellerInvalidTest1() throws EKartException, NoSuchAlgorithmException {

		Mockito.when(sellerRepository.getPasswordOfSeller(Mockito.anyString())).thenReturn(null);
		EKartException eKartException = Assertions.assertThrows(EKartException.class,
				() -> sellerService.authenticateSeller("tom@infosys.com", "Tom@123"));

		Assertions.assertEquals("Service.SELLER_NOT_FOUND", eKartException.getMessage());
	}

	// testing for INVALID_CREDENTIALS exception
	@Test
	void authenticateSellerInvalidTest2() throws EKartException, NoSuchAlgorithmException {

		String emailId = "Jack@infosys.com";
		String password = "Jack@12345";
		String hashedPassword = HashingUtility.getHashValue(password);

		Mockito.when(sellerRepository.getPasswordOfSeller(Mockito.anyString())).thenReturn(hashedPassword);

		EKartException eKartException = Assertions.assertThrows(EKartException.class,
				() -> sellerService.authenticateSeller(emailId, "Jack@12"));

		Assertions.assertEquals("SellerService.INVALID_CREDENTIALS", eKartException.getMessage());
	}

	// testing changePassword() method
	@Test
	void changePasswordValidTest() throws EKartException, NoSuchAlgorithmException {
		
		SellerDTO sellerDTO = new SellerDTO();
		String newPassword = "Charles@423";
		String currentPassword = "Charles@123";
		String emailId = "charles@infosys.com";
		sellerDTO.setEmailId(emailId);

		sellerDTO.setPassword(currentPassword);
		sellerDTO.setNewPassword(newPassword);
		sellerDTO.setConfirmNewPassword(newPassword);
		
		Mockito.when(sellerRepository.getPasswordOfSeller(Mockito.anyString())).thenReturn(HashingUtility.getHashValue(currentPassword));

		Seller seller = new Seller();

		seller.setPassword(HashingUtility.getHashValue(currentPassword));
		seller.setEmailId(emailId);

		Optional<Seller> optionalSeller = Optional.of(seller);

		Mockito.when(sellerRepository.findById(Mockito.anyString())).thenReturn(optionalSeller);

		Assertions.assertDoesNotThrow(() -> sellerService.changePassword(sellerDTO));

	}

	// testing for OLD_PASSWORD_NEW_PASSWORD_SAME exception
	@Test
	void changePasswordInvalidTest1() throws EKartException, NoSuchAlgorithmException {

		SellerDTO sellerDTO = new SellerDTO();
		String newPassword = "Charles@423";
		String currentPassword = "Charles@423";
		String emailId = "charles@infosys.com";
		sellerDTO.setEmailId(emailId);

		sellerDTO.setPassword(currentPassword);
		sellerDTO.setNewPassword(newPassword);
		sellerDTO.setConfirmNewPassword(newPassword);
		
		Mockito.when(sellerRepository.getPasswordOfSeller(Mockito.anyString())).thenReturn(HashingUtility.getHashValue(currentPassword));

		EKartException eKartException = Assertions.assertThrows(EKartException.class,
				() -> sellerService.changePassword(sellerDTO));
		Assertions.assertEquals("SellerService.OLD_PASSWORD_NEW_PASSWORD_SAME", eKartException.getMessage());

	}

	// testing for INVALID_CURRENT_PASSWORD exception
	@Test
	void changePasswordInvalidTest2() throws EKartException, NoSuchAlgorithmException {

		SellerDTO sellerDTO = new SellerDTO();
		String newPassword = "Charles@423";
		String currentPassword = "Charles@123";
		String emailId = "charles@infosys.com";
		sellerDTO.setEmailId(emailId);

		sellerDTO.setPassword(currentPassword);
		sellerDTO.setNewPassword(newPassword);
		sellerDTO.setConfirmNewPassword(newPassword);
		
		Mockito.when(sellerRepository.getPasswordOfSeller(Mockito.anyString())).thenReturn(HashingUtility.getHashValue("Charlie@1234"));

		EKartException eKartException = Assertions.assertThrows(EKartException.class,
				() -> sellerService.changePassword(sellerDTO));
		Assertions.assertEquals("SellerService.INVALID_CURRENT_PASSWORD", eKartException.getMessage());

	}

	//testing for registerNewSeller() method
	@Test
	void registerNewSellerValidTest() throws EKartException, NoSuchAlgorithmException {

		SellerDTO sellerDTO = new SellerDTO();
		sellerDTO.setEmailId("charles@infosys.com");
		sellerDTO.setPhoneNumber("9876409375");
		sellerDTO.setPassword("Charles@123");

		Optional<Seller> optionalSeller = Optional.empty();

		Mockito.when(sellerRepository.findById(Mockito.anyString())).thenReturn(optionalSeller);
		List<Seller> newList = new ArrayList<>();

		Mockito.when(sellerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(newList);
		Assertions.assertEquals(sellerDTO.getEmailId(), sellerService.registerNewSeller(sellerDTO));
	}

	// testing for EMAIL_ID_ALREADY_IN_USE exception
	@Test
	void registerNewSellerInvalidTest1() throws EKartException {

		SellerDTO sellerDTO = new SellerDTO();
		sellerDTO.setEmailId("charles@infosys.com");

		Optional<Seller> optionalSeller = Optional.of(new Seller());

		Mockito.when(sellerRepository.findById(Mockito.anyString())).thenReturn(optionalSeller);

		EKartException eKartException = Assertions.assertThrows(EKartException.class,
				() -> sellerService.registerNewSeller(sellerDTO));
		Assertions.assertEquals("SellerService.EMAIL_ID_ALREADY_IN_USE", eKartException.getMessage());

	}

	// testing for PHONE_NUMBER_ALREADY_IN_USE exception
	@Test
	void registerNewSellerInvalidTest2() throws EKartException {

		SellerDTO sellerDTO = new SellerDTO();
		sellerDTO.setEmailId("charles@infosys.com");
		sellerDTO.setPhoneNumber("9876409375");
		sellerDTO.setPassword("Charles@123");

		Optional<Seller> optionalSeller = Optional.empty();

		Mockito.when(sellerRepository.findById(Mockito.anyString())).thenReturn(optionalSeller);

		Seller seller = new Seller();
		seller.setEmailId("jerry1992@infosys.com");
		seller.setPhoneNumber("9876409375");
		List<Seller> newList = new ArrayList<>();
		newList.add(seller);

		Mockito.when(sellerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(newList);
		EKartException eKartException = Assertions.assertThrows(EKartException.class,
				() -> sellerService.registerNewSeller(sellerDTO));
		Assertions.assertEquals("SellerService.PHONE_NUMBER_ALREADY_IN_USE", eKartException.getMessage());
	}

	// testing updateProfile() method
 	@Test
	void updateProfileValidTest() throws EKartException {
		SellerDTO sellerDTO = new SellerDTO();
		sellerDTO.setEmailId("jack@infosys.com");
		sellerDTO.setPhoneNumber("9876409375");
		sellerDTO.setPassword("Charles@123");
		sellerDTO.setNewPassword("");

		Seller seller = new Seller();
		seller.setName("Jack");
		seller.setPhoneNumber("9876543210");
		seller.setAddress("Ist Main, Building No.3, Park Square, Salem, US");
		seller.setEmailId("jack@infosys.com");

		List<Seller> optionalSeller = new ArrayList<>();
		optionalSeller.add(seller);
		Mockito.when(sellerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(optionalSeller);

		Seller newSeller = new Seller();
		newSeller.setEmailId("jack@g.com");
		Optional<Seller> optionalSeller1 = Optional.ofNullable(newSeller);
		Mockito.when(sellerRepository.findById(Mockito.anyString())).thenReturn(optionalSeller1);

		Assertions.assertDoesNotThrow(() -> sellerService.updateProfile(sellerDTO));

	}

 	// testing PHONE_NUMBER_ALREADY_IN_USE exception
	@Test
	void updateProfileInvalidTest() throws EKartException {
		SellerDTO sellerDTO = new SellerDTO();
		sellerDTO.setEmailId("jack@g.com");
		sellerDTO.setPhoneNumber("9876409375");
		sellerDTO.setPassword("Charles@123");

		Seller seller = new Seller();
		seller.setName("Jack Roger");
		seller.setPhoneNumber("9876543210");
		seller.setAddress("Ist Main, Building No.3, Park Square, Salem, US");
		seller.setEmailId("jack@gmail.com");

		List<Seller> optionalSeller = new ArrayList<>();
		optionalSeller.add(seller);
		Mockito.when(sellerRepository.findByPhoneNumber(Mockito.anyString())).thenReturn(optionalSeller);

		Seller newSeller = new Seller();
		newSeller.setEmailId("jack@g.com");

		Optional<Seller> optionalSeller1 = Optional.ofNullable(newSeller);
		Mockito.when(sellerRepository.findById(Mockito.anyString())).thenReturn(optionalSeller1);

		EKartException eKartException = Assertions.assertThrows(EKartException.class,
				() -> sellerService.updateProfile(sellerDTO));
		Assertions.assertEquals("SellerService.PHONE_NUMBER_ALREADY_IN_USE", eKartException.getMessage());

	}
}

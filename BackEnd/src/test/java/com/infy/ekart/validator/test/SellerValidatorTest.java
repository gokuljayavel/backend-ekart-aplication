package com.infy.ekart.validator.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.infy.ekart.dto.SellerDTO;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.validator.SellerValidator;

class SellerValidatorTest {

	private SellerDTO sellerDTO;

	@BeforeEach
	public void setUp() {
		sellerDTO = new SellerDTO();
		sellerDTO.setEmailId("fahad@infosys.com");
		sellerDTO.setNewPassword("fahad@123");
	}

	@Test
	void validatePasswordsForSellerChangePasswordValidTest() {
		sellerDTO.setConfirmNewPassword("fahad@123");
		Assertions.assertEquals(sellerDTO.getNewPassword(), sellerDTO.getConfirmNewPassword());
	}
	
	@Test
	void validatePasswordsForSellerChangePasswordInvalidTest() {
		sellerDTO.setConfirmNewPassword("fahad@12");
		Exception exception = Assertions.assertThrows(EKartException.class,
				() -> SellerValidator.validatePasswordsForSellerChangePassword(sellerDTO));
		Assertions.assertEquals("SellerValidator.NEW_PASSWORD_AND_CONFIRM_NEW_PASSWORD_MISMATCH", exception.getMessage());
	}
}

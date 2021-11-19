package com.infy.ekart.validator;

import com.infy.ekart.dto.SellerDTO;
import com.infy.ekart.exception.EKartException;

public class SellerValidator {
		
	public static void validatePasswordsForSellerChangePassword(SellerDTO sellerDTO) throws EKartException {
		if(!sellerDTO.getNewPassword().equals(sellerDTO.getConfirmNewPassword()))
			throw new EKartException("SellerValidator.NEW_PASSWORD_AND_CONFIRM_NEW_PASSWORD_MISMATCH");
	}
	
}

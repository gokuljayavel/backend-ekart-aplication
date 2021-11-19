package com.infy.ekart.service;

import java.security.NoSuchAlgorithmException;

import com.infy.ekart.dto.SellerDTO;
import com.infy.ekart.exception.EKartException;

public interface SellerService {
	
	public SellerDTO authenticateSeller(String emailId, String password) throws EKartException, NoSuchAlgorithmException;

	public String registerNewSeller(SellerDTO sellerDTO) throws EKartException, NoSuchAlgorithmException ;
	
	public void updateProfile(SellerDTO sellerDTO) throws EKartException;
	
	public void changePassword(SellerDTO sellerDTO) throws EKartException, NoSuchAlgorithmException;
}

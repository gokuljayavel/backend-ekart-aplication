package com.infy.ekart.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.ekart.dto.ProductDTO;
import com.infy.ekart.dto.SellerDTO;
import com.infy.ekart.entity.Product;
import com.infy.ekart.entity.Seller;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.repository.SellerRepository;
import com.infy.ekart.utility.HashingUtility;
import com.infy.ekart.validator.SellerValidator;

@Service(value = "sellerService")
@Transactional
public class SellerServiceImpl implements SellerService {

	@Autowired
	private SellerRepository sellerRepository;

	/**
     * @params
     *          emailID and password ----> Seller Id and password
     *
     * @operation
     *  
     * 			Authenticates the seller
     * 				--Fetches password from DB
     *
     * @returns
     *          SellerDTO -----> authenticated seller
     */
	@Override
	public SellerDTO authenticateSeller(String emailId, String password)
			throws EKartException, NoSuchAlgorithmException {
		SellerDTO sellerDTO = null;
		emailId = emailId.toLowerCase();

		String passwordFromDB = sellerRepository.getPasswordOfSeller(emailId);
		if (passwordFromDB != null) {
			String hashedPassword = HashingUtility.getHashValue(password);

			if (hashedPassword.equals(passwordFromDB)) {

				List<ProductDTO> sellingProductDTOs = new ArrayList<>();
				Optional<Seller> optionalSeller = sellerRepository.findById(emailId);
				Seller seller = optionalSeller.orElseThrow(() -> new EKartException("Service.SELLER_NOT_FOUND"));
				sellerDTO = new SellerDTO();
				sellerDTO.setAddress(seller.getAddress());
				sellerDTO.setEmailId(seller.getEmailId());
				sellerDTO.setName(seller.getName());
				sellerDTO.setPhoneNumber(seller.getPhoneNumber());
				if (seller.getProduct() != null) {
					for (Product product : seller.getProduct()) {
						ProductDTO productDTO = new ProductDTO();
						productDTO.setBrand(product.getBrand());
						productDTO.setCategory(product.getCategory());
						productDTO.setDescription(product.getDescription());
						productDTO.setDiscount(product.getDiscount());
						productDTO.setName(product.getName());
						productDTO.setPrice(product.getPrice());
						productDTO.setProductId(product.getProductId());
						productDTO.setQuantity(product.getQuantity());

						sellingProductDTOs.add(productDTO);
					}
				}

				sellerDTO.setProducts(sellingProductDTOs);
			} else {
				throw new EKartException("SellerService.INVALID_CREDENTIALS");
			}
		} else {
			throw new EKartException("Service.SELLER_NOT_FOUND");
		}
		return sellerDTO;
	}

	/**
     * @params
     *         sellerDTO 
     *
     * @operation 
     * 				registers the new seller and persists it in the database
     *
     * @returns
     *          emailId of the new registered seller
     */
	@Override
	public String registerNewSeller(SellerDTO sellerDTO) throws EKartException, NoSuchAlgorithmException {
		String registeredWithEmailId = null;

		boolean available = sellerRepository.findById(sellerDTO.getEmailId().toLowerCase()).isEmpty();
		if (available) {

			if (sellerRepository.findByPhoneNumber(sellerDTO.getPhoneNumber()).isEmpty()) {
				String emailIdToDB = sellerDTO.getEmailId().toLowerCase();
				String passwordToDB = HashingUtility.getHashValue(sellerDTO.getPassword());

				sellerDTO.setEmailId(emailIdToDB);
				sellerDTO.setPassword(passwordToDB);
				Seller seller = new Seller();

				seller.setAddress(sellerDTO.getAddress());
				seller.setEmailId(sellerDTO.getEmailId().toLowerCase());
				seller.setName(sellerDTO.getName());
				seller.setPassword(sellerDTO.getPassword());
				seller.setPhoneNumber(sellerDTO.getPhoneNumber());
				seller.setProduct(null);
				sellerRepository.save(seller);

				registeredWithEmailId = seller.getEmailId();

			} else {
				throw new EKartException("SellerService.PHONE_NUMBER_ALREADY_IN_USE");
			}
		} else {
			throw new EKartException("SellerService.EMAIL_ID_ALREADY_IN_USE");
		}

		return registeredWithEmailId;
	}


	 /**
    * @params
    *         sellerDTO
    *
    * @operation 
    * 				Update the profile details of the existing seller
    * 					--Fetches the existing seller based on phoneNumber from DB   
    */
	@Override
	public void updateProfile(SellerDTO sellerDTO) throws EKartException {
		Seller newSeller = null;

		List<Seller> sellers = sellerRepository.findByPhoneNumber(sellerDTO.getPhoneNumber());

		if (!sellers.isEmpty()) {
			newSeller = sellers.get(0);
		} else {
			Optional<Seller> optionalSeller = sellerRepository.findById(sellerDTO.getEmailId().toLowerCase());
			Seller seller = optionalSeller.orElseThrow(() -> new EKartException("Service.SELLER_NOT_FOUND"));
			seller.setAddress(sellerDTO.getAddress());
			seller.setName(sellerDTO.getName());
			seller.setPhoneNumber(sellerDTO.getPhoneNumber());
			return;
		}
		if (newSeller.getEmailId().equals(sellerDTO.getEmailId())) {
			Optional<Seller> optionalSeller = sellerRepository.findById(sellerDTO.getEmailId().toLowerCase());
			Seller seller = optionalSeller.orElseThrow(() -> new EKartException("Service.SELLER_NOT_FOUND"));
			seller.setAddress(sellerDTO.getAddress());
			seller.setName(sellerDTO.getName());
			seller.setPhoneNumber(sellerDTO.getPhoneNumber());
		} else {
			throw new EKartException("SellerService.PHONE_NUMBER_ALREADY_IN_USE");
		}
	}

	/**
     * @params
     *         sellerDTO
     *
     * @operation 
     * 
     * 				changes the password of already existing seller
     */
	@Override
	public void changePassword(SellerDTO sellerDTO) throws EKartException, NoSuchAlgorithmException {
		SellerValidator.validatePasswordsForSellerChangePassword(sellerDTO);

		if (sellerDTO.getPassword().equals(sellerDTO.getNewPassword()))
			throw new EKartException("SellerService.OLD_PASSWORD_NEW_PASSWORD_SAME");

		String hashedPasswordFromDB = sellerRepository.getPasswordOfSeller(sellerDTO.getEmailId());
		String hashedCurrentPassword = HashingUtility.getHashValue(sellerDTO.getPassword());

		if (!hashedPasswordFromDB.equals(hashedCurrentPassword))
			throw new EKartException("SellerService.INVALID_CURRENT_PASSWORD");

		String newHashedPassword = HashingUtility.getHashValue(sellerDTO.getNewPassword());
		Optional<Seller> optionalSeller = sellerRepository.findById(sellerDTO.getEmailId().toLowerCase());
		Seller seller = optionalSeller.orElseThrow(() -> new EKartException("Service.SELLER_NOT_FOUND"));
		seller.setPassword(newHashedPassword);
	}

}

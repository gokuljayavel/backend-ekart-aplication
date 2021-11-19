package com.infy.ekart.api;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.ekart.dto.SellerDTO;
import com.infy.ekart.exception.EKartException;
import com.infy.ekart.service.SellerService;

@CrossOrigin
@RestController
@RequestMapping(value = "/seller-api")
@Validated
public class SellerAPI {

	@Autowired
	private SellerService sellerService;

	@Autowired
	private SellerService sellerLoginService;

	@Autowired
	private Environment environment;

	static Log logger = LogFactory.getLog(SellerAPI.class);

	@PostMapping(value = "/sellers")
	public ResponseEntity<String> registerSeller(@RequestBody SellerDTO sellerDTO) throws EKartException, NoSuchAlgorithmException {

		logger.info("SELLER TRYING TO REGISTER. SELLER EMAIL ID: " + sellerDTO.getEmailId());
		String registeredWithEmailID = sellerService.registerNewSeller(sellerDTO);
		logger.info("SELLER REGISTRATION SUCCESSFUL. SELLER EMAIL ID: " + sellerDTO.getEmailId());
		registeredWithEmailID = environment.getProperty("SellerAPI.SELLER_REGISTRATION_SUCCESS")
				+ registeredWithEmailID;
		return new ResponseEntity<>(registeredWithEmailID, HttpStatus.OK);

	}

	@PostMapping(value = "/login")
	public ResponseEntity<SellerDTO> authenticateSeller(@RequestBody SellerDTO sellerDTO) throws EKartException, NoSuchAlgorithmException {

		logger.info("SELLER TRYING TO LOGIN. SELLER EMAIL ID: " + sellerDTO.getEmailId());
		SellerDTO sellerFromDB = sellerLoginService.authenticateSeller(sellerDTO.getEmailId(), sellerDTO.getPassword());
		logger.info("SELLER LOGIN SUCCESSFUL. SELLER EMAIL ID: " + sellerDTO.getEmailId());
		return new ResponseEntity<>(sellerFromDB, HttpStatus.OK);
	}

	@PutMapping(value = "/sellers")
	public ResponseEntity<String> updateSellerProfile(@RequestBody SellerDTO sellerDTO) throws EKartException {
		logger.info("SELLER TRYING TO UPDATE PROFILE. SELLER EMAIL ID: " + sellerDTO.getEmailId());
		sellerService.updateProfile(sellerDTO);
		logger.info("SELLER PROFILE UPDATED SUCCESSFULLY. SELLER EMAIL ID: " + sellerDTO.getEmailId());
		String modificationSuccessMsg = environment.getProperty("SellerAPI.SELLER_DETAILS_UPDATION_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

	@PutMapping(value = "/sellers/password")
	public ResponseEntity<String> changePassword(@RequestBody SellerDTO sellerDTO) throws EKartException, NoSuchAlgorithmException {

		logger.info("SELLER TRYING TO CHANGE PASSWORD. SELLER EMAIL ID: " + sellerDTO.getEmailId());
		sellerService.changePassword(sellerDTO);
		logger.info("SELLER CHANGE PASSWORD SUCCESSFUL. SELLER EMAIL ID: " + sellerDTO.getEmailId());
		String modificationSuccessMsg = environment.getProperty("SellerAPI.SELLER_PASSWORD_CHANGE_SUCCESS");
		return new ResponseEntity<>(modificationSuccessMsg, HttpStatus.OK);

	}

}

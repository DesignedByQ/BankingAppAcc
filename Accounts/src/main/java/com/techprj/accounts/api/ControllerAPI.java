package com.techprj.accounts.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techprj.accounts.dto.AccountDTO;
import com.techprj.accounts.service.ServiceDAOImpl;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Validated
public class ControllerAPI {
	
	@Autowired
	ServiceDAOImpl serviceDAOImpl;
	
	@PostMapping(value="/createaccount", consumes = {MediaType.ALL_VALUE})
	public ResponseEntity<AccountDTO> createAcc(@RequestBody AccountDTO accountDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(serviceDAOImpl.createAccount(accountDTO));
	}

}

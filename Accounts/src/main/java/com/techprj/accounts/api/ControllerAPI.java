package com.techprj.accounts.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techprj.accounts.dto.AccountDTO;
import com.techprj.accounts.service.ServiceDAOImpl;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class ControllerAPI {
	
	@Autowired
	ServiceDAOImpl serviceDAOImpl;
	
	@PostMapping(value="/createaccount", consumes = {MediaType.ALL_VALUE})
	public ResponseEntity<AccountDTO> createAcc(@RequestBody AccountDTO accountDTO) {
		return ResponseEntity.status(HttpStatus.CREATED).body(serviceDAOImpl.createAccount(accountDTO));
	}
	
	@GetMapping(value="/getaccount/{accountid}", consumes = {MediaType.ALL_VALUE}, produces = {"application/json", "application/xml"})
	public ResponseEntity<AccountDTO> getAcc(@PathVariable("accountid") Long accountid) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceDAOImpl.getAccount(accountid));
	}
	
	@GetMapping(value="/getaccounts/{userid}", consumes = {MediaType.ALL_VALUE}, produces = {"application/json", "application/xml"})
	public Object[] getAccsByUserId(@PathVariable("userid") Long userid) {
		return serviceDAOImpl.getAccounts(userid);
	}
	
	@GetMapping(value="/test", consumes = {MediaType.ALL_VALUE}, produces = {"application/json", "application/xml"})
	public Object tester() {
		System.out.println(111);
		serviceDAOImpl.test();
		return ResponseEntity.status(HttpStatus.OK);
	}
	
	@PatchMapping(value="/updatebalance/{accid}", consumes = {MediaType.ALL_VALUE})
	public ResponseEntity<AccountDTO> updateBal(@PathVariable("accid") Long accid, @RequestBody Map<Object, Object> fields) {
		
		Object balanceObj = fields.get("balance");
		double balance = Double.parseDouble(balanceObj.toString());
		
		fields.put("balance", balance);
		
		System.out.println(fields);
		return ResponseEntity.status(HttpStatus.OK).body(serviceDAOImpl.updateBalance(accid, fields));
	}
	
	@PatchMapping(value="/transfer/from/{accid}/to/{accid1}", consumes = {MediaType.ALL_VALUE})
	public ResponseEntity<List<AccountDTO>> updateBalInt(@PathVariable("accid") Long accid, @PathVariable("accid1") Long accid1, @RequestBody Map<Object, Object> fields) {
		
		Object balanceObj = fields.get("balance");
		double balance = Double.parseDouble(balanceObj.toString());
		
		fields.put("balance", balance);
		
		System.out.println(fields);
		return ResponseEntity.status(HttpStatus.OK).body(serviceDAOImpl.updateBalanceInt(accid, accid1, fields));
	}
	
	@PatchMapping(value="/transfer/external/from/{accid}/to/{accid1}", consumes = {MediaType.ALL_VALUE})
	public ResponseEntity<AccountDTO> updateBalExt(@PathVariable("accid") Long accid, @PathVariable("accid1") Long accid1, @RequestBody Map<Object, Object> fields) {
		
		Object balanceObj = fields.get("balance");
		double balance = Double.parseDouble(balanceObj.toString());
		
		fields.put("balance", balance);
		
		System.out.println(fields);
		return ResponseEntity.status(HttpStatus.OK).body(serviceDAOImpl.updateBalanceExt(accid, accid1, fields));
	}

}

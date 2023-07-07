package com.techprj.accounts.service;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.techprj.accounts.dto.AccountDTO;
import com.techprj.accounts.entity.Account;
import com.techprj.accounts.repo.AccountRepo;

import com.techprj.banking.dto.UserProfileDTO;

@Service(value="ServiceDAO")
@Transactional
public class ServiceDAOImpl implements ServiceDAO{
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	AccountRepo accountRepo;
	
	@Autowired
	RestTemplate restTemplate;

	@Override
	public AccountDTO createAccount(AccountDTO accountDTO) {
		
		Long[] ids = new Long[2];
		ids[0] = accountDTO.getUserProfileDTO()[0].getIdUserProfile();
		
		if(accountDTO.getUserProfileDTO().length > 1)
			ids[1] = accountDTO.getUserProfileDTO()[1].getIdUserProfile();
		
		Account a = new Account();
		a.setBalance(accountDTO.getBalance());
		a.setType(accountDTO.getType());
		a.setUserProfileID(ids);
		a.setTranslog(null);
			
		Account aSaved = accountRepo.saveAndFlush(a);
		
		accountDTO.setAccountId(aSaved.getAccountId());
	
		return accountDTO;
		
	}
	
	//UserProfileDTO updto = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+id, UserProfileDTO.class);

}

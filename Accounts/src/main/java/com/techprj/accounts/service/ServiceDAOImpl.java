package com.techprj.accounts.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.techprj.accounts.dto.AccountDTO;
import com.techprj.accounts.dto.TransLogDTO;
import com.techprj.accounts.entity.Account;
import com.techprj.accounts.entity.TransLog;
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
		
		for(int i = 0; i < accountDTO.getUserProfileDTO().size(); i++) {
			
			ids[i] = accountDTO.getUserProfileDTO().get(i).getIdUserProfile();
			
		}
		
//		for(UserProfileDTO up: accountDTO.getUserProfileDTO()) {
//				
//		}
//		ids[0] = accountDTO.getUserProfileDTO()[0].getIdUserProfile();
//		
//		if(accountDTO.getUserProfileDTO().length > 1)
//			ids[1] = accountDTO.getUserProfileDTO()[1].getIdUserProfile();
		
		Account a = new Account();
		a.setBalance(accountDTO.getBalance());
		a.setType(accountDTO.getType());
		a.setUserProfileID(ids);
		a.setTranslog(null);
			
		Account aSaved = accountRepo.saveAndFlush(a);
		
		accountDTO.setAccountId(aSaved.getAccountId());
	
		return accountDTO;
		
	}

	@Override
	public AccountDTO getAccount(Long accountid) {

		Optional<Account> a = accountRepo.findById(accountid);
		
		if(a.isPresent()) {
			
			List<UserProfileDTO> upl = new ArrayList();
			
			UserProfileDTO updto = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+ a.get().getUserProfileID()[0], UserProfileDTO.class);
			
			upl.add(updto);
			
			if(a.get().getUserProfileID()[1] != null) {
				
				UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+ a.get().getUserProfileID()[1], UserProfileDTO.class);
				
				upl.add(updto1);
				
			}
			
			AccountDTO adto = new AccountDTO();
			adto.setAccountId(a.get().getAccountId());
			adto.setSortCode(a.get().getSortCode());
			adto.setType(a.get().getType());
			adto.setBalance(a.get().getBalance());
			adto.setTransLogDTO(a.get().getTranslogDTO());
			adto.setUserProfileDTO(upl);
			
			return adto;
			
		}
		return null;
	}

	@Override
	public AccountDTO updateBalance(Long accid, Map<Object, Object> fields) {
		
		AccountDTO adto = new AccountDTO();
		
		Optional<Account> a = accountRepo.findById(accid);
		
		//Create transaction
		TransLogDTO trans = new TransLogDTO();
		trans.setDate(LocalDate.now());
		trans.setAmount(Double.parseDouble(fields.get("balance").toString())-a.get().getBalance());
		trans.setFrom((long) 99999999);
		trans.setOldBal(a.get().getBalance());
		trans.setTo(accid);
		trans.setNewBal(Double.parseDouble(fields.get("balance").toString()));
		trans.setReference("Bank Clerk Transaction");
		
		TransLog transConv = modelMapper.map(trans, TransLog.class);
		
		//List<TransLog> tll = new ArrayList<>();
		
		//tll.add(modelMapper.map(trans, TransLog.class));
		
		//adto.setTransLogDTO();
		
		if(a.isPresent()) {
			
			//a.get().setTranslog(tll);
			
			//List<TransLog> fm = (List<TransLog>) modelMapper.map(fields.get("transLogDTO"), TransLog.class);
			
			//fields.put(tll, a)
			
	        fields.forEach((key, value) -> {
	        	
	        	Field field1 = ReflectionUtils.findRequiredField(Account.class, (String) key);
	        	field1.setAccessible(true);
	        	ReflectionUtils.setField(field1, a.get(), value);
	        	
	        });
	        
	        List<TransLog> tlog = a.get().getTranslog();
	        tlog.add(transConv);
	        
	        a.get().setTranslog(tlog);
	        
	        Account updatedAcc = accountRepo.save(a.get());
	        
//	        System.out.println(".................................");
//	        System.out.println(updatedAcc.getUserProfileID().toString());
//	        System.out.println(updatedAcc.getUserProfileID().length);
//	        System.out.println(updatedAcc.getUserProfileID()[0]);
	        	        
			adto.setAccountId(updatedAcc.getAccountId());
			adto.setSortCode(updatedAcc.getSortCode());
			adto.setType(updatedAcc.getType());
			adto.setBalance(updatedAcc.getBalance());
			adto.setTransLogDTO(updatedAcc.getTranslogDTO());
			
			List<UserProfileDTO> upl = new ArrayList();
			
			for(int i = 0; i < updatedAcc.getUserProfileID().length; i++) {
				
				if(updatedAcc.getUserProfileID()[i] != null) {
					UserProfileDTO updto = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+ updatedAcc.getUserProfileID()[i], UserProfileDTO.class);
					System.out.println(updto);
					upl.add(updto);
					
				}
			}
			
			adto.setUserProfileDTO(upl);
			
		}
		
		//if a.balance is differnt to adto.balance then populate the trans 
		
		return adto;
		
	}
	
	
	
	
	
}

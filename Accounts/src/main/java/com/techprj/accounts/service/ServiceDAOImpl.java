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
		a.setSortCode(accountDTO.getSortCode());
		a.setType(accountDTO.getType());
		a.setUserProfileID(ids);
		a.setTranslog(null);
			
		Account aSaved = accountRepo.saveAndFlush(a);
		
		//accountDTO.setAccountId(aSaved.getAccountId());
		AccountDTO adto = new AccountDTO();
		adto.setAccountId(aSaved.getAccountId());
		adto.setBalance(aSaved.getBalance());
		adto.setSortCode(aSaved.getSortCode());
		adto.setType(aSaved.getType());
		//adto.setTransLogDTO(aSaved.getTranslogDTO());
		
		List<UserProfileDTO> upl = new ArrayList();
		
		Long id = aSaved.getUserProfileID()[0];
		
		UserProfileDTO updto = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+ id, UserProfileDTO.class);
		
		upl.add(updto);
		
		if(aSaved.getUserProfileID()[1] != null) {
			
			Long id1 = aSaved.getUserProfileID()[1];
			
			UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+ id1, UserProfileDTO.class);
			
			upl.add(updto1);
			
		}	
		
		adto.setUserProfileDTO(upl);
	
		return adto;
		
	}

	@Override
	public AccountDTO getAccount(Long accountid) {
		//System.out.println(111);
		//System.out.println(accountid);
		Optional<Account> a = accountRepo.findById(accountid);
		//System.out.println(a.get());
		//AccountDTO adto1 = new AccountDTO();
//		if(a.isEmpty()) {
//		System.out.println("empty");
//		}
		
//		a.get().getUserProfileID()[0] = (long) 1;
//		a.get().getUserProfileID()[1] = (long) 9;
		
		System.out.println(a.get());
		if(a.isPresent()) {
			
			//System.out.println("hello");
			
			List<UserProfileDTO> upl = new ArrayList();
			
			//when creatiing an account need to inject the up ids correct instead of null
			Long id = a.get().getUserProfileID()[0];
			//Long id2 = Long.parseLong(id);
			//System.out.println(id.TYPE);
			
			String url = "http://localhost:8080/api/getprobyid/"+id;
		
			UserProfileDTO updto = restTemplate.getForObject(url, UserProfileDTO.class);
			System.out.println(updto.getEmail());
			upl.add(updto);
			
			System.out.println("-------------------------");
			
			//System.out.println(a.get().getUserProfileID()[1]);
		
			
			if(a.get().getUserProfileID()[1] != null) {
				
				Long id1 = a.get().getUserProfileID()[1];
				//System.out.println(id1);
				
				UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+id1, UserProfileDTO.class);
				
				upl.add(updto1);
				
			}
			
			System.out.println(upl.get(1));
			
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
	
	@Override
	public List<AccountDTO> updateBalanceInt(Long accid, Long accid1, Map<Object, Object> fields) {
		
		//get the value of the to acc and add it to the field balance before patching
		//get the val of the from acc and minus the field balance the patch the result
		//patch both accounts
		//create a transaction for both accounts
		//return a accountDTO
		
		//Find accounts
		
		Optional<Account> a = accountRepo.findById(accid);
		//System.out.println(a.get());
		Optional<Account> a1 = accountRepo.findById(accid1);
		//System.out.println(a1.get());
		//Create transaction
		TransLogDTO trans = new TransLogDTO();
		trans.setDate(LocalDate.now());
		trans.setAmount(Double.parseDouble(fields.get("balance").toString()));
		trans.setFrom(accid);
		trans.setOldBal(a.get().getBalance());
		trans.setTo(accid1);
		trans.setNewBal(a.get().getBalance() - Double.parseDouble(fields.get("balance").toString()));
		trans.setReference(((Map<Object, Object>) fields.get("transLogDTO")).get("reference").toString());
		
		TransLog transConv = modelMapper.map(trans, TransLog.class);
		System.out.println(transConv);
		AccountDTO adto = new AccountDTO();
		
		TransLogDTO trans1 = new TransLogDTO();
		trans1.setDate(LocalDate.now());
		trans1.setAmount(Double.parseDouble(fields.get("balance").toString()));
		trans1.setFrom(accid);
		trans1.setOldBal(a1.get().getBalance());
		trans1.setTo(accid1);
		trans1.setNewBal(a1.get().getBalance() + Double.parseDouble(fields.get("balance").toString()));
		trans1.setReference(((Map<Object, Object>) fields.get("transLogDTO")).get("reference").toString());
		
		TransLog transConv1 = modelMapper.map(trans1, TransLog.class);
		System.out.println(transConv1);
		AccountDTO adto1 = new AccountDTO();
		
		if(a.isPresent() & a1.isPresent()) {
			
			//From account
			
			Double originalBal = Double.parseDouble(fields.get("balance").toString());
			
			Double bal = a.get().getBalance();
			
			bal -= Double.parseDouble(fields.get("balance").toString());
			
			fields.put("balance", bal);
			
			//MODELmap a to a dto the switch utils to dto class patch the changes then modelmap to an entity then save
			
			AccountDTO aConv = modelMapper.map(a.get(), AccountDTO.class);
			
	        fields.forEach((key, value) -> {
	        	
	        	if(key != "transLogDTO") {
	        	
		        	Field field = ReflectionUtils.findRequiredField(AccountDTO.class, (String) key);
		        	field.setAccessible(true);
		        	ReflectionUtils.setField(field, aConv, value);
		        	
	        	}
		        	
	        });
	        
	        Account aConvSaved = accountRepo.save(modelMapper.map(aConv, Account.class));
	        
	        fields.put("balance", originalBal);
	        	        
	        //To account
	        
			Double bal1 = a1.get().getBalance();
			
			bal1 += Double.parseDouble(fields.get("balance").toString());
			
			fields.put("balance", bal1);
	        
			AccountDTO aConv1 = modelMapper.map(a1.get(), AccountDTO.class);
			
			//you want the patching to stop at transLogDTO then to run a separate patching for transLogDto
	        fields.forEach((key, value) -> {
	        	
	        	if(key != "transLogDTO") {
	        	
		        	Field field1 = ReflectionUtils.findRequiredField(AccountDTO.class, (String) key);
		        	field1.setAccessible(true);
		        	ReflectionUtils.setField(field1, aConv1, value);
		        	
	        	}
		        	
	        });
	        
	        Account aConvSaved1 = accountRepo.save(modelMapper.map(aConv1, Account.class));
	        
	        //add the transaction before saving it
	        
	        List<TransLog> tlog = aConvSaved.getTranslog();
	        tlog.add(transConv);
	        
	        aConvSaved.setTranslog(tlog);
	        
	        Account updatedAcc = accountRepo.save(aConvSaved);
	               	        
	        //Build AccountDTO
		
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
			
	        List<TransLog> tlog1 = aConvSaved1.getTranslog();
	        tlog1.add(transConv1);
	        
	        aConvSaved1.setTranslog(tlog1);
	        
	        Account updatedAcc1 = accountRepo.save(aConvSaved1);
			
			adto1.setAccountId(updatedAcc1.getAccountId());
			adto1.setSortCode(updatedAcc1.getSortCode());
			adto1.setType(updatedAcc1.getType());
			adto1.setBalance(updatedAcc1.getBalance());
			adto1.setTransLogDTO(updatedAcc1.getTranslogDTO());
	        
			List<UserProfileDTO> upl1 = new ArrayList();
			
			for(int i = 0; i < updatedAcc1.getUserProfileID().length; i++) {
				
				if(updatedAcc1.getUserProfileID()[i] != null) {
					UserProfileDTO updto = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+ updatedAcc1.getUserProfileID()[i], UserProfileDTO.class);
					System.out.println(updto);
					upl1.add(updto);
					
				}
			}
			
			adto1.setUserProfileDTO(upl1);
		
		}
		
		List<AccountDTO> rtndAccs = new ArrayList<>();
		rtndAccs.add(adto);
		rtndAccs.add(adto1);
		
		return rtndAccs;
		
	}
	
}

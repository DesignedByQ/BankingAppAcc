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
import com.techprj.accounts.repo.TransRepo;
import com.techprj.banking.dto.UserProfileDTO;


@Service(value="ServiceDAO")
@Transactional
public class ServiceDAOImpl implements ServiceDAO{
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	AccountRepo accountRepo;
	
	@Autowired
	TransRepo transRepo;
	
	@Autowired
	RestTemplate restTemplate;

	@Override
	public AccountDTO createAccount(AccountDTO accountDTO) {
		
		Long[] ids = new Long[2];
		
		//System.out.println("----------------------------");
		
		//System.out.println(accountDTO.getUserProfileDTO());
		
		//System.out.println("----------------------------");
		
		//System.out.println(accountDTO.getUserProfileDTO().get(0).getIdUserProfile());
		
		for(int i = 0; i < accountDTO.getUserProfileDTO().size(); i++) {
			
			//System.out.println("----------------------------");
			
			//System.out.println(accountDTO.getUserProfileDTO().get(i).getIdUserProfile());
			
			ids[i] = accountDTO.getUserProfileDTO().get(i).getIdUserProfile();
			
			//System.out.println("----------------------------");
			
			//System.out.println(ids[i]);
		}
		
//		for(UserProfileDTO up: accountDTO.getUserProfileDTO()) {
//				
//		}
//		ids[0] = accountDTO.getUserProfileDTO()[0].getIdUserProfile();
//		
//		if(accountDTO.getUserProfileDTO().length > 1)
//			ids[1] = accountDTO.getUserProfileDTO()[1].getIdUserProfile();
		
		//System.out.println(ids[0]);
		Account a = new Account();
		a.setBalance(accountDTO.getBalance());
		a.setSortCode(accountDTO.getSortCode());
		a.setType(accountDTO.getType());
		a.setUserProfileID(ids);
		a.setTranslog(null);
			
		Account aSaved = accountRepo.saveAndFlush(a);
				
		System.out.println(aSaved);
		//accountDTO.setAccountId(aSaved.getAccountId());
		AccountDTO adto = new AccountDTO();
		adto.setAccountId(aSaved.getAccountId());
		adto.setBalance(aSaved.getBalance());
		adto.setSortCode(aSaved.getSortCode());
		adto.setType(aSaved.getType());
		//adto.setTransLogDTO(aSaved.getTranslogDTO());

		List<UserProfileDTO> upl = new ArrayList();
		
		Long id = aSaved.getUserProfileID()[0];
		
		String url = "http://localhost:8080/api/getprobyid/"+id;
		
		UserProfileDTO updto = restTemplate.getForObject(url, UserProfileDTO.class);
		
		upl.add(updto);
		
		if(aSaved.getUserProfileID()[1] != null) {
			
			Long id1 = aSaved.getUserProfileID()[1];
			
			UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+id1, UserProfileDTO.class);
			
			upl.add(updto1);
			
		}	
		
		adto.setUserProfileDTO(upl);
		
		//System.out.println(adto);
		
		//AccountDTO adto1 = new AccountDTO();
		return adto;
		
	}

	@Override
	public AccountDTO getAccount(Long accountid) {
		//System.out.println(111);
		//System.out.println(accountid);
		Optional<Account> a = accountRepo.findById(accountid);
		//System.out.println(a.get());
		AccountDTO adto1 = new AccountDTO();
//		if(a.isEmpty()) {
//		System.out.println("empty");
//		}
		
		//Set up id in long array
//		a.get().getUserProfileID()[0] = (long) 1;
//		a.get().getUserProfileID()[1] = (long) 9;
//		
//		Account as = accountRepo.save(a.get());
//		
//		System.out.println(as);
		
		if(a.isPresent()) {
			
			System.out.println(a.get());
		
			List<UserProfileDTO> upl = new ArrayList();
			
			//when creatiing an account need to inject the up ids correct instead of null
			Long id = a.get().getUserProfileID()[0];
			//Long id2 = Long.parseLong(id);
			//System.out.println(id.TYPE);
			
			String url = "http://localhost:8080/api/getprobyid/"+id;
		
			UserProfileDTO updto = restTemplate.getForObject(url, UserProfileDTO.class);
			//System.out.println(updto.getEmail());
			upl.add(updto);
			
			System.out.println("-------------------------");
			System.out.println(a.get());
			//System.out.println(a.get().getUserProfileID()[1]);
		
			System.out.println(upl.get(0));
			
			if(a.get().getUserProfileID()[1] != null) {
				
				Long id1 = a.get().getUserProfileID()[1];
				//System.out.println(id1);
				
				UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+id1, UserProfileDTO.class);
				
				upl.add(updto1);
				
				System.out.println(upl.get(1));
				
			}
			System.out.println(a.get());
			//System.out.println(upl.get(1));
			
			AccountDTO adto = new AccountDTO();
			adto.setAccountId(a.get().getAccountId());
			adto.setSortCode(a.get().getSortCode());
			adto.setType(a.get().getType());
			adto.setBalance(a.get().getBalance());
			
			List<TransLog> tl = transRepo.findByFromAcc(a.get().getAccountId());
			
			List<TransLogDTO> tldto = new ArrayList();
			
			for(TransLog t: tl) {
				
				TransLogDTO tdto = modelMapper.map(t, TransLogDTO.class);
				
				tldto.add(tdto);
				
			}
			
			System.out.println(tldto);
			
			List<TransLog> tl1 = transRepo.findByToAcc(a.get().getAccountId());
			
			for(TransLog t: tl1) {
				
				TransLogDTO tdto = modelMapper.map(t, TransLogDTO.class);
				
				tldto.add(tdto);
				
			}
			
			adto.setTransLogDTO(tldto);
			adto.setUserProfileDTO(upl);
			System.out.println(a.get());
			System.out.println(upl.get(0));
			return adto;
			
		}
		
		return adto1;
		//return null;
		
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
			
			return adto;
			
		}
		
		return null;
		
	}
	
	@Override
	public List<AccountDTO> updateBalanceInt(Long accid, Long accid1, Map<Object, Object> fields) {
		
		//get the value of the to acc and add it to the field balance before patching
		//get the val of the from acc and minus the field balance the patch the result
		//patch both accounts
		//create a transaction for both accounts
		//return a accountDTO
		
		//RTN to and from acc
		List<AccountDTO> rtndAccs = new ArrayList<>();
		
		//From account
		
		//Find Account
		
		Optional<Account> a = accountRepo.findById(accid);
		System.out.println("11111111111111111111111111111111");
		System.out.println(a);
		//Create dto transaction details for from acc 
		TransLogDTO trans = new TransLogDTO();
		trans.setDate(LocalDate.now());
		trans.setAmount(Double.parseDouble(fields.get("balance").toString()));
		trans.setFrom(accid);
		trans.setOldBal(a.get().getBalance());
		trans.setTo(accid1);
		trans.setNewBal(a.get().getBalance() - Double.parseDouble(fields.get("balance").toString()));
		trans.setReference(((Map<Object, Object>) fields.get("transLogDTO")).get("reference").toString());
		
		//Make entity version of the dto details 
		TransLog transConv = modelMapper.map(trans, TransLog.class);
		//System.out.println(transConv);
		System.out.println("22222222222222222222222222222222222222222");
		System.out.println(transConv);
		//If entity is found, +/- balance in field to suit transaction before patching
		if(a.isPresent()) {
		
			Double originalBal = Double.parseDouble(fields.get("balance").toString());
			
			Double bal = a.get().getBalance();
			
			bal -= Double.parseDouble(fields.get("balance").toString());
			
			fields.put("balance", bal);
		
			//Create dto of the found account entity to cycle through with a patch
			AccountDTO aConv = new AccountDTO();
			aConv.setAccountId(a.get().getAccountId());
			aConv.setBalance(a.get().getBalance());
			aConv.setSortCode(a.get().getSortCode());
			aConv.setType(a.get().getType());
			
			//Create up list to add to aConv
			List<UserProfileDTO> upl = new ArrayList();

			Long id = a.get().getUserProfileID()[0];
			
			String url = "http://localhost:8080/api/getprobyid/"+id;
		
			UserProfileDTO updto = restTemplate.getForObject(url, UserProfileDTO.class);
			
			upl.add(updto);
			
			if(a.get().getUserProfileID()[1] != null) {
				
				Long id1 = a.get().getUserProfileID()[1];
				
				UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+id1, UserProfileDTO.class);
				
				upl.add(updto1);
				
				//System.out.println(upl.get(1));
				
			}
			
			aConv.setUserProfileDTO(upl);
			System.out.println("3333333333333333333333333333333333");
			System.out.println(aConv);
			//System.out.println(aConv);
			//The patch will update the balance for now only
			
			//Patch and save the DTO you've just created
	        fields.forEach((key, value) -> {
	        	
	        	if(key != "transLogDTO" & key != "userProfileDTO") {
	        	
		        	Field field = ReflectionUtils.findRequiredField(AccountDTO.class, (String) key);
		        	field.setAccessible(true);
		        	ReflectionUtils.setField(field, aConv, value);
		        	
	        	}
		        	
	        });
			System.out.println("4444444444444444444444444444444444");
			System.out.println(aConv);
	        //this will lose the up upon converting to entity!!!
	        //Account aConvSaved = accountRepo.save(modelMapper.map(aConv, Account.class));
	        
	        //create a entity from aConv to be saved with new ups and balance!!!
	        
	        Account aConvEnt = new Account();
	        aConvEnt.setAccountId(aConv.getAccountId());
	        aConvEnt.setBalance(aConv.getBalance());
	        aConvEnt.setSortCode(aConv.getSortCode());
	        aConvEnt.setType(aConv.getType());
	        
	        //Add the UP ids to the entity
	        Long[] ids = new Long[2];
	        
	        for(int i = 0; i < aConv.getUserProfileDTO().size(); i++) {
	        	
	        	ids[i] = aConv.getUserProfileDTO().get(i).getIdUserProfile();
	        	
	        }
	        
	        aConvEnt.setUserProfileID(ids);
	              
	        //create transaction list from existing logs then add the new transaction before saving the entity
	        //why isnt it storing previous transactons
	        List<TransLog> tlog = new ArrayList();
	        
	        if(a.get().getTranslog() != null) {
	        
		        for(TransLog tl: a.get().getTranslog()) {
		        	tlog.add(tl);
		        }
		        
	        }
	        
	        tlog.add(transConv);
	        
	        aConvEnt.setTranslog(tlog);
	        
			System.out.println("5555555555555555555555555555555");
			System.out.println(aConvEnt);
	        
	        //Now you have entity with all fields updated now save it
	        
	        Account updatedAcc = accountRepo.save(aConvEnt);
			System.out.println("666666666666666666666666666666");
			System.out.println(updatedAcc);
	        //Reset the balance for the to acc
	        fields.put("balance", originalBal);
  
	        //Build AccountDTO from the entity to be returned to FE
			AccountDTO adto = new AccountDTO();
			adto.setAccountId(updatedAcc.getAccountId());
			adto.setSortCode(updatedAcc.getSortCode());
			adto.setType(updatedAcc.getType());
			adto.setBalance(updatedAcc.getBalance());
			adto.setTransLogDTO(updatedAcc.getTranslogDTO());
			
			//Add UPdto's 
			List<UserProfileDTO> upl1 = new ArrayList();
			
			for(int i = 0; i < updatedAcc.getUserProfileID().length; i++) {
				
				if(updatedAcc.getUserProfileID()[i] != null) {
					
					UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+ updatedAcc.getUserProfileID()[i], UserProfileDTO.class);

					upl1.add(updto1);
					
				}
			}
			
			adto.setUserProfileDTO(upl1);
			System.out.println("777777777777777777777777777777");
			System.out.println(adto);
			
			//Add fully created accounts with logs and ups to returned list
			rtndAccs.add(adto);
			
		}
			//To Acc
			
			//Find Account
			
			Optional<Account> a1 = accountRepo.findById(accid1);
			
			//Create dto transaction details for from acc 
			TransLogDTO trans1 = new TransLogDTO();
			trans1.setDate(LocalDate.now());
			trans1.setAmount(Double.parseDouble(fields.get("balance").toString()));
			trans1.setFrom(accid);
			trans1.setOldBal(a1.get().getBalance());
			trans1.setTo(accid1);
			trans1.setNewBal(a1.get().getBalance() + Double.parseDouble(fields.get("balance").toString()));
			trans1.setReference(((Map<Object, Object>) fields.get("transLogDTO")).get("reference").toString());
			
			TransLog transConv1 = modelMapper.map(trans1, TransLog.class);
			
			System.out.println("22222222222222222222222222222222222222222");
			System.out.println(transConv1);
			//If entity is found, +/- balance in field to suit transaction before patching
			if(a1.isPresent()) {

				Double bal1 = a1.get().getBalance();
				
				bal1 += Double.parseDouble(fields.get("balance").toString());
				
				fields.put("balance", bal1);
			    
				AccountDTO aConv1 = new AccountDTO();
				aConv1.setAccountId(a1.get().getAccountId());
				aConv1.setBalance(a1.get().getBalance());
				aConv1.setSortCode(a1.get().getSortCode());
				aConv1.setType(a1.get().getType());
			
				//Create up list to add to aConv
				List<UserProfileDTO> upl2 = new ArrayList();
			
				Long id2 = a1.get().getUserProfileID()[0];
				
				String url2 = "http://localhost:8080/api/getprobyid/"+id2;
			
				UserProfileDTO updto2 = restTemplate.getForObject(url2, UserProfileDTO.class);
				
				upl2.add(updto2);
				
				if(a1.get().getUserProfileID()[1] != null) {
					
					Long id3 = a1.get().getUserProfileID()[1];
					
					UserProfileDTO updto3 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+id3, UserProfileDTO.class);
					
					upl2.add(updto3);
					
					//System.out.println(upl2.get(1));
					
				}
				
				aConv1.setUserProfileDTO(upl2);
				
				System.out.println(aConv1);				

				//The patch will update the balance for now only
				
				//you want the patching to stop at transLogDTO then to run a separate patching for transLogDto
				//Patch and save the DTO you've just created
			    fields.forEach((key, value) -> {
			    	
			    	if(key != "transLogDTO" & key != "userProfileDTO") {
			    	
			        	Field field1 = ReflectionUtils.findRequiredField(AccountDTO.class, (String) key);
			        	field1.setAccessible(true);
			        	ReflectionUtils.setField(field1, aConv1, value);
			        	
			    	}
			        	
			    });
			    
			  		
		
				System.out.println("4444444444444444444444444444444444");
				System.out.println(aConv1);
		
		        
		        //create a entity from aConv to be saved with new ups and balance
		        
		        Account aConvEnt1 = new Account();
		        aConvEnt1.setAccountId(aConv1.getAccountId());
		        aConvEnt1.setBalance(aConv1.getBalance());
		        aConvEnt1.setSortCode(aConv1.getSortCode());
		        aConvEnt1.setType(aConv1.getType());
		        
		        //Add the UP ids to the entity
		        Long[] ids1 = new Long[2];
		        
		        for(int i = 0; i < aConv1.getUserProfileDTO().size(); i++) {
		        	
		        	ids1[i] = aConv1.getUserProfileDTO().get(i).getIdUserProfile();
		        	
		        }
		        
		        aConvEnt1.setUserProfileID(ids1);
		        	              
		        //create transaction list from existing logs then add the new transaction before saving the entity
		    
		        List<TransLog> tlog1 = new ArrayList();
		        
		        if(a1.get().getTranslog() != null) {
		        
			        for(TransLog tl: a1.get().getTranslog()) {
			        	tlog1.add(tl);
			        }
			        
		        }
		        
		        tlog1.add(transConv1);
		        
		        aConvEnt1.setTranslog(tlog1);
		        
				System.out.println("5555555555555555555555555555555");
				System.out.println(aConvEnt1);
		        
		        //Now you have entity with all fields updated now save it
		        
		        Account updatedAcc1 = accountRepo.save(aConvEnt1);
				System.out.println("666666666666666666666666666666");
				System.out.println(updatedAcc1);
	  
		        //Build AccountDTO from the entity to be returned to FE
				AccountDTO adto1 = new AccountDTO();
				adto1.setAccountId(updatedAcc1.getAccountId());
				adto1.setSortCode(updatedAcc1.getSortCode());
				adto1.setType(updatedAcc1.getType());
				adto1.setBalance(updatedAcc1.getBalance());
				adto1.setTransLogDTO(updatedAcc1.getTranslogDTO());
				
				//Add UPdto's 
				List<UserProfileDTO> upl3 = new ArrayList();
				
				for(int i = 0; i < updatedAcc1.getUserProfileID().length; i++) {
					
					if(updatedAcc1.getUserProfileID()[i] != null) {
						
						UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+ updatedAcc1.getUserProfileID()[i], UserProfileDTO.class);

						upl3.add(updto1);
						
					}
				}
				
				adto1.setUserProfileDTO(upl3);
				System.out.println("777777777777777777777777777777");
				System.out.println(adto1);

			
			//Add fully created accounts with logs and ups to returned list

			rtndAccs.add(adto1);
			
			return rtndAccs;
		}
		
		return null;
	
	}
	
	@Override
	public AccountDTO updateBalanceExt(Long accid, Long accid1, Map<Object, Object> fields) {
		
		//From account
		
		//Find Account
		
		Optional<Account> a = accountRepo.findById(accid);
		System.out.println("11111111111111111111111111111111");
		System.out.println(a);
		//Create dto transaction details for from acc 
		TransLogDTO trans = new TransLogDTO();
		trans.setDate(LocalDate.now());
		trans.setAmount(Double.parseDouble(fields.get("balance").toString()));
		trans.setFrom(accid);
		trans.setOldBal(a.get().getBalance());
		trans.setTo(accid1);
		trans.setNewBal(a.get().getBalance() - Double.parseDouble(fields.get("balance").toString()));
		trans.setReference(((Map<Object, Object>) fields.get("transLogDTO")).get("reference").toString());
		
		//Make entity version of the dto details 
		TransLog transConv = modelMapper.map(trans, TransLog.class);
		//System.out.println(transConv);
		System.out.println("22222222222222222222222222222222222222222");
		System.out.println(transConv);
		//If entity is found, +/- balance in field to suit transaction before patching
		if(a.isPresent()) {
		
			Double originalBal = Double.parseDouble(fields.get("balance").toString());
			
			Double bal = a.get().getBalance();
			
			bal -= Double.parseDouble(fields.get("balance").toString());
			
			fields.put("balance", bal);
		
			//Create dto of the found account entity to cycle through with a patch
			AccountDTO aConv = new AccountDTO();
			aConv.setAccountId(a.get().getAccountId());
			aConv.setBalance(a.get().getBalance());
			aConv.setSortCode(a.get().getSortCode());
			aConv.setType(a.get().getType());
			
			//Create up list to add to aConv
			List<UserProfileDTO> upl = new ArrayList();

			Long id = a.get().getUserProfileID()[0];
			
			String url = "http://localhost:8080/api/getprobyid/"+id;
		
			UserProfileDTO updto = restTemplate.getForObject(url, UserProfileDTO.class);
			
			upl.add(updto);
			
			if(a.get().getUserProfileID()[1] != null) {
				
				Long id1 = a.get().getUserProfileID()[1];
				
				UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+id1, UserProfileDTO.class);
				
				upl.add(updto1);
				
				//System.out.println(upl.get(1));
				
			}
			
			aConv.setUserProfileDTO(upl);
			System.out.println("3333333333333333333333333333333333");
			System.out.println(aConv);
			//System.out.println(aConv);
			//The patch will update the balance for now only
			
			//Patch and save the DTO you've just created
	        fields.forEach((key, value) -> {
	        	
	        	if(key != "transLogDTO" & key != "userProfileDTO") {
	        	
		        	Field field = ReflectionUtils.findRequiredField(AccountDTO.class, (String) key);
		        	field.setAccessible(true);
		        	ReflectionUtils.setField(field, aConv, value);
		        	
	        	}
		        	
	        });
			System.out.println("4444444444444444444444444444444444");
			System.out.println(aConv);
	        
	        //create a entity from aConv to be saved with new ups and balance!!!
	        
	        Account aConvEnt = new Account();
	        aConvEnt.setAccountId(aConv.getAccountId());
	        aConvEnt.setBalance(aConv.getBalance());
	        aConvEnt.setSortCode(aConv.getSortCode());
	        aConvEnt.setType(aConv.getType());
	        
	        //Add the UP ids to the entity
	        Long[] ids = new Long[2];
	        
	        for(int i = 0; i < aConv.getUserProfileDTO().size(); i++) {
	        	
	        	ids[i] = aConv.getUserProfileDTO().get(i).getIdUserProfile();
	        	
	        }
	        
	        aConvEnt.setUserProfileID(ids);
	              
	        //create transaction list from existing logs then add the new transaction before saving the entity
	        //why isnt it storing previous transactons
	        List<TransLog> tlog = new ArrayList();
	        
	        if(a.get().getTranslog() != null) {
	        
		        for(TransLog tl: a.get().getTranslog()) {
		        	tlog.add(tl);
		        }
		        
	        }
	        
	        tlog.add(transConv);
	        
	        aConvEnt.setTranslog(tlog);
	        
			System.out.println("5555555555555555555555555555555");
			System.out.println(aConvEnt);
	        
	        //Now you have entity with all fields updated now save it
	        
	        Account updatedAcc = accountRepo.save(aConvEnt);
			System.out.println("666666666666666666666666666666");
			System.out.println(updatedAcc);
	        //Reset the balance for the to acc
	        fields.put("balance", originalBal);
  
	        //Build AccountDTO from the entity to be returned to FE
			AccountDTO adto = new AccountDTO();
			adto.setAccountId(updatedAcc.getAccountId());
			adto.setSortCode(updatedAcc.getSortCode());
			adto.setType(updatedAcc.getType());
			adto.setBalance(updatedAcc.getBalance());
			adto.setTransLogDTO(updatedAcc.getTranslogDTO());
			
			//Add UPdto's 
			List<UserProfileDTO> upl1 = new ArrayList();
			
			for(int i = 0; i < updatedAcc.getUserProfileID().length; i++) {
				
				if(updatedAcc.getUserProfileID()[i] != null) {
					
					UserProfileDTO updto1 = restTemplate.getForObject("http://localhost:8080/api/getprobyid/"+ updatedAcc.getUserProfileID()[i], UserProfileDTO.class);

					upl1.add(updto1);
					
				}
			}
			
			adto.setUserProfileDTO(upl1);
			System.out.println("777777777777777777777777777777");
			System.out.println(adto);

			return adto;
		}
		
		return null;
	
	}
	
}
	


		
	
		

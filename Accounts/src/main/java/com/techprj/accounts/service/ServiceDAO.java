package com.techprj.accounts.service;

import java.util.List;
import java.util.Map;

import com.techprj.accounts.dto.AccountDTO;

public interface ServiceDAO {
	
	AccountDTO createAccount(AccountDTO accountDTO);
	
	AccountDTO getAccount(Long accountid);
	
	AccountDTO updateBalance(Long accid, Map<Object, Object> fields);
	
	List<AccountDTO> updateBalanceInt(Long accid, Long accid1, Map<Object, Object> fields);

	AccountDTO updateBalanceExt(Long accid, Long accid1, Map<Object, Object> fields);

}

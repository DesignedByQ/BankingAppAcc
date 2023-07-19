package com.techprj.accounts.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techprj.accounts.entity.Account;
import com.techprj.accounts.entity.TransLog;

@Repository
public interface TransRepo extends JpaRepository<TransLog, Long>{

	List<TransLog> findByFromAcc(Long accountId);

	List<TransLog> findByToAcc(Long accountId);

}

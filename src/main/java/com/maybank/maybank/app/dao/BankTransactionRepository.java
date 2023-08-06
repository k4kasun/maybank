package com.maybank.maybank.app.dao;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.maybank.maybank.app.entity.BankTransaction;


public interface BankTransactionRepository extends JpaRepository<BankTransaction, Integer> {
	
	Page<BankTransaction> findByAccountNumberOrCustomerIdOrDescription(String AccountNumber, Integer CustomerId, String description, Pageable pageable);

	Page<BankTransaction> findAll(Pageable pageable);
	
	@Modifying
	@Query("update BankTransaction b set b.description=:description where b.accountNumber=:id")
	public void updateByAccountNumber(@Param("description") String description, @Param("id") String accountNumber);
	
}
	
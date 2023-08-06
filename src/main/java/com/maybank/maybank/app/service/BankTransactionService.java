package com.maybank.maybank.app.service;

import java.util.List;

//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.maybank.maybank.app.entity.BankTransaction;

public interface BankTransactionService {
	
	public List<BankTransaction> getTransaction();
	
	public Page<BankTransaction> getByAccountNumberOrCustomerIdOrDescription(String accountNumber, Integer CustomerId, String description, Pageable paging);
	

	public Page<BankTransaction> getByAccountNumbers(Pageable paging);
	
	public BankTransaction updateRecord(String accountNumber, String description);
	
}

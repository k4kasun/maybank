package com.maybank.maybank.app.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.maybank.maybank.app.dao.BankTransactionRepository;
import com.maybank.maybank.app.entity.BankTransaction;
import com.maybank.maybank.app.service.BankTransactionService;

import jakarta.transaction.Transactional;
@Service
public class BankTransactionServiceImpl implements  BankTransactionService{

	@Autowired
	BankTransactionRepository bankTransactionRepository;

	@Override
	public List<BankTransaction> getTransaction() {
		 
		return bankTransactionRepository.findAll();
	}

	@Override
	public Page<BankTransaction> getByAccountNumberOrCustomerIdOrDescription(String accountNumber, Integer CustomerId, String description, Pageable pageable) {
		// TODO Auto-generated method stub
		return bankTransactionRepository.findByAccountNumberOrCustomerIdOrDescription(accountNumber, CustomerId, description, pageable);
	}
	@Override
	public Page<BankTransaction> getByAccountNumbers(Pageable pageable) {
		
		return bankTransactionRepository.findAll(pageable);
		
	}
	@Transactional
	@Override
	public BankTransaction updateRecord(String accountNumber, String description) {
			bankTransactionRepository.updateByAccountNumber(description, accountNumber);
		return null;
	}
	
}

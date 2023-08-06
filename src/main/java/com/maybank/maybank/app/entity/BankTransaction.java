package com.maybank.maybank.app.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "bank_transactions")
public class BankTransaction {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private int Id;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "trx_amount")
	private String trxAmount;
	
	@Column(name = "description")
	private String description;

	@Column(name = "trx_date")
	private String trxDate;
	
	@Column(name = "trx_time")
	private String trxTime;
	
	@Column(name = "customer_id")
	private int customerId;	
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "version")
	private int version;

	

	
}





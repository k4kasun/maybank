package com.maybank.maybank.app.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.maybank.maybank.app.dao.BankTransactionRepository;
import com.maybank.maybank.app.entity.BankTransaction;
import com.maybank.maybank.app.request.UpdateTransactionRequest;
import com.maybank.maybank.app.service.BankTransactionService;

@RestController
@RequestMapping("/api")
@Slf4j
public class TransactionController {

	@Autowired
	BankTransactionService bankTransactionService;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@GetMapping(path = "/BankRecords", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> getTransactionPageble(
			@RequestParam(name = "account_number", required = false, defaultValue = "") String accountNumber,
			@RequestParam(name = "customer_id", required = false, defaultValue = "0") int customerId,
			@RequestParam(name = "description", required = false, defaultValue = "") String description,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		log.info("Initiating|getRecordsPagable");
		long startTime = System.currentTimeMillis();
		log.info("ReqParam|account_number={} customer_id={} description={} page={}", accountNumber, customerId,
				description, page);

		int size = 5;

		try {
			Pageable paging = PageRequest.of(page, size);

			Page<BankTransaction> bankTransactions = bankTransactionService
					.getByAccountNumberOrCustomerIdOrDescription(accountNumber, customerId, description, paging);

			List<BankTransaction> bankTransactionsList = bankTransactions.getContent();

			Map<String, Object> response = new HashMap<>();
			response.put("records", bankTransactionsList);
			response.put("currentPage", bankTransactions.getNumber());
			response.put("totalItemsPage", bankTransactions.getTotalElements());
			response.put("totalPAges", bankTransactions.getTotalPages());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} finally {
			log.info("Completed|getRecordsPagable");
			log.info("ProcessingTime|{}ms", System.currentTimeMillis() - startTime);
		}

	}

	@PatchMapping(path = "/BankRecords", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateAccountDescription(@RequestBody UpdateTransactionRequest updateTransaction) {
		long startTime = System.currentTimeMillis();
		
		try {
			bankTransactionService.updateRecord(updateTransaction.getAccountNumber(),
					updateTransaction.getDescription());
			return ResponseEntity.ok("success");
		} finally {
			log.info("Completed|BankRecords");
			log.info("ProcessingTime|{}ms", System.currentTimeMillis() - startTime);
		}

	}

	@GetMapping("/triggerBatchJob")
	public BatchStatus load() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
			JobParametersInvalidException, JobRestartException {

		JobParameters jobParameters = new JobParametersBuilder().addDate("timestamp", Calendar.getInstance().getTime())
				.toJobParameters();

		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		while (jobExecution.isRunning()) {
			System.out.println("..................");
		}
		return jobExecution.getStatus();
	}

}

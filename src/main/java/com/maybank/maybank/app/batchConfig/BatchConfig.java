package com.maybank.maybank.app.batchConfig;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.maybank.maybank.app.dao.BankTransactionRepository;
import com.maybank.maybank.app.entity.BankTransaction;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
//@RequiredArgsConstructor
public class BatchConfig extends DefaultBatchConfiguration {
	
	@Autowired
	private BankTransactionRepository bankTransactionRepository;
	
	 @Autowired
	 private BankTransaction bankTransaction;
	
	@Bean
    public ItemWriter<BankTransaction> writer() {
		
        return bankTransactionRepository::saveAll;
    }	
	
	
	@Autowired
    private PlatformTransactionManager transactionManager;
	
	@Autowired
   private JobRepository jobRepository;
	
	@Bean
    public BankTransaction bankTransaction() {
        return new BankTransaction();
    }
	
	@Bean
	public ItemReader<BankTransaction >  BankTransactionItemReader(){
		return new FlatFileItemReaderBuilder<BankTransaction>()
				.name("readerstep")
				.resource(new ClassPathResource("dataSource.csv"))
				.linesToSkip(1)
				.delimited().delimiter("|")				
				.names("ACCOUNT_NUMBER","TRX_AMOUNT","DESCRIPTION","TRX_DATE","TRX_TIME","CUSTOMER_ID")
				.targetType(BankTransaction.class)
				.build();   
				
	}
	
	
	 @Bean
	 public Step importStep(JobRepository jobRepository, BankTransaction bankTransaction, PlatformTransactionManager transactionManager) {
	        return new StepBuilder("importStep", jobRepository)
	                .<BankTransaction, BankTransaction>chunk(100, transactionManager)
	                .reader(BankTransactionItemReader())	               
	                .writer(writer())
	                .build();
	 }
	 
	 @Bean
	 public Job importJob() {
	        return new JobBuilder("importJob", jobRepository)
	                .start(importStep(jobRepository, bankTransaction, transactionManager))
	                .build();
	    }
	
}
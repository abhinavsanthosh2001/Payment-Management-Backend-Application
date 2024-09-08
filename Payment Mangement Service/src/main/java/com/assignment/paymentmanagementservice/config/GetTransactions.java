package com.assignment.paymentmanagementservice.config;

import com.assignment.paymentmanagementservice.constants.PaymentStatus;
import com.assignment.paymentmanagementservice.entities.Payment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GetTransactions {
    StepBuilderFactory stepBuilderFactory;
    JobBuilderFactory jobBuilderFactory;
    DataSource dataSource;

    @Bean
    public JdbcCursorItemReader<Payment> getTransactionsReader() {
        JdbcCursorItemReader<Payment> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("Select * from payments");
        reader.setRowMapper((rs, rowNum) -> {
            Payment payment = new Payment();
            payment.setTransactionId(rs.getString("transaction_id"));
            payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
            return payment;
        });
        return reader;
    }

    @Bean
    public FlatFileItemWriter<Payment> getTransactionsWriter() {
        FlatFileItemWriter<Payment> writer = new FlatFileItemWriter<>();
        writer.setHeaderCallback(writer1 -> {
            String s = "transactionId,status";
            writer1.write(s);
        });
        writer.setResource(new ClassPathResource("getTransactions.csv"));

        DelimitedLineAggregator<Payment> aggregator = new DelimitedLineAggregator<>();
        aggregator.setDelimiter(",");
        BeanWrapperFieldExtractor<Payment> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"transactionId", "status"});
        aggregator.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(aggregator);
        return writer;
    }

    @Bean
    public Step getTransactionsStep() {
        return stepBuilderFactory.get("GetTransactionsStep").<Payment, Payment>chunk(999)
                .reader(getTransactionsReader())
                .writer(getTransactionsWriter()).build();
    }

    @Bean(name = "getTransaction")
    public Job exportPersonJob() {
        return jobBuilderFactory.get("getTransaction")
                .incrementer(new RunIdIncrementer())
                .flow(getTransactionsStep())
                .end()
                .build();
    }
}

package com.assignment.paymentmanagementservice.config;

import com.assignment.paymentmanagementservice.constants.Constants;
import com.assignment.paymentmanagementservice.constants.PaymentModes;
import com.assignment.paymentmanagementservice.constants.PaymentStatus;
import com.assignment.paymentmanagementservice.dto.OrderDto;
import com.assignment.paymentmanagementservice.entities.Payment;
import com.assignment.paymentmanagementservice.services.OrderService;
import com.assignment.paymentmanagementservice.util.EntityDtoConversions;
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
public class ExportPaymentStatus {
    StepBuilderFactory stepBuilderFactory;
    JobBuilderFactory jobBuilderFactory;
    DataSource dataSource;
    OrderService orderService;
    EntityDtoConversions entityDtoConversions;

    @Bean
    public JdbcCursorItemReader<Payment> exportReader() {
        JdbcCursorItemReader<Payment> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("Select * from payments");
        reader.setRowMapper((rs, rowNum) -> {
            Payment payment = new Payment();
            payment.setTransactionId(rs.getString("transaction_id"));
            payment.setAmount(rs.getDouble("amount"));
            payment.setStatus(PaymentStatus.valueOf(rs.getString("status")));
            payment.setPaymentMode(PaymentModes.valueOf(rs.getString("payment_mode")));
            OrderDto order = orderService.getOrderById(rs.getString("order_id"));
            payment.setOrder(entityDtoConversions.orderDtoToEntity(order));
            payment.getOrder().setOrderId(rs.getString("order_id"));
            return payment;
        });
        return reader;
    }

    @Bean
    public FlatFileItemWriter<Payment> exportWriter() {
        FlatFileItemWriter<Payment> writer = new FlatFileItemWriter<>();
        writer.setHeaderCallback(writerHeader -> {
            String s = "transactionId,amount,status,paymentMode,orderId";
            writerHeader.write(s);
        });
        writer.setResource(new ClassPathResource(Constants.PAYMENTS_FILE));

        DelimitedLineAggregator<Payment> aggregator = new DelimitedLineAggregator<>();
        aggregator.setDelimiter(",");
        BeanWrapperFieldExtractor<Payment> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"transactionId", "amount", "status", "paymentMode", "order.orderId"});
        aggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(aggregator);
        return writer;
    }

    @Bean
    public Step exportStep() {
        return stepBuilderFactory.get("step1").<Payment, Payment>chunk(999)
                .reader(exportReader())
                .writer(exportWriter())
                .build();
    }

    @Bean(name = "Export")
    public Job exportPersonJob() {
        return jobBuilderFactory.get("exportPersonJob")
                .incrementer(new RunIdIncrementer())
                .flow(exportStep())
                .end()
                .build();
    }
}

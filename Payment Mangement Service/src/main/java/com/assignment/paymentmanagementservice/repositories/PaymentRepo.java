package com.assignment.paymentmanagementservice.repositories;

import com.assignment.paymentmanagementservice.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    @Modifying
    @Transactional
    @Query(value = "update payments set status=:status where transaction_id=:tId", nativeQuery = true)
    void updateStatus(@Param("tId") String transactionId, @Param("status") String status);

    List<Payment> findByOrderOrderId(String orderId);

    Payment findByTransactionId(String transactionId);


}

package com.assignment.paymentmanagementservice.repositories;

import com.assignment.paymentmanagementservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query(value = "Select * from orders join customers on orders.customer_id=customers.customer_id where customers.phone_number=:pNo", nativeQuery = true)
    List<Order> getOrdersWithPhoneNumber(@Param("pNo") String phoneNumber);

    @Query(value = "Select * from orders join customers on orders.customer_id=customers.customer_id where customers.email=:email", nativeQuery = true)
    List<Order> getOrdersWithEmail(@Param("email") String email);

    boolean existsByOrderId(String orderId);

    @Query(value = "Update orders set status=:status where order_id=:oId", nativeQuery = true)
    @Transactional
    @Modifying
    void updateStatus(@Param("status") String orderConfirmed, @Param("oId") String orderId);

    Order findByOrderId(String orderId);
}

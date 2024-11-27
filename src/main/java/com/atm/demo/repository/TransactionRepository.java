package com.atm.demo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atm.demo.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByType(String type);
    int countByTimestampAfter(Date timestamp);  // Count transactions in the last 24 hours
}

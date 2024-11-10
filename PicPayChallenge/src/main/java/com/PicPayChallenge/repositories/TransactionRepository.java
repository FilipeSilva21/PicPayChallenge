package com.PicPayChallenge.repositories;

import com.PicPayChallenge.domain.Transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

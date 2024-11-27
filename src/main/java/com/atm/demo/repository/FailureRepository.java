package com.atm.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atm.demo.model.Failure;

public interface FailureRepository extends JpaRepository<Failure, Long> {
    List<Failure> findAll();
}

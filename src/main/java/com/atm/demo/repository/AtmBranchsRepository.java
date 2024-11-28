package com.atm.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.atm.demo.model.AtmBranchs;

public interface AtmBranchsRepository extends JpaRepository<AtmBranchs, Long> {
}

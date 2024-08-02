package com.leultewolde.budgeteer_backend.repository;

import com.leultewolde.budgeteer_backend.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}

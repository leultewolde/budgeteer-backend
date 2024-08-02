package com.leultewolde.budgeteer_backend.repository;

import com.leultewolde.budgeteer_backend.model.ActualIncome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActualIncomeRepository extends JpaRepository<ActualIncome, Long> {
}

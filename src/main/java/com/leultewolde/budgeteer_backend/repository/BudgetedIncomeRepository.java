package com.leultewolde.budgeteer_backend.repository;

import com.leultewolde.budgeteer_backend.model.BudgetedIncome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetedIncomeRepository extends JpaRepository<BudgetedIncome, Long> {
}

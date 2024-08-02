package com.leultewolde.budgeteer_backend.repository;

import com.leultewolde.budgeteer_backend.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}

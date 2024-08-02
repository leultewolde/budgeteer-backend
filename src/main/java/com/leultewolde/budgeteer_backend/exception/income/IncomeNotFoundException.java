package com.leultewolde.budgeteer_backend.exception.income;

public class IncomeNotFoundException extends RuntimeException {
    public IncomeNotFoundException(Long incomeId) {
        super("Income not found with ID: " + incomeId);
    }
}

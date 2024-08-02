package com.leultewolde.budgeteer_backend.exception.expense;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException(Long expenseId) {
        super("Expense not found with ID: " + expenseId);
    }
}

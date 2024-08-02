package com.leultewolde.budgeteer_backend.exception.expense;

public class ExpenseSaveException extends RuntimeException {
    public ExpenseSaveException(String message) {
        super(message);
    }
}

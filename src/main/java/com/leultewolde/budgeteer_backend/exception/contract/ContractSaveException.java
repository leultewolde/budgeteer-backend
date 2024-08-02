package com.leultewolde.budgeteer_backend.exception.contract;

public class ContractSaveException extends RuntimeException {
    public ContractSaveException(String message) {
        super(message);
    }
}

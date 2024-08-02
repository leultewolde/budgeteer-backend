package com.leultewolde.budgeteer_backend.exception.contract;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(Long contractId) {
        super("Contract not found with ID: " + contractId);
    }
}


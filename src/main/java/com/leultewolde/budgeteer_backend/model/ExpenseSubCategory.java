package com.leultewolde.budgeteer_backend.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExpenseSubCategory {
    RENT(30),
    UTILITIES(null),
    GROCERIES(null),
    MIN_DEBT_PAYMENTS(null),
    DINING_OUT(null),
    HOBBIES(null),
    ENTERTAINMENT(null),
    EMERGENCY_FUND(null),
    INVESTING(null),
    ADDITIONAL_DEBT_PAYMENTS(null),
    OTHERS(null);

    private final Integer percentageOfIncome;
}
